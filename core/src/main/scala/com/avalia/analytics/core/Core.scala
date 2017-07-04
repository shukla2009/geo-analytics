package com.avalia.analytics.core

import akka.actor.{ActorSystem, Props}
import com.avalia.analytics.core.Message.FindMeeting

object Core extends App {
  private val core = ActorSystem("core")

  private val onlyScalaSolution = core.actorOf(Props[SparkAndScalaSolution])
  // ("26c56675", "c9d2e553"), ("c9d2e553", "b378b411")
  onlyScalaSolution ! FindMeeting("26c56675", "c9d2e553")
  onlyScalaSolution ! FindMeeting("c9d2e553", "b378b411")

}
