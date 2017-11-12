package services

import com.mohiva.play.silhouette.api.services.IdentityService
import models.User

trait UserService extends IdentityService[User]{

  def save(user: User): User

}
