name := "content-type-resolver-notes"
version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.5"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-encoding",
  "UTF-8",
  "-language:_",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-unused-import"
)

libraryDependencies ++= Seq(
  "org.apache.tika" % "tika-core"  % "1.17",
  "org.scalatest"   %% "scalatest" % "3.0.5" % Test
)
