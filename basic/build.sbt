name := """silhouett-basic"""

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"
val silhouetteVersion = "5.0.0"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
  guice,
  "com.mohiva" %% "play-silhouette" % silhouetteVersion,
  "com.mohiva" %% "play-silhouette-password-bcrypt" % silhouetteVersion,
  "com.mohiva" %% "play-silhouette-persistence" % silhouetteVersion,
  "com.mohiva" %% "play-silhouette-crypto-jca" % silhouetteVersion,
  "net.codingwell" %% "scala-guice" % "4.1.0",
  "com.iheart" %% "ficus" % "1.4.3",
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
)

// Temporary fix for Eviction warning
// https://github.com/playframework/playframework/issues/7832#issuecomment-336014319
val akkaVersion = "2.5.6"
dependencyOverrides ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.google.guava" % "guava" % "22.0",
  "org.slf4j" % "slf4j-api" % "1.7.25"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "at.morec.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "at.morec.binders._"
