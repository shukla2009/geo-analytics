package com.avalia.analytics.client

import akka.actor.{Actor, ActorLogging, Props}
import com.avalia.analytics.core.Message.FindMeeting
import com.avalia.analytics.core.{CassandraSparkAndScalaSolution, OnlyScalaSolution, SparkAndScalaSolution}
import com.typesafe.config.ConfigFactory

/**
  * Created by rahul on 4/7/17.
  */

case class Find(uid1: String, uid2: String, version: String = "S")

/**
  * Client to call the solution and print results
  */
class ClientActor extends Actor with ActorLogging {
  val s = context.actorOf(Props[OnlyScalaSolution], "S")
  log.info(s"Starting Scala Solution on ${s.path}")
  val ss = context.actorOf(Props[SparkAndScalaSolution], "SS")
  log.info(s"Starting Spark Scala Solution on ${ss.path}")
  if (ConfigFactory.load().getString("cassandra.pass").nonEmpty) {
    val css = context.actorOf(Props[CassandraSparkAndScalaSolution], "CSS")
    log.info(s"Starting Cassandra Spark Scala Solution on ${css.path}")
  }

  override def receive: Receive = {
    case Find(u1, u2, v) => context.actorSelection(s"$v") ! FindMeeting(u1, u2)
    case s: String => println(s)
  }
}
