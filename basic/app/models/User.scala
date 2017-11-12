package models

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}

case class User(id: Option[Long], loginInfo: LoginInfo, name: String, email: String, password: String) extends Identity
