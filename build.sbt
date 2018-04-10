val Http4sVersion = "0.18.7"
val Specs2Version = "4.0.3"
val LogbackVersion = "1.2.3"
val DoobieVersion = "0.5.2"
val circeVersion = "0.9.3"

lazy val root = (project in file("."))
  .settings(
    organization := "org.ben",
    name := "ben-twitter",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.5",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "org.specs2"     %% "specs2-core"          % Specs2Version % "test",
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,

      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full,

      // Start with this one
      "org.tpolecat" %% "doobie-core"      % DoobieVersion,

      // And add any of these as needed
      "org.tpolecat" %% "doobie-h2"        % DoobieVersion, // H2 driver 1.4.197 + type mappings.
      "org.tpolecat" %% "doobie-hikari"    % DoobieVersion, // HikariCP transactor.
      "org.tpolecat" %% "doobie-postgres"  % DoobieVersion, // Postgres driver 42.2.2 + type mappings.
      "org.tpolecat" %% "doobie-specs2"    % DoobieVersion, // Specs2 support for typechecking statements.
      "org.tpolecat" %% "doobie-scalatest" % DoobieVersion  // ScalaTest support for typechecking statements.
      )
  )
