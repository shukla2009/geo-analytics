package com.avalia.analytics.core

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingReceive
import com.datastax.driver.core.{Cluster, Row, Session}
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConverters._
import org.apache.spark.sql.cassandra.DefaultSource

/**
  * Created by rahul on 3/7/17.
  */


class CassandraSparkAndScalaSolution extends Actor with ActorLogging {

  private val config = ConfigFactory.load()
  private val host = config.getString("cassandra.host")
  private val keyspace = config.getString("cassandra.keyspace")
  private val table = config.getString("cassandra.table")
  private val user = config.getString("cassandra.user")
  private val pass = config.getString("cassandra.pass")

  private val session: Session = Cluster.builder().addContactPoint(host)
    .withCredentials(user, pass).build().connect(keyspace)

  def getUserRecords(uid: String): List[Record] = {
    session.execute(s"SELECT * FROM $table WHERE uid='$uid'").all().asScala.toList.map(r => Record(r))
  }

  override def receive: Receive = LoggingReceive {
    case Message.FindMeeting(uid1, uid2) =>
      val user1Records: List[Record] = getUserRecords(uid1)
      val user2Records: List[Record] = getUserRecords(uid2)
      val start = System.currentTimeMillis()
      val meetings = Util.findMeeting(user1Records, user2Records)
      val end = System.currentTimeMillis()
      val result = Util.formatResult(uid1, uid2, meetings, start, end)
      log.info(result)
      sender() ! result
    case _ => //Do Nothing
  }
}
