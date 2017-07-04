package com.avalia.analytics.core

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingReceive
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SparkSession

/**
  * Created by rahul on 3/7/17.
  */


class SparkAndScalaSolution extends Actor with ActorLogging {

  private val config = ConfigFactory.load()
  private val file = config.getString("avalia.data")


  private val spark = SparkSession
    .builder()
    .appName("injestion")
    .master(s"local[*]")
    .config("spark.local.dir", "temp")
    .getOrCreate()

  import spark.implicits._

  private val data = spark.read
    .option("header", true)
    .option("inferSchema", true)
    .csv(file)
    .na.drop().cache().createOrReplaceTempView("geo")

  override def receive: Receive = LoggingReceive {
    case Message.FindMeeting(uid1, uid2) =>
      val user1Records: List[Record] = spark.sql(s"select * from geo where uid = '$uid1'").as[Record].collect.toList
      val user2Records: List[Record] = spark.sql(s"select * from geo where uid = '$uid2'").as[Record].collect.toList
      val start = System.currentTimeMillis()
      val meetings = Util.findMeeting(user1Records, user2Records)
      val end = System.currentTimeMillis()
      val result = Util.formatResult(uid1, uid2, meetings, start, end)
      log.info(result)
      sender() ! result
    case _ => //Do Nothing
  }
}
