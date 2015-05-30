```tut
case class Point(x: Int, y: Int)
```

```tut
import eu.timepit.refined._

trait QuadrantI

implicit val quadrantIPredicate: Predicate[QuadrantI, Point] =
  Predicate.instance(p => p.x >= 0 && p.y >= 0, p => s"(${p.x} >= 0 && ${p.y} >= 0)")
```

```tut  
refine[QuadrantI](Point(1, 3))

refine[QuadrantI](Point(3, -2))
```
