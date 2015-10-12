# Custom predicates

The library comes with a lot [predefined predicates][provided-predicates]
but also allows to define your own. This example shows how to add predicates
for a simple type representing a point in a two-dimensional [Cartesian
coordinate system][cartesian-coordinate-system]. We start by defining a
`Point` class that represents a point in our coordinate system:

```tut
case class Point(x: Int, y: Int)
```

The axes of a two-dimensional Cartesian coordinate system divide the plane into
four infinite regions, called quadrants, which are often numbered 1st to 4th.
Suppose we want to refine `Point`s with the quadrant they are lying in.
So let's create simple types that represent the four quadrants:

```tut
case class Quadrant1()
case class Quadrant2()
case class Quadrant3()
case class Quadrant4()
```

We now have type-level predicates and a type that we want to refine with these
predicates. The next step is to define instances of the `Validate` type class
for `Point` that are indexed by the corresponding quadrant predicate. We use
the `Validate.fromPredicate` function to create the instances from two functions,
one that checks if a given `Point` lies in the corresponding quadrant and one
that provides a string representation for the predicate that is used for error
messages:

```tut:silent
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

We have now everything in place to refine `Point` values with the `refineT`
function and our predicates:

```tut
import eu.timepit.refined.refineT

refineT[Quadrant1](Point(1, 3))

refineT[Quadrant1](Point(3, -2))

refineT[Quadrant4](Point(3, -2))
```

We can also use refined's higher order predicates, which take other predicates
as arguments, with our quadrant predicates (without defining corresponding
`Validate` instances):

```tut
import eu.timepit.refined.boolean.Not

refineT[Not[Quadrant1]](Point(-3, -9))

refineT[Not[Quadrant1]](Point(5, 4))

import eu.timepit.refined.boolean.Or

type Quadrant1Or3 = Quadrant1 Or Quadrant3

refineT[Quadrant1Or3](Point(1, 3))

refineT[Quadrant1Or3](Point(-3, -2))

refineT[Quadrant1Or3](Point(3, -2))
```

[provided-predicates]: https://github.com/fthomas/refined#provided-predicates
[cartesian-coordinate-system]: http://en.wikipedia.org/wiki/Cartesian_coordinate_system
