package com.github.ylobazov.spark.sql

import com.github.ylobazov.spark.util.measure
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD

object FriendsByAge {

  /** Our main function where the action happens */
  def main(args: Array[String]) {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    implicit val logger: Logger = Logger.getLogger(this.getClass)

    // Create a SparkContext using every core of the local machine
    val conf = new SparkConf().setMaster("local[*]").setAppName("FriendsByAge").set("spark.driver.host", "localhost")
    val sc = new SparkContext(conf)

    // Load each line of the source data into an RDD
    val rdd = sc.textFile("../fakefriends.csv")

    measure {
      val avgFriendsByAge: Array[(Int, Int)] = friendsByAge(rdd)

      // Sort and print the final results.
      avgFriendsByAge.foreach(println)
    }

    measure {
      val avgFriendsByName = friendsByName(rdd)
      avgFriendsByName.sorted.foreach(println)
    }

  }

  /** A function that splits a line of input into (age, numFriends) tuples. */
  def extractAgeAndFriendsCount(line: String): (Int, Int) = {
    // Split by commas
    val fields = line.split(",")
    // Extract the age and numFriends fields, and convert to integers
    val age = fields(2).toInt
    val numFriends = fields(3).toInt
    // Create a tuple that is our result.
    (age, numFriends)
  }

  private def friendsByAge(src: RDD[String]) = {
    // Use our parseLines function to convert to (age, numFriends) tuples
    val rdd = src.map(extractAgeAndFriendsCount)

    // Lots going on here...
    // We are starting with an RDD of form (age, numFriends) where age is the KEY and numFriends is the VALUE
    // We use mapValues to convert each numFriends value to a tuple of (numFriends, 1)
    // Then we use reduceByKey to sum up the total numFriends and total instances for each age, by
    // adding together all the numFriends values and 1's respectively.
    val totalsByAge = rdd.mapValues(x => (x, 1)).reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))

    // So now we have tuples of (age, (totalFriends, totalInstances))
    // To compute the average we divide totalFriends / totalInstances for each age.
    val averagesByAge = totalsByAge.mapValues(x => x._1 / x._2)

    // Collect the results from the RDD (This kicks off computing the DAG and actually executes the job)
    averagesByAge.collect()
  }

  def friendsByName(src: RDD[String]): Array[(String, Int)] = {
    def extractNameAndFriendsCount(line: String): (String, Int) = {
      val fields = line.split(",")
      val name = fields(1)
      val numFriends = fields(3).toInt
      (name, numFriends)
    }

    val rdd = src.map(extractNameAndFriendsCount)

    val totalsByName = rdd.mapValues(x => (x, 1)).reduceByKey((x, y) => (x._1 + y._1, x._2 + y._2))
    val averageByName = totalsByName.mapValues(x => x._1 / x._2)
    averageByName.collect()
  }

}

