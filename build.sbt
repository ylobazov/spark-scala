name := "spark-scala"

version := "0.1"

scalaVersion := "2.11.8"

resolvers in Global ++= Seq(
  "MVNRepository" at "https://mvnrepository.com/artifact"
)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.0" % "provided",
  "org.apache.spark" %% "spark-sql" % "2.4.0" % "provided",
  "org.apache.spark" %% "spark-mllib" % "2.4.0" % "provided",

  "org.apache.logging.log4j" % "log4j-core" % "2.11.1"
)

// set the main class for packaging the main jar
mainClass in assembly := Some("com.github.ylobazov.spark.MovieSimilarities")
assemblyOutputPath in assembly := file("../compiledJars/spark-scala.jar")