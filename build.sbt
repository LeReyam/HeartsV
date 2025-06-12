val scala3Version = "3.6.4"

lazy val root = project
  .in(file("."))
  .enablePlugins(CoverallsPlugin)
  .settings(
    name := "HeartsV",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,
    coverageEnabled := true,

    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.19",
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "org.scalafx" %% "scalafx" % "21.0.0-R32"
    ),

    Compile / run / fork := true,
    Compile / run / javaOptions ++= Seq(
      "--module-path", "C:/javafx-sdk-21.0.7/lib",
      "--add-modules", "javafx.controls,javafx.fxml"
    ),

    Compile / resourceDirectory := baseDirectory.value / "src/main/resources"
  )
