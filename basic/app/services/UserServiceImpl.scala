package services
import java.util.concurrent.atomic.AtomicLong
import javax.inject.Singleton

import com.mohiva.play.silhouette.api.LoginInfo
import models.User

import scala.collection.mutable
import scala.concurrent.Future

@Singleton
class UserServiceImpl extends UserService {

  import UserServiceImpl._

  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = Future.successful(
    users.find { case (_, user) => user.loginInfo == loginInfo }.map(_._2)
  )

  override def save(user: User): User = {
    val nextId =idCounter.incrementAndGet()
    val newUser = user.copy(id = Some(nextId))
    users += (nextId -> newUser)
    newUser
  }

}

object UserServiceImpl {
  val idCounter: AtomicLong = new AtomicLong(0)
  val users: mutable.HashMap[Long, User] = mutable.HashMap()
}