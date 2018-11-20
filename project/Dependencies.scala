import sbt._

object Dependencies {
  val spark = "org.apache.spark" %% "spark-core" % "2.4.0"
  val log4j = "org.apache.logging.log4j" % "log4j-core" % "2.11.1"

  val libs = Seq(spark, log4j)
}

