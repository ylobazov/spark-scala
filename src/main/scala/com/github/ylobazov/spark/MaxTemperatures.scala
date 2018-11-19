package com.github.ylobazov.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._
import scala.math.max

/** Find the maximum temperature by weather station */
object MaxTemperatures {

  def parseLine(line:String)= {
    val fields = line.split(",")
    val stationID = fields(0)
    val entryType = fields(2)
    val temperature = (fields(3).toDouble * 0.1).toFahrenheit
    (stationID, entryType, temperature)
  }
  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkContext using every core of the local machine
    val conf = new SparkConf().setMaster("local[*]").setAppName("MaxTemperatures").set("spark.driver.host", "localhost")
    val sc = new SparkContext(conf)

    // Read each line of input data
    val lines = sc.textFile("../1800.csv")

    // Convert to (stationID, entryType, temperature) tuples
    val parsedLines = lines.map(parseLine)

    // Filter out all but TMAX entries
    val maxTemps = parsedLines.filter(x => x._2 == "TMAX")

    // Convert to (stationID, temperature)
    val stationTemps = maxTemps.map(x => (x._1, x._3.toFloat))

    // Reduce by stationID retaining the maximum temperature found
    val maxTempsByStation = stationTemps.reduceByKey( (x,y) => max(x,y))

    // Collect, format, and print the results
    val results = maxTempsByStation.collect()

    for (result <- results.sorted) {
      val station = result._1
      val temp = result._2
      val formattedTemp = f"$temp%.2f F"
      println(s"$station maximum temperature: $formattedTemp")
    }

  }

  implicit class FahrenheitConverter(celsius: Double){
    def toFahrenheit: Double = celsius * (9.0f / 5.0) + 32.0
  }
}