import Dependencies._

name := "spark-scala"

version := "0.1"

scalaVersion := "2.11.8"

resolvers in Global ++= Seq(
  "MVNRepository" at "https://mvnrepository.com/artifact",
)

libraryDependencies ++= libs.map(_ % "provided")

// set the main class for packaging the main jar
mainClass in Compile := Some("com.github.ylobazov.spark.MovieSimilarities")
assemblyOutputPath in assembly := file("../compiledJars/spark-scala.jar")