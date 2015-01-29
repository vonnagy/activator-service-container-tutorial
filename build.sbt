import sbt._
import Keys._

name := "activator-service-container-tutorial"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies ++= {

	val containerVersion 	= "1.0.0-SNAPSHOT"
  val configVersion     = "1.2.1"
  val akkaVersion  			= "2.3.8"
  val liftVersion				= "2.5.1"
  val sprayVersion 			= "1.3.1"

  Seq(
  	"com.github.vonnagy"	%%  "service-container" % containerVersion,
    "com.typesafe" 	      %   "config"				    % configVersion,
    "com.typesafe.akka" 	%%  "akka-actor"				% akkaVersion exclude ("org.scala-lang" , "scala-library"),
    "com.typesafe.akka" 	%%  "akka-slf4j"				% akkaVersion exclude ("org.slf4j", "slf4j-api") exclude ("org.scala-lang" , "scala-library"),
    "ch.qos.logback" 			%   "logback-classic"		% "1.0.13",
    "io.spray" 						%   "spray-can"					% sprayVersion,
    "io.spray" 						%   "spray-routing"			% sprayVersion,
    "net.liftweb" 				%%  "lift-json"					% liftVersion,

    "org.specs2" 					%%  "specs2-core" 			% "2.4.15"      % "test",
    "io.spray" 						%   "spray-testkit" 		% sprayVersion  % "test",
    "com.typesafe.akka" 	%%  "akka-testkit" 			% akkaVersion   % "test",
    "org.scalamock" 			%%  "scalamock-specs2-support" % "3.2.1" % "test" exclude("org.specs2", "specs2")
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
