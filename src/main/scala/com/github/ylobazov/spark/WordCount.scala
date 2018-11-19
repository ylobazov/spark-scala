package com.github.ylobazov.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._

import util._

/** Count up how many of each word occurs in a book, using regular expressions and sorting the final results */
object WordCount {

  /** Our main function where the action happens */
  def main(args: Array[String]): Unit = {

    // Set the log level to only print errors
    Logger.getLogger("org").setLevel(Level.ERROR)
    implicit val logger: Logger = Logger.getLogger(this.getClass)

    // Create a SparkContext using the local machine
    val conf = new SparkConf().setMaster("local[*]").setAppName("WordCount").set("spark.driver.host", "localhost")
    val sc = new SparkContext(conf)

    // Load each line of my book into an RDD
    val input = sc.textFile("../book.txt")

    measure {
      // Split using a regular expression that extracts words
      val words = input.flatMap(x => x.split("[\\d\\W]+"))

      // Normalize everything to lowercase
      val lowercaseWords = words.map(x => x.toLowerCase())

      //Filter black-listed words
      val filteredWords = lowercaseWords.filter(!BlackList.contains(_))

      // Count of the occurrences of each word
      val wordCounts = filteredWords.map(x => (x, 1)).reduceByKey((x, y) => x + y)

      // Flip (word, count) tuples to (count, word) and then sort by key (the counts)
      val wordCountsSorted = wordCounts.map(x => (x._2, x._1)).sortByKey(ascending = false).collect()

      // Print the results, flipping the (count, word) results to word: count as we go.
      for (result <- wordCountsSorted) {
        val count = result._1
        val word = result._2
        println(s"$word: $count")
      }
    }

  }

  val BlackList = List("a", "an", "are", "of", "in", "is", "on", "the", "to")

}


