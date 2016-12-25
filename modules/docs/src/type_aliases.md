# Using type aliases for refined types

```tut:silent
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.api.RefType.applyRef
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Interval
```

```tut
type File = Char Refined Interval.Closed[W.`'a'`.T, W.`'h'`.T]
type Rank = Int Refined Interval.Closed[W.`1`.T, W.`8`.T]

case class Square(file: File, rank: Rank)
```

```tut
Square('a', 1)
Square('e', 4)
```

```tut:nofail
Square('i', 2)
Square('a', 9)
Square('k', -1)
```

```tut
val a1 = Square('a', 1)

val b2 = for {
  f <- applyRef[File]((a1.file + 1).toChar).right
  r <- applyRef[Rank](a1.rank + 1).right
} yield Square(f, r)

val i9 = for {
  f <- applyRef[File]((a1.file + 8).toChar).right
  r <- applyRef[Rank](a1.rank + 8).right
} yield Square(f, r)
```
