package com.avalia.analytics.injest

import com.datastax.driver.core.exceptions.AlreadyExistsException
import org.apache.spark.sql.{SaveMode, _}
import com.datastax.spark.connector._
import org.apache.spark.sql.cassandra.CassandraFormat

/**
  */
case class DFHelper(spark: SparkSession, keyspace: String) {
  def getDataFrame(table: String): DataFrame = {
    spark.sqlContext.read
      .format("org.apache.spark.sql.cassandra")
      .options(Map("table" -> table, "keyspace" -> keyspace))
      .load()
  }

  def saveDataFrame(dataframe: DataFrame, tableName: String, partitionKeys: Option[Seq[String]] = None, clusterKeys: Option[Seq[String]] = None) = {

    try {
      dataframe.createCassandraTable(keyspace, tableName, partitionKeys, clusterKeys)
    } catch {
      case ex: AlreadyExistsException => None //Do Nothing
    }
    dataframe.write.format(CassandraFormat)
      .options(Map("table" -> tableName, "keyspace" -> keyspace))
      .mode(SaveMode.Append).save()
  }
}

