# Safe sliding

```scala
scala> val l = List.range(0, 5)
l: List[Int] = List(0, 1, 2, 3, 4)

scala> l.sliding(0)
java.lang.IllegalArgumentException: requirement failed: size=0 and step=1, but both must be positive
  at scala.collection.Iterator$GroupedIterator.<init>(Iterator.scala:889)
  at scala.collection.Iterator$class.sliding(Iterator.scala:1048)
  at scala.collection.AbstractIterator.sliding(Iterator.scala:1202)
  at scala.collection.IterableLike$class.sliding(IterableLike.scala:204)
  at scala.collection.AbstractIterable.sliding(Iterable.scala:54)
  at scala.collection.IterableLike$class.sliding(IterableLike.scala:190)
  at scala.collection.AbstractIterable.sliding(Iterable.scala:54)
  ... 136 elided
```

```scala
scala> import eu.timepit.refined.numeric._
import eu.timepit.refined.numeric._

scala> import shapeless.tag.@@
import shapeless.tag.$at$at

scala> def sliding[A](l: List[A], n: Int @@ Positive): List[List[A]] =
     |   l.sliding(n).toList
sliding: [A](l: List[A], n: shapeless.tag.@@[Int,eu.timepit.refined.numeric.Positive])List[List[A]]
```

```scala
scala> import eu.timepit.refined._
import eu.timepit.refined._

scala> sliding(l, refineLit(2))
res1: List[List[Int]] = List(List(0, 1), List(1, 2), List(2, 3), List(3, 4))
```

```scala
scala> sliding(l, refineLit(0))
<console>:18: error: could not find implicit value for parameter p: eu.timepit.refined.Predicate[P,Int]
              sliding(l, refineLit(0))
                                  ^

scala> sliding(l, refineLit[Positive](0))
<console>:18: error: Predicate failed: (0 > 0).
              sliding(l, refineLit[Positive](0))
                                            ^
```
