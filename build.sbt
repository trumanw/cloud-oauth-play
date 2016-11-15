name := """cloud-auth"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

// Scala OAuth2 Provider
libraryDependencies ++= Seq(
  "com.nulab-inc" %% "play2-oauth2-provider" % "1.0.0"
)

// Database dependencies
libraryDependencies ++= Seq(
  jdbc,
  evolutions,
  "org.postgresql" % "postgresql" % "9.4.1211.jre7",
  "org.skinny-framework" % "skinny-orm_2.11" % "2.3.0-RC1",
  "org.scalikejdbc" % "scalikejdbc-play-dbapi-adapter_2.11" % "2.5.1"
)
