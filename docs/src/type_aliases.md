# Using type aliases for refined types

```tut:silent
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import shapeless.nat._
```

```tut
type File = Char Refined Interval[W.`'a'`.T, W.`'h'`.T]
type Rank = Int Refined Interval[_1, _8]

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
