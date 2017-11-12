package controllers

import javax.inject._

import com.mohiva.play.silhouette.api.Silhouette
import forms.SignUpForm
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.UserService
import utils.auth.DefaultEnv

@Singleton
class SignUpController @Inject()(
                                  cc: ControllerComponents,
                                  silhouette: Silhouette[DefaultEnv],
                                  userService: UserService
                                )
  extends AbstractController(cc) {

  def view = silhouette.UnsecuredAction { implicit request: Request[AnyContent] =>
    Ok(views.html.sign_up(SignUpForm.form))
  }

  def submit = silhouette.UnsecuredAction { implicit request: Request[AnyContent] =>
    SignUpForm.form.bindFromRequest.fold(
      form => BadRequest(views.html.sign_up(form)),
      data => {
        Ok(views.html.index())
      }
    )
  }

}
