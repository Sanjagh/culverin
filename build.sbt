
val akkaVersion = "2.4.16"

lazy val commonSettings = Seq(
  organization := "co.saverin",
  scalaVersion := "2.11.8"
)

lazy val core = project.
  settings(commonSettings: _*).
  settings(
    version := "0.0.1",
    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % akkaVersion
  ).enablePlugins(JavaAppPackaging)

lazy val agent = project.
  settings(commonSettings: _*).
  settings(
    version := "0.0.1",
    libraryDependencies ++= Seq(
            "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    )
  ).
  dependsOn(core).
  enablePlugins(JavaAppPackaging)

lazy val engine = project.
  settings(commonSettings: _*).
  settings(
    version := "0.0.1",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.5",
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    )
  ).
    dependsOn(core).
    enablePlugins(JavaAppPackaging)

lazy val root = (project in file(".")).
  aggregate(core, engine).
  settings(
    version := "0.0.1"
  )
