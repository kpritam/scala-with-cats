import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.6-SNAP6"

  object Cats {
    private val Version  = "1.6.0"
    lazy val `cats-core` = "org.typelevel" %% "cats-core" % Version
  }
}
