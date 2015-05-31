# Custom predicate

The library comes with a lot [predefined predicates][provided-predicates]
but also allows to define your own. This example shows how to add a
predicate for a simple type representing a point in a two-dimensional
Cartesian coordinate system. We start by defining a `Point` class that
represents a point in our coordinate system:

```scala
scala> case class Point(x: Int, y: Int)
defined class Point
```

The axes of a Cartesian coordinate system divide the plane into four infinite
regions, called quadrants, which are often labelled with Roman numerals from I
to IV. Suppose we want to refine `Point`s with the quadrant they are lying in.
So let's create a simple type that represents the first quadrant:

```scala
scala> trait QuadrantI
defined trait QuadrantI
```

We now have type-level predicate and a type that we want to refine with this
predicate.

```scala
scala> import eu.timepit.refined._
import eu.timepit.refined._

scala> implicit val quadrantIPredicate: Predicate[QuadrantI, Point] =
     |   Predicate.instance(p => p.x >= 0 && p.y >= 0, p => s"(${p.x} >= 0 && ${p.y} >= 0)")
quadrantIPredicate: eu.timepit.refined.Predicate[QuadrantI,Point] = eu.timepit.refined.Predicate$$anon$2@93944f9
```

```scala
scala> refine[QuadrantI](Point(1, 3))
res0: Either[String,shapeless.tag.@@[Point,QuadrantI]] = Right(Point(1,3))

scala> refine[QuadrantI](Point(3, -2))
res1: Either[String,shapeless.tag.@@[Point,QuadrantI]] = Left(Predicate failed: (3 >= 0 && -2 >= 0).)
```

[provided-predicates]: https://github.com/fthomas/refined#provided-predicates
