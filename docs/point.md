```scala
scala> case class Point(x: Int, y: Int)
defined class Point
```

```scala
scala> import eu.timepit.refined._
import eu.timepit.refined._

scala> trait QuadrantI
defined trait QuadrantI

scala> implicit val quadrantIPredicate: Predicate[QuadrantI, Point] =
     |   Predicate.instance(p => p.x >= 0 && p.y >= 0, p => s"(${p.x} >= 0 && ${p.y} >= 0)")
quadrantIPredicate: eu.timepit.refined.Predicate[QuadrantI,Point] = eu.timepit.refined.Predicate$$anon$2@21c79f25
```
  
```scala
scala> refine[QuadrantI](Point(1, 3))
res0: Either[String,shapeless.tag.@@[Point,QuadrantI]] = Right(Point(1,3))

scala> refine[QuadrantI](Point(3, -2))  
res1: Either[String,shapeless.tag.@@[Point,QuadrantI]] = Left(Predicate failed: (3 >= 0 && -2 >= 0).)
```
