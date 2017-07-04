import Dependencies._
import Settings._

lazy val injest = (project in file("injest")).
  settings(Settings.settings: _*).
  settings(Settings.injestSettings: _*).
  settings(libraryDependencies ++= injestDependencies)

lazy val core = (project in file("core")).
  settings(Settings.settings: _*).
  settings(Settings.coreSettings: _*).
  settings(libraryDependencies ++= coreDependencies)

lazy val client = (project in file("client")).
  settings(Settings.settings: _*).
  settings(Settings.clientSettings: _*).
  dependsOn(core).
  configs(Test)
