package models

import com.mohiva.play.silhouette.api.Identity

case class User(id: Long, name: String, email: String, activated: Boolean) extends Identity
