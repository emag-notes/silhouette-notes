package controllers

import javax.inject._

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import play.api.mvc._
import utils.auth.DefaultEnv

@Singleton
class SecuredController @Inject()(
                                   cc: ControllerComponents,
                                   silhouette: Silhouette[DefaultEnv])
  extends AbstractController(cc) {

  def index() = silhouette.SecuredAction { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Ok(views.html.secured(request.identity))
  }

}
