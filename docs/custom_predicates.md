# Custom predicates

The library comes with a lot [predefined predicates][provided-predicates]
but also allows to define your own. This example shows how to add predicates
for a simple type representing a point in a two-dimensional [Cartesian
coordinate system][cartesian-coordinate-system]. We start by defining a
`Point` class that represents a point in our coordinate system:

```scala
scala> case class Point(x: Int, y: Int)
defined class Point
```

The axes of a two-dimensional Cartesian coordinate system divide the plane into
four infinite regions, called quadrants, which are often numbered 1st to 4th.
Suppose we want to refine `Point`s with the quadrant they are lying in.
So let's create simple types that represent the four quadrants:

```scala
scala> trait Quadrant1
defined trait Quadrant1

scala> trait Quadrant2
defined trait Quadrant2

scala> trait Quadrant3
defined trait Quadrant3

scala> trait Quadrant4
defined trait Quadrant4
```

We now have type-level predicates and a type that we want to refine with these
predicates. The next step is to define instances of the `Predicate` type class
for `Point` that are indexed by the corresponding quadrant predicate. We use
the `Predicate.instance` function to create the instances from two functions,
one that checks if a given `Point` lies in the corresponding quadrant and one
that provides a string representation for the predicate that is used for error
messages:

```scala
import eu.timepit.refined.Predicate

implicit val quadrant1Predicate: Predicate[Quadrant1, Point] =
  Predicate.instance(p => p.x >= 0 && p.y >= 0, p => s"($p is in quadrant 1)")

implicit val quadrant2Predicate: Predicate[Quadrant2, Point] =
  Predicate.instance(p => p.x < 0 && p.y >= 0, p => s"($p is in quadrant 2)")

implicit val quadrant3Predicate: Predicate[Quadrant3, Point] =
  Predicate.instance(p => p.x < 0 && p.y < 0, p => s"($p is in quadrant 3)")

implicit val quadrant4Predicate: Predicate[Quadrant4, Point] =
  Predicate.instance(p => p.x >= 0 && p.y < 0, p => s"($p is in quadrant 4)")
```

We have now everything in place to refine `Point` values with the `refine`
function and our predicates:

```scala
scala> import eu.timepit.refined.refineT
import eu.timepit.refined.refineT

scala> refineT[Quadrant1](Point(1, 3))
res4: Either[String,shapeless.tag.@@[Point,Quadrant1]] = Right(Point(1,3))

scala> refineT[Quadrant1](Point(3, -2))
res5: Either[String,shapeless.tag.@@[Point,Quadrant1]] = Left(Predicate failed: (Point(3,-2) is in quadrant 1).)

scala> refineT[Quadrant4](Point(3, -2))
res6: Either[String,shapeless.tag.@@[Point,Quadrant4]] = Right(Point(3,-2))
```

We can also use refined's higher order predicates, which take other predicates
as arguments, with our quadrant predicates (without defining corresponding
`Predicate` instances):

```scala
scala> import eu.timepit.refined.boolean.Not
import eu.timepit.refined.boolean.Not

scala> refineT[Not[Quadrant1]](Point(-3, -9))
res7: Either[String,shapeless.tag.@@[Point,eu.timepit.refined.boolean.Not[Quadrant1]]] = Right(Point(-3,-9))

scala> refineT[Not[Quadrant1]](Point(5, 4))
res8: Either[String,shapeless.tag.@@[Point,eu.timepit.refined.boolean.Not[Quadrant1]]] = Left(Predicate (Point(5,4) is in quadrant 1) did not fail.)

scala> import eu.timepit.refined.boolean.Or
import eu.timepit.refined.boolean.Or

scala> type Quadrant1Or3 = Quadrant1 Or Quadrant3
defined type alias Quadrant1Or3

scala> refineT[Quadrant1Or3](Point(1, 3))
res9: Either[String,shapeless.tag.@@[Point,Quadrant1Or3]] = Right(Point(1,3))

scala> refineT[Quadrant1Or3](Point(-3, -2))
res10: Either[String,shapeless.tag.@@[Point,Quadrant1Or3]] = Right(Point(-3,-2))

scala> refineT[Quadrant1Or3](Point(3, -2))
res11: Either[String,shapeless.tag.@@[Point,Quadrant1Or3]] = Left(Both predicates of ((Point(3,-2) is in quadrant 1) || (Point(3,-2) is in quadrant 3)) failed. Left: Predicate failed: (Point(3,-2) is in quadrant 1). Right: Predicate failed: (Point(3,-2) is in quadrant 3).)
```

[provided-predicates]: https://github.com/fthomas/refined#provided-predicates
[cartesian-coordinate-system]: http://en.wikipedia.org/wiki/Cartesian_coordinate_system
