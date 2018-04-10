package org.ben.bentwitter

import cats.effect.IO
import doobie.implicits._
import org.ben.bentwitter.BenTwitterServer.{User, xa}

object Auth {
  trait SecurityContext
  case class Authorized(user: User) extends SecurityContext
  case class Unauthorized() extends SecurityContext

  def login(handle: String, password: String): IO[SecurityContext] = {
    verifyUserInfo(handle, password).map(selectUser)
  }

  def selectUser(user: Option[User]): SecurityContext = {
    user match {
      case Some(user) => Authorized(user)
      case None => Unauthorized()
    }
  }

  def verifyUserInfo(handle: String, password: String): IO[Option[User]] = {
    sql"SELECT * FROM USERS WHERE handle = '$handle' AND password= '$password'"
      .query[User]
      .option
      .transact(xa)
  }
}
