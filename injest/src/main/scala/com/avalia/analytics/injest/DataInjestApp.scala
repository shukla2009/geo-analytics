package com.avalia.analytics.injest

import com.datastax.spark.connector.cql.CassandraConnector
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.SparkSession

/**
  * Created by rahul on 30/6/17.
  */

/**
  * Injest Data To Cassandra
  */
object DataInjestApp extends App {

  private val config = ConfigFactory.load()
  private val port = config.getInt("cassandra.port")
  private val clazz = config.getString("cassandra.clazz")
  private val repFact = config.getString("cassandra.repFact")
  private val durableWrites = config.getString("cassandra.durableWrites")
  private val host = config.getString("cassandra.host")
  private val keyspace = config.getString("cassandra.keyspace")
  private val table = config.getString("cassandra.table")
  private val user = config.getString("cassandra.user")
  private val pass = config.getString("cassandra.pass")
  private val file = config.getString("avalia.data")

  val spark = SparkSession
    .builder()
    .appName("injestion")
    .master(s"local[*]")
    .config("spark.local.dir", "temp")
    .config("spark.cassandra.connection.host", host)
    .config("spark.cassandra.connection.port", port)
    .config("spark.cassandra.auth.username", user)
    .config("spark.cassandra.auth.password", pass)
    .config("spark.casandra.output.batch.size.rows", 10000)
    .getOrCreate()
  CassandraConnector(spark.sparkContext).withSessionDo { session =>
    val query =
      s"CREATE KEYSPACE IF NOT EXISTS $keyspace WITH replication = {'class': '${clazz}'," +
        s"'replication_factor': '${repFact}'}  AND durable_writes = ${durableWrites};"
    session.execute(query)
  }
  val data = spark.read
    .option("header", true)
    .option("inferSchema", true)
    .csv(file)
    .na.drop()
  DFHelper(spark, keyspace).saveDataFrame(data, "geo", Some(Seq("uid")), Some(Seq("timestamp", "floor")))
}