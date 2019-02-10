package exercises

import exercises.case_study.crdt.{GCounter, KVStore}
import org.scalatest.{FunSuite, Matchers}
import cats.implicits._
import exercises.case_study.crdt.GCounterOps.GCounterOps

class GCounterTest extends FunSuite with Matchers {

  test("GCounter increment") {
    implicit val store: KVStore[Map, String, Int] = KVStore.mapInstance[String, Int]

    val gc1 = GCounter[Map, String, Int].increment(Map("m1" → 5, "m2" → 6))("m1", 1)
    val gc2 = GCounter[Map, String, Int].increment(Map("m1" → 3, "m2" → 7))("m2", 1)

    val merged = gc1.merge(gc2)
    merged shouldBe Map("m1" → 9, "m2" → 14)
  }
}
