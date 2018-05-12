package com.avalia.analytics.core

/**
  * Created by rahul on 12/5/18.
  */

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.avalia.analytics.core.Message.FindMeetingRaw
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.concurrent.duration._

class OnlyScalaSolutionSpec() extends TestKit(ActorSystem("core")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An Onlyscala solution actor" must {

    "send back meeting records " in {
      val s = system.actorOf(Props[OnlyScalaSolution])
      s ! FindMeetingRaw("26c56675", "c9d2e553")
      expectMsg(10 seconds, List(
        (Record("26c56675", 1, "2014-07-20T08:15:21.228Z", 104.18302, 90.02981825268105), Record("c9d2e553", 1, "2014-07-20T08:15:23.006Z", 104.19539, 90.59059924032633)),
        (Record("26c56675", 1, "2014-07-20T08:15:41.004Z", 104.23871, 90.83547255589068), Record("c9d2e553", 1, "2014-07-20T08:15:33.122Z", 104.069176, 90.38909348194139))))
    }
  }
}
