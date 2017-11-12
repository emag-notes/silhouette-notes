package controllers

import javax.inject._

import com.mohiva.play.silhouette.api.exceptions.ProviderException
import com.mohiva.play.silhouette.api.util.Credentials
import com.mohiva.play.silhouette.api.{LoginEvent, Silhouette}
import com.mohiva.play.silhouette.impl.exceptions.IdentityNotFoundException
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import forms.SignInForm
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.UserService
import utils.auth.DefaultEnv

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SignInController @Inject()(
                                  cc: ControllerComponents,
                                  silhouette: Silhouette[DefaultEnv],
                                  userService: UserService,
                                  credentialsProvider: CredentialsProvider
                                )(implicit ex: ExecutionContext)
  extends AbstractController(cc) {

  def view = silhouette.UnsecuredAction { implicit request: Request[AnyContent] =>
    Ok(views.html.sign_in(SignInForm.form))
  }

  def submit = silhouette.UnsecuredAction.async { implicit request: Request[AnyContent] =>
    SignInForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.sign_in(form))),
      data => {
        val credentials = Credentials(data.email, data.password)
        credentialsProvider.authenticate(credentials).flatMap { loginInfo =>
          userService.retrieve(loginInfo).flatMap {
            case Some(user) =>
              silhouette.env.authenticatorService.create(loginInfo).flatMap { authenticator =>
                silhouette.env.eventBus.publish(LoginEvent(user, request))
                silhouette.env.authenticatorService.init(authenticator).flatMap { v =>
                  silhouette.env.authenticatorService.embed(v, Redirect(routes.SecuredController.index()))
                }
              }
            case None => Future.failed(new IdentityNotFoundException("Couldn't find user"))
          }
        }.recover {
          case _: ProviderException =>
            Redirect(routes.SignInController.view())
        }
      }
    )
  }

}
