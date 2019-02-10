package exercises

import cats.implicits._
import exercises.case_study.crdt.{GCounter, KVStore}
import org.scalatest.{FunSuite, Matchers}

class GCounterTest extends FunSuite with Matchers {

  test("GCounter") {
    implicit val store: KVStore[Map, String, Int] = KVStore.mapInstance[String, Int]

    val g1      = Map("m1" → 5, "m2" → 0)
    val g2      = Map("m1" → 0, "m2" → 7)
    val counter = GCounter[Map, String, Int]

    val merged = counter.merge(g1, g2)
    merged shouldBe Map("m1" → 5, "m2" → 7)

    val g3 = counter.increment(g1)("m1", 2)
    val g4 = counter.increment(g2)("m2", 3)

    val merged1 = counter.merge(g3, g4)
    merged1 shouldBe Map("m1" → 7, "m2" → 10)

    counter.total(merged1) shouldBe 17
  }
}
