package com.github.ylobazov.spark

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.log4j._
import org.apache.spark.rdd.RDD
import util._

object PurchaseByCustomer {

  implicit val logger: Logger = Logger.getLogger(this.getClass)

  def extractCustomerPricePairs(line: String): (Long, Double) = {
    val fields = line.split(",")
    val customerId = fields(0).toLong
    val amount = fields(2).toDouble
    (customerId, amount)
  }

  def countPurchaseByCustomer(rdd: RDD[String]): Array[(Double, Long)] = {
    rdd.map(extractCustomerPricePairs)
      .reduceByKey((x, y) => x + y)
      .map(x => (x._2, x._1))
      .sortByKey(ascending = false)
      .collect()
  }

  def printStats(entry: (Double, Long)): Unit = {
    val customerId = entry._2
    val amount = entry._1
    println(f"$customerId: $amount%.2f USD")
  }

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)

    val conf = new SparkConf().setMaster("local[*]").setAppName("TotalSpentByCustomer").set("spark.driver.host", "localhost")
    val sc = new SparkContext(conf)

    val lines = sc.textFile("../customer-orders.csv")

    measure {
      val result = countPurchaseByCustomer(lines)
      result.foreach(printStats)
    }
  }

}
