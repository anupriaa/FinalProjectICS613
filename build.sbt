import play.PlayJava

name := """FinalProjectICS613"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  jdbc,
  "org.postgresql" % "postgresql" % "9.3-1102-jdbc4",
  "org.mindrot" % "jbcrypt" % "0.3m",
  filters,
  "com.kennycason" % "kumo" % "1.1"
)