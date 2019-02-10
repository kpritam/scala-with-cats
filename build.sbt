import Dependencies._

ThisBuild / scalaVersion     := "2.13.0-M5"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "scala-with-cats",
    libraryDependencies += Cats.`cats-core`,
    libraryDependencies += scalaTest % Test
  )
