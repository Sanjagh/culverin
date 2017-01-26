
val akkaVersion = "2.4.16"

lazy val commonSettings = Seq(
  organization := "co.saverin",
  scalaVersion := "2.12.1"
)

lazy val core = project.
  settings(commonSettings: _*).
  settings(
    version := "0.0.1",
    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaVersion
  ).enablePlugins(JavaAppPackaging)

lazy val server = project.
  settings(commonSettings: _*).
  settings(
    version := "0.0.1"
  ).
  dependsOn(core).
  enablePlugins(JavaAppPackaging)

lazy val client = project.
  settings(commonSettings: _*).
  settings(
    version := "0.0.1"
  ).
  dependsOn(core).
  enablePlugins(JavaAppPackaging)

lazy val root = (project in file(".")).
    aggregate(core,server,client).
    settings(
      version := "0.0.1"
    )
