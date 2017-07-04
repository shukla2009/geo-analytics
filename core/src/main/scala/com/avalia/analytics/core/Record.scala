package com.avalia.analytics.core

import java.sql.Timestamp

import com.datastax.driver.core.Row

/**
  * Created by rahul on 3/7/17.
  */
case class Record(uid: String, floor: Int, timestamp: Timestamp, x: Double, y: Double) {
  override def toString: String = {
    s"uid:$uid|floor:$floor|ts:$timestamp|x:$x|y:$y"
  }
}


object Record {
  def apply(row: Row): Record = {
    val timestamp: Timestamp = new Timestamp(row.getTimestamp("timestamp").getTime)
    new Record(row.getString("uid"), row.getInt("floor"), timestamp, row.getDouble("x"), row.getDouble("y"))
  }
}
