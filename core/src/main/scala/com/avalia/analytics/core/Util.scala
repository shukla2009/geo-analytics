package com.avalia.analytics.core

import java.io.{BufferedWriter, File, FileWriter}

/**
  * Utilty to find meetings between two point
  * Created by Rahul Shukla <shukla2009@gmail.com> on 3/7/17.
  */

object Util {


  /**
    * Calculate distance between two coordinates
    *
    * @param r1 First Record / Coordinate
    * @param r2 Second Record / Coordinate
    * @return Distance between r1 and r2
    */
  private def getDistance(r1: Record, r2: Record): Double = {
    math.sqrt((r2.x - r1.x) * (r2.x - r1.x) + (r2.y - r1.y) * (r2.y - r1.y))
  }

  /**
    * Find whether given two records should be considered as meet
    *
    * @param r1 First Record / Coordinate
    * @param r2 Second Record / Coordinate
    * @return Boolean true if it is a meet
    */
  private def whetherMeet(r1: Record, r2: Record): Boolean = {
    if (r1 == null || r2 == null || r1.floor != r2.floor)
      false
    else
      getDistance(r1, r2) < 1
  }

  /**
    * Find all meets between two users
    *
    * @param u1        User 1 Records
    * @param u2        User 2 Records
    * @param lastLocU1 Last Location of user 1
    * @param lastLocU2 Last location of User 2
    * @param points    accumulator to collect meeting points
    * @return meeting point between given users
    */
  private def getMeetings(u1: List[Record], u2: List[Record], lastLocU1: Record, lastLocU2: Record,
                          points: List[(Record, Record)]): List[(Record, Record)] = {

    if (u1.isEmpty || u2.isEmpty) {
      if (whetherMeet(lastLocU1, lastLocU2)) {
        points :+ (lastLocU1, lastLocU2)
      } else {
        points
      }
    } else if (lastLocU1 == null || lastLocU2 == null)
      getMeetings(u1.tail, u2.tail, u1.head, u2.head, points)
    else if (u1.head.timestamp.before(lastLocU2.timestamp)) {
      getMeetings(u1.tail, u2, u1.head, lastLocU2, points)
    } else if (u2.head.timestamp.before(lastLocU1.timestamp)) {
      getMeetings(u1, u2.tail, lastLocU1, u2.head, points)
    }
    else if (whetherMeet(lastLocU1, lastLocU2))
      if (u1.head.timestamp.after(u2.head.timestamp)) {
        getMeetings(u1, u2.tail, lastLocU1, u2.head, points :+ (lastLocU1, lastLocU2))
      } else {
        getMeetings(u1.tail, u2, u1.head, lastLocU2, points :+ (lastLocU1, lastLocU2))
      }
    else {
      if (u1.head.timestamp.after(u2.head.timestamp)) {
        getMeetings(u1, u2.tail, lastLocU1, u2.head, points)
      } else {
        getMeetings(u1.tail, u2, u1.head, lastLocU2, points)
      }
    }
  }

  /**
    * find meetings between two users
    *
    * @param user1Records User 1 Records
    * @param user2Records User 2 Records
    * @return Meetings points
    */
  def findMeeting(user1Records: List[Record], user2Records: List[Record]): List[(Record, Record)] = {
    getMeetings(user1Records, user2Records, null, null, List.empty[(Record, Record)])
  }

  /**
    * Format Result in readable format
    *
    * @param uid1   User one id
    * @param uid2   User two Id
    * @param points Meeting Points
    * @param start  Start time of api
    * @param end    End time of api
    * @return Readable output
    */
  def formatResult(uid1: String, uid2: String, points: List[(Record, Record)], start: Long, end: Long): String = {
    s"UID : ($uid1,$uid2)\n" +
      s"Time Consumed : ${end - start}ms\n" +
      s"Number of Meets : ${points.length}\n" +
      s"Metting Points\n${points.map(p => s"${p._1.toString}\n${p._2.toString}").mkString("\n\n")}\n"
  }
}