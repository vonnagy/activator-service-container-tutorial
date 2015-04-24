import sbt._
import Keys._

name := "activator-service-container-tutorial"

version := "1.0.1"

scalaVersion := "2.11.6"
crossScalaVersions := Seq("2.10.5", "2.11.6")

resolvers += "Scalaz Bintray Repo" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies ++= {

	val containerVersion 	= "1.0.1"
  val configVersion     = "1.2.1"
  val akkaVersion  			= "2.3.9"
  val liftVersion				= "2.6.2"
  val sprayVersion 			= "1.3.3"

  Seq(
  	"com.github.vonnagy"	%%  "service-container" % containerVersion,
    "com.github.vonnagy"	%%  "service-container-metrics-reporting" % containerVersion,
    "com.typesafe" 	      %   "config"				    % configVersion,
    "com.typesafe.akka" 	%%  "akka-actor"				% akkaVersion exclude ("org.scala-lang" , "scala-library"),
    "com.typesafe.akka" 	%%  "akka-slf4j"				% akkaVersion exclude ("org.slf4j", "slf4j-api") exclude ("org.scala-lang" , "scala-library"),
    "ch.qos.logback" 			%   "logback-classic"		% "1.1.3",
    "io.spray" 						%%  "spray-can"					% sprayVersion,
    "io.spray" 						%%  "spray-routing"			% sprayVersion,
    "net.liftweb" 				%%  "lift-json"					% liftVersion,

    "com.typesafe.akka" 	%%  "akka-testkit" 			% akkaVersion   % "test",
    "io.spray"            %%  "spray-testkit"     % sprayVersion  % "test",
    "junit"               %   "junit"             % "4.12"        % "test",
    "org.scalaz.stream"   %%  "scalaz-stream"     % "0.7a"        % "test",
    "org.specs2"          %%  "specs2-core"       % "3.5"         % "test",
    "org.specs2"          %%  "specs2-mock"       % "3.5"         % "test"
  )
}

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-Xlint",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.7",
  "-encoding", "UTF-8"
)

crossPaths := false

parallelExecution in Test := false
