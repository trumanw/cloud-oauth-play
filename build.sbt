name := """cloud-auth"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

// Scala OAuth2 Provider
libraryDependencies ++= Seq(
  "com.nulab-inc" %% "play2-oauth2-provider" % "1.0.0"
)
