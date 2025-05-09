val scala3Version = "3.6.4"

lazy val root = project
  .in(file("."))
  .enablePlugins(CoverallsPlugin)
  .settings(
    name := "Hearts",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,


    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.14",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.14" % "test",

    coverageEnabled := true
  )
