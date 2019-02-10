package exercises.case_study.crdt

import scala.language.higherKinds

trait KVStore[F[_, _], K, V] {
  def put(f: F[K, V])(k: K, v: V): F[K, V]
  def get(f: F[K, V])(k: K): Option[V]
  def getOrElse(f: F[K, V])(k: K, v: V): V
  def values(f: F[K, V]): List[V]
}

object KVStore {
  def mapInstance[K, V]: KVStore[Map, K, V] = new KVStore[Map, K, V] {
    override def put(map: Map[K, V])(k: K, v: V): Map[K, V] = map + (k → v)
    override def get(map: Map[K, V])(k: K): Option[V]       = map.get(k)
    override def getOrElse(map: Map[K, V])(k: K, v: V): V   = map.getOrElse(k, v)
    override def values(map: Map[K, V]): List[V]            = map.values.toList
  }
}

object KVStoreOps {
  implicit class KvsOps[F[_, _], K, V](f: F[K, V]) {
    def put(key: K, value: V)(implicit kvs: KVStore[F, K, V]): F[K, V] =
      kvs.put(f)(key, value)

    def get(key: K)(implicit kvs: KVStore[F, K, V]): Option[V] =
      kvs.get(f)(key)

    def getOrElse(key: K, default: V)(implicit kvs: KVStore[F, K, V]): V =
      kvs.getOrElse(f)(key, default)

    def values(implicit kvs: KVStore[F, K, V]): List[V] =
      kvs.values(f)
  }
}
