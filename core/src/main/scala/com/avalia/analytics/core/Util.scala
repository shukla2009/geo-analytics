package com.avalia.analytics.core

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
    * @param u1     User 1 Records
    * @param u2     User 2 Records
    * @param points accumulator to collect meeting points
    * @return meeting point between given users
    */
  private def getMeetings(u1: List[Record], u2: List[Record], points: List[(Record, Record)]): List[(Record, Record)] = {
    (u1, u2) match {
      case (Nil, Nil) | (_, Nil) | (Nil, _) => points
      case (h1 :: t1, l@(h2 :: t2)) if t1.nonEmpty && t1.head.timestamp.before(h2.timestamp) => getMeetings(t1, l, points)
      case (l@(h1 :: t1), h2 :: t2) if t2.nonEmpty && t2.head.timestamp.before(h1.timestamp) => getMeetings(l, t2, points)
      case (l1@(h1 :: t1), l2@(h2 :: t2)) if whetherMeet(h1, h2) =>
        if (h1.timestamp.after(h2.timestamp)) getMeetings(l1, t2, points :+ (h1, h2)) else getMeetings(t1, l2, points :+ (h1, h2))
      case (l1@(h1 :: t1), l2@(h2 :: t2)) =>
        if (h1.timestamp.after(h2.timestamp)) getMeetings(l1, t2, points) else getMeetings(t1, l2, points)
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
    getMeetings(user1Records, user2Records, List.empty[(Record, Record)])
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
      s"Time Consumed : ${
        end - start
      }ms\n" +
      s"Number of Meets : ${
        points.length
      }\n" +
      s"Metting Points\n${
        points.map(p => s"${
          p._1.toString
        }\n${
          p._2.toString
        }").mkString("\n\n")
      }\n"
  }
}