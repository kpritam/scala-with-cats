package exercises.case_study.crdt

import cats.instances.list._
import cats.kernel.CommutativeMonoid
import cats.syntax.foldable._
import cats.syntax.semigroup._
import exercises.case_study.crdt.KVStoreOps.KvsOps

import scala.language.higherKinds

trait GCounter[F[_, _], K, V] {
  def increment(f: F[K, V])(k: K, v: V)(implicit m: CommutativeMonoid[V]): F[K, V]
  def merge(f1: F[K, V], f2: F[K, V])(implicit b: BoundedSemiLattice[V]): F[K, V]
  def total(f: F[K, V])(implicit m: CommutativeMonoid[V]): V
}

object GCounter {
  def apply[F[_, _], K, V](implicit counter: GCounter[F, K, V]): GCounter[F, K, V] = counter

  implicit def gcounterInstance[F[_, _], K, V](implicit kvs: KVStore[F, K, V],
                                               km: CommutativeMonoid[F[K, V]]): GCounter[F, K, V] =
    new GCounter[F, K, V] {
      def increment(f: F[K, V])(key: K, value: V)(implicit m: CommutativeMonoid[V]): F[K, V] = {
        val total = f.getOrElse(key, m.empty) |+| value
        f.put(key, total)
      }

      def merge(f1: F[K, V], f2: F[K, V])(implicit b: BoundedSemiLattice[V]): F[K, V] = f1 |+| f2
      def total(f: F[K, V])(implicit m: CommutativeMonoid[V]): V                      = f.values.combineAll
    }
}

object GCounterOps {
  implicit class GCounterOps[F[_, _], K, V](f: F[K, V]) {
    def increment(key: K, value: V)(implicit gc: GCounter[F, K, V], m: CommutativeMonoid[V]): F[K, V] =
      gc.increment(f)(key, value)
    def merge(f2: F[K, V])(implicit gc: GCounter[F, K, V], b: BoundedSemiLattice[V]): F[K, V] = gc.merge(f, f2)
    def total(implicit gc: GCounter[F, K, V], m: CommutativeMonoid[V]): V                     = gc.total(f)
  }
}
