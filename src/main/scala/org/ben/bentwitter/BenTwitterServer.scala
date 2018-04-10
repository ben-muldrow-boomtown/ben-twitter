package org.ben.bentwitter

import java.sql.Date
import java.time.{LocalDate, ZoneId}

import cats.effect.IO
import doobie._
import doobie.implicits._
import fs2.StreamApp
import io.circe._
import org.ben.bentwitter.Auth.Authorized
import org.http4s._
import org.http4s.circe._
import io.circe.syntax._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext.Implicits.global

object BenTwitterServer extends StreamApp[IO] with Http4sDsl[IO] {

  val xa = Transactor.fromDriverManager[IO](
    "org.h2.Driver", "jdbc:h2:mem:test", "test", ""
  )

  case class User(handle: String, password: String)
  case class Post(id: Int, content: String, date: Date)
  case class UserLikes(userId: Int, postId: Int)

  implicit val userDecoder: Decoder[User]

  def getPosts(user: User): ConnectionIO[Option[Post]] =
    sql"SELECT * FROM Posts".query[Post].option

  def createPost(user: User)(content: String): ConnectionIO[Post] = {
    val localTime = LocalDate.now(ZoneId.of("America/Montreal"))
    sql"INSERT INTO Posts (content, date) VALUES ($content, $localTime)".query[Post].unique
  }

  val service = HttpService[IO] {
    case req @ POST -> Root / "login" =>
      for {
        user <- req.as[User]
        auth <- Auth.login(user.handle, user.password)
        context <- auth match {
          case Authorized(user) => Ok(Json.obj("user" -> user.handle.asJson))
          case Unauthorized => Unauthorized()
        }
      } yield context
  }

  def stream(args: List[String], requestShutdown: IO[Unit]) =
    BlazeBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .mountService(service, "/")
      .serve
}
