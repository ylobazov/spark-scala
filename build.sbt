name := "spark-scala"

version := "0.1"

scalaVersion := "2.12.7"

resolvers in Global ++= Seq(
  "MVNRepository"  at "https://mvnrepository.com/artifact",
)

libraryDependencies                ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.0",
  "org.apache.logging.log4j" % "log4j-core" % "2.11.1"

)