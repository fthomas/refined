# Using type aliases for refined types

```scala
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.api.RefType.applyRef
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Interval
```

```scala
scala> type File = Char Refined Interval.Closed[W.`'a'`.T, W.`'h'`.T]
defined type alias File

scala> type Rank = Int Refined Interval.Closed[W.`1`.T, W.`8`.T]
defined type alias Rank

scala> case class Square(file: File, rank: Rank)
defined class Square
```

```scala
scala> Square('a', 1)
res0: Square = Square(Refined(a),Refined(1))

scala> Square('e', 4)
res1: Square = Square(Refined(e),Refined(4))
```

```scala
scala> Square('i', 2)
<console>:23: error: Right predicate of (!(i < a) && !(i > h)) failed: Predicate (i > h) did not fail.
       Square('i', 2)
              ^

scala> Square('a', 9)
<console>:23: error: Right predicate of (!(9 < 1) && !(9 > 8)) failed: Predicate (9 > 8) did not fail.
       Square('a', 9)
                   ^

scala> Square('k', -1)
<console>:23: error: Right predicate of (!(k < a) && !(k > h)) failed: Predicate (k > h) did not fail.
       Square('k', -1)
              ^
<console>:23: error: Left predicate of (!(-1 < 1) && !(-1 > 8)) failed: Predicate (-1 < 1) did not fail.
       Square('k', -1)
                    ^
```

```scala
scala> val a1 = Square('a', 1)
a1: Square = Square(Refined(a),Refined(1))

scala> val b2 = for {
     |   f <- applyRef[File]((a1.file + 1).toChar).right
     |   r <- applyRef[Rank](a1.rank + 1).right
     | } yield Square(f, r)
b2: scala.util.Either[String,Square] = Right(Square(Refined(b),Refined(2)))

scala> val i9 = for {
     |   f <- applyRef[File]((a1.file + 8).toChar).right
     |   r <- applyRef[Rank](a1.rank + 8).right
     | } yield Square(f, r)
i9: scala.util.Either[String,Square] = Left(Right predicate of (!(i < a) && !(i > h)) failed: Predicate (i > h) did not fail.)
```
