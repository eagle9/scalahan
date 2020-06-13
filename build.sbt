name := "scala3"

version := "0.1"



organization := "com.yourorg"



scalaVersion := "2.11.12"

scalacOptions ++= Seq("-unchecked", "-deprecation")

resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.30",
  "com.typesafe.akka" %% "akka-slf4j" % "2.5.30",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.30"
)
// https://mvnrepository.com/artifact/com.typesafe.akka/akka-slf4j
//libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.5.30"