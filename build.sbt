name := "content-type-resolver-notes"
version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.5"
val akkaVersion     = "2.5.11"
val akkaHttpVersion = "10.0.11"

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
  "org.apache.tika"   % "tika-core"               % "1.17",
  "com.typesafe.akka" %% "akka-actor"             % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"            % akkaVersion,
  "com.typesafe.akka" %% "akka-http-core"         % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http"              % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json"   % akkaHttpVersion,
  "com.typesafe.play" %% "play-ahc-ws-standalone" % "1.1.6",
  "org.scalatest"     %% "scalatest"              % "3.0.5" % Test,
  "com.typesafe.akka" %% "akka-stream-testkit"    % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-testkit"           % akkaVersion % Test
)
