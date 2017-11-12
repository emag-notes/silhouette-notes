package controllers

import javax.inject._

import com.mohiva.play.silhouette.api.actions.UnsecuredErrorHandler
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasherRegistry
import com.mohiva.play.silhouette.api.{LoginInfo, SignUpEvent, Silhouette}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import forms.SignUpForm
import models.User
import play.api.mvc._
import services.UserService
import utils.auth.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SignUpController @Inject()(
                                  cc: ControllerComponents,
                                  silhouette: Silhouette[DefaultEnv],
                                  userService: UserService,
                                  authInfoRepository: AuthInfoRepository,
                                  passwordHasherRegistry: PasswordHasherRegistry
                                )(implicit ex: ExecutionContext)
  extends AbstractController(cc) {

  val unSecuredErrorHandler = new UnsecuredErrorHandler {
    override def onNotAuthorized(implicit request: RequestHeader): Future[Result] = {
      Future.successful(Redirect(controllers.routes.SecuredController.index()))
    }
  }

  def view = silhouette.UnsecuredAction(unSecuredErrorHandler) { implicit request: Request[AnyContent] =>
    Ok(views.html.sign_up(SignUpForm.form))
  }

  def submit = silhouette.UnsecuredAction.async { implicit request: Request[AnyContent] =>
    SignUpForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.sign_up(form))),
      data => {
        val loginInfo = LoginInfo(CredentialsProvider.ID, data.email)
        userService.retrieve(loginInfo).flatMap {
          case Some(_) =>
            Future.successful(Redirect(routes.SignUpController.view()))
          case None =>
            val authInfo = passwordHasherRegistry.current.hash(data.password)
            authInfoRepository.add(loginInfo, authInfo)

            val user = User(
              None,
              loginInfo,
              data.name,
              data.email,
              data.password
            )
            val newUser = userService.save(user)
            silhouette.env.eventBus.publish(SignUpEvent(newUser, request))
            Future.successful(Redirect(routes.SignInController.view()))
        }
      }
    )
  }

}
