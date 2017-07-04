import sbt._
import Keys._

object Dependencies {

  lazy val version = new {
    val scalaTest = "3.0.0"
    val scalaCheck = "1.13.4"
    val spark = "2.1.1"
    val akka = "2.5.3"
  }

  lazy val library = new {
    val test = "org.scalatest" %% "scalatest" % version.scalaTest % Test
    val check = "org.scalacheck" %% "scalacheck" % version.scalaCheck % Test
  }


  lazy val spark = new {
    lazy val core = "org.apache.spark" %% "spark-core" % version.spark
    lazy val sql = "org.apache.spark" %% "spark-sql" % version.spark
    lazy val cassandra = "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.2"

  }

  lazy val akka = new {
    lazy val actor = "com.typesafe.akka" %% "akka-actor" % version.akka
    lazy val cluster = "com.typesafe.akka" %% "akka-cluster" % version.akka
    lazy val clusterMetrics = "com.typesafe.akka" % "akka-cluster-metrics_2.11" % version.akka
    lazy val clusterTool = "com.typesafe.akka" %% "akka-cluster-tools" % version.akka
    lazy val testkit = "com.typesafe.akka" %% "akka-testkit" % version.akka % "test"

  }

  lazy val config = "com.typesafe" % "config" % "1.3.1"


  val injestDependencies: Seq[ModuleID] = Seq(
    library.test,
    spark.core,
    spark.sql,
    spark.cassandra,
    config,
    library.check
  )

  val coreDependencies: Seq[ModuleID] = Seq(
    library.test,
    spark.core,
    spark.sql,
    spark.cassandra,
    akka.actor,
    library.check
  )

  val clientDependencies: Seq[ModuleID] = Seq(
    library.test,
    spark.core,
    spark.sql,
    spark.cassandra,
    library.check
  )

}
