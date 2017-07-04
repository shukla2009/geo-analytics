package com.avalia.analytics.core

import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingReceive
import com.typesafe.config.ConfigFactory

import scala.io.Source

/**
  * Created by rahul on 3/7/17.
  */

class OnlyScalaSolution extends Actor with ActorLogging {

  private val config = ConfigFactory.load()
  private val file = config.getString("avalia.data")
  private val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

  ///timestamp,x,y,floor,uid
  //2014-07-19T16:00:06.074Z

  private val data: Map[String, List[Record]] = Source.fromFile(file).getLines().drop(1)
    .map(l => l.split(","))
    .map(arr =>
      Record(
        timestamp = Timestamp.valueOf(LocalDateTime.parse(arr(0), dtf)),
        x = arr(1).toDouble,
        y = arr(2).toDouble,
        floor = arr(3).toInt,
        uid = arr(4)))
    .toList
    .groupBy(_.uid)


  override def receive: Receive = LoggingReceive {
    case Message.FindMeeting(uid1, uid2) =>
      val start = System.currentTimeMillis()
      val meetings = Util.findMeeting(data.getOrElse(uid1, List.empty[Record]), data.getOrElse(uid2, List.empty[Record]))
      val end = System.currentTimeMillis()
      val result = Util.formatResult(uid1, uid2, meetings, start, end)
      log.info(result)
      sender() ! result
    case _ => //Do Nothing
  }
}
