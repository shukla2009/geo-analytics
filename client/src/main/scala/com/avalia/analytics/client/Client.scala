package com.avalia.analytics.client

import akka.actor.{ActorSystem, Props}

import scala.io.StdIn

/**
  * Created by rahul on 4/7/17.
  */
object Client extends App {
  private val core = ActorSystem("core")

  private val client = core.actorOf(Props[ClientActor])

  println("###################### Find Command ######################\n")
  println("      FIND userId1 userId2 SolutionChoice[S|SS|CSS]")
  println("EX => FIND  26c56675 c9d2e553 S \n\n")
  println("Type exit to stop")
  var input = StdIn.readLine()
  while (!"exit".equals(input)) {
    input.trim.split(" ").toList match {
      case "FIND" :: uid1 :: uid2 :: sc :: Nil => client ! Find(uid1, uid2, sc)
      case "FIND" :: uid1 :: uid2 :: Nil => client ! Find(uid1, uid2)
      case _ => println("Not Supported")
    }
    input = StdIn.readLine()
  }

  // ("26c56675", "c9d2e553"), ("c9d2e553", "b378b411")
  //client ! Find("26c56675", "c9d2e553", "S")
  //client ! Find("c9d2e553", "b378b411", "SS")
}
