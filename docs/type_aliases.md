# Using type aliases for refined types

```scala
import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import shapeless.nat._
```

```scala
scala> type File = Char Refined Interval[W.`'a'`.T, W.`'h'`.T]
defined type alias File

scala> type Rank = Int Refined Interval[_1, _8]
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
<console>:28: error: Right predicate of (!(i < a) && !(i > h)) failed: Predicate (i > h) did not fail.
       Square('i', 2)
              ^

scala> Square('a', 9)
<console>:28: error: Right predicate of (!(9 < 1) && !(9 > 8)) failed: Predicate (9 > 8) did not fail.
       Square('a', 9)
                   ^

scala> Square('k', -1)
<console>:28: error: Right predicate of (!(k < a) && !(k > h)) failed: Predicate (k > h) did not fail.
       Square('k', -1)
              ^
<console>:28: error: Left predicate of (!(-1 < 1) && !(-1 > 8)) failed: Predicate (-1 < 1) did not fail.
       Square('k', -1)
                    ^
```
