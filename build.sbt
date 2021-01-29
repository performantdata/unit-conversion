name := "unit-conversion"
version := "0.1.0"
scalaVersion := "2.13.4"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked",
)

val circeVersion = "0.12.3"
val http4sVersion = "0.21.16"

libraryDependencies ++= Seq(
  "com.beachape" %% "enumeratum" % "1.6.1",
  "com.lihaoyi" %% "fastparse" % "2.3.0",
  "io.circe" %% "circe-core"    % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser"  % circeVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe"        % http4sVersion,
  "org.http4s" %% "http4s-dsl"          % http4sVersion,
  "org.typelevel" %% "cats-effect" % "2.3.1",

  "org.scalatest" %% "scalatest-freespec" % "3.2.2" % "test",
)

enablePlugins(JavaAppPackaging, DockerPlugin)
dockerBaseImage := "circleci/openjdk:11"
dockerExposedPorts := Seq(8080)
dockerUpdateLatest := true
