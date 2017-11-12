package services
import javax.inject.Singleton

import com.mohiva.play.silhouette.api.LoginInfo

@Singleton
class UserServiceImpl extends UserService {
  override def retrieve(loginInfo: LoginInfo) = ???
}
