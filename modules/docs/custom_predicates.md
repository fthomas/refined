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
scala> case class Quadrant1()
defined class Quadrant1

scala> case class Quadrant2()
defined class Quadrant2

scala> case class Quadrant3()
defined class Quadrant3

scala> case class Quadrant4()
defined class Quadrant4
```

We now have type-level predicates and a type that we want to refine with these
predicates. The next step is to define instances of the `Validate` type class
for `Point` that are indexed by the corresponding quadrant predicate. We use
the `Validate.fromPredicate` function to create the instances from two functions,
one that checks if a given `Point` lies in the corresponding quadrant and one
that provides a string representation for the predicate that is used for error
messages:

```scala
import eu.timepit.refined.api.Validate

implicit val quadrant1Validate: Validate.Plain[Point, Quadrant1] =
  Validate.fromPredicate(p => p.x >= 0 && p.y >= 0, p => s"($p is in quadrant 1)", Quadrant1())

implicit val quadrant2Validate: Validate.Plain[Point, Quadrant2] =
  Validate.fromPredicate(p => p.x < 0 && p.y >= 0, p => s"($p is in quadrant 2)", Quadrant2())

implicit val quadrant3Validate: Validate.Plain[Point, Quadrant3] =
  Validate.fromPredicate(p => p.x < 0 && p.y < 0, p => s"($p is in quadrant 3)", Quadrant3())

implicit val quadrant4Validate: Validate.Plain[Point, Quadrant4] =
  Validate.fromPredicate(p => p.x >= 0 && p.y < 0, p => s"($p is in quadrant 4)", Quadrant4())
```

We have now everything in place to refine `Point` values with the `refineV`
function and our predicates:

```scala
scala> import eu.timepit.refined.refineV
import eu.timepit.refined.refineV

scala> refineV[Quadrant1](Point(1, 3))
res0: Either[String,eu.timepit.refined.api.Refined[Point,Quadrant1]] = Right(Point(1,3))

scala> refineV[Quadrant1](Point(3, -2))
res1: Either[String,eu.timepit.refined.api.Refined[Point,Quadrant1]] = Left(Predicate failed: (Point(3,-2) is in quadrant 1).)

scala> refineV[Quadrant4](Point(3, -2))
res2: Either[String,eu.timepit.refined.api.Refined[Point,Quadrant4]] = Right(Point(3,-2))
```

We can also use refined's higher order predicates, which take other predicates
as arguments, with our quadrant predicates (without defining corresponding
`Validate` instances):

```scala
scala> import eu.timepit.refined.boolean.Not
import eu.timepit.refined.boolean.Not

scala> refineV[Not[Quadrant1]](Point(-3, -9))
res3: Either[String,eu.timepit.refined.api.Refined[Point,eu.timepit.refined.boolean.Not[Quadrant1]]] = Right(Point(-3,-9))

scala> refineV[Not[Quadrant1]](Point(5, 4))
res4: Either[String,eu.timepit.refined.api.Refined[Point,eu.timepit.refined.boolean.Not[Quadrant1]]] = Left(Predicate (Point(5,4) is in quadrant 1) did not fail.)

scala> import eu.timepit.refined.boolean.Or
import eu.timepit.refined.boolean.Or

scala> type Quadrant1Or3 = Quadrant1 Or Quadrant3
defined type alias Quadrant1Or3

scala> refineV[Quadrant1Or3](Point(1, 3))
res5: Either[String,eu.timepit.refined.api.Refined[Point,Quadrant1Or3]] = Right(Point(1,3))

scala> refineV[Quadrant1Or3](Point(-3, -2))
res6: Either[String,eu.timepit.refined.api.Refined[Point,Quadrant1Or3]] = Right(Point(-3,-2))

scala> refineV[Quadrant1Or3](Point(3, -2))
res7: Either[String,eu.timepit.refined.api.Refined[Point,Quadrant1Or3]] = Left(Both predicates of ((Point(3,-2) is in quadrant 1) || (Point(3,-2) is in quadrant 3)) failed. Left: Predicate failed: (Point(3,-2) is in quadrant 1). Right: Predicate failed: (Point(3,-2) is in quadrant 3).)
```

[provided-predicates]: https://github.com/fthomas/refined#provided-predicates
[cartesian-coordinate-system]: http://en.wikipedia.org/wiki/Cartesian_coordinate_system
