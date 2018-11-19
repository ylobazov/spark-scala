package com.github.ylobazov.spark

import java.time.Clock

import org.apache.log4j.Logger

object util {
  def measure(func: => Unit)(implicit logger: Logger): Unit = {
    val clock = Clock.systemUTC()
    val start = clock.millis()
    func
    logger.info(s"TIME CONSUMED: ${clock.millis() - start} ms")
  }
}
