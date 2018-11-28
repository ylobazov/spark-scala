name := "spark-scala"

version := "0.1"

scalaVersion := "2.11.8"

resolvers in Global ++= Seq(
  "MVNRepository" at "https://mvnrepository.com/artifact"
)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.2.2" % "provided",
  "org.apache.spark" %% "spark-sql" % "2.2.2" % "provided",
  "org.apache.spark" %% "spark-mllib" % "2.2.2" % "provided",

  "org.apache.bahir" %% "spark-streaming-twitter" % "2.2.2",
  "org.twitter4j" % "twitter4j-core" % "3.0.x",
  "org.twitter4j" % "twitter4j-stream" % "3.0.x",

  "org.apache.logging.log4j" % "log4j-core" % "2.11.1"
)

// set the main class for packaging the main jar
mainClass in assembly := Some("com.github.ylobazov.spark.MovieSimilarities")
assemblyOutputPath in assembly := file("../compiledJars/spark-scala.jar")