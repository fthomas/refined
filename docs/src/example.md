# Safe sliding

```tut:nofail
val l = List.range(0, 5)
l.sliding(0)
```

```tut
import eu.timepit.refined.numeric.Positive
import shapeless.tag.@@

def sliding[A](l: List[A], n: Int @@ Positive): List[List[A]] =
  l.sliding(n).toList
```

```tut
import eu.timepit.refined._

sliding(l, refineLit(2))
```

```tut:nofail
sliding(l, refineLit(0))

sliding(l, refineLit[Positive, Int](0))
```
