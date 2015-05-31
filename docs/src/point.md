# Custom predicate

The library comes with a lot [predefined predicates][provided-predicates]
but also allows to define your own. This example shows how to add a
predicate for a simple type representing a point in a two-dimensional
Cartesian coordinate system. We start by defining a `Point` class that
represents a point in our coordinate system:

```tut
case class Point(x: Int, y: Int)
```

The axes of a Cartesian coordinate system divide the plane into four infinite
regions, called quadrants, which are often labelled with Roman numerals from I
to IV. Suppose we want to refine `Point`s with the quadrant they are lying in.
So let's create a simple type that represents the first quadrant:

```tut
trait QuadrantI
```

We now have type-level predicate and a type that we want to refine with this
predicate.

```tut
import eu.timepit.refined._

implicit val quadrantIPredicate: Predicate[QuadrantI, Point] =
  Predicate.instance(p => p.x >= 0 && p.y >= 0, p => s"(${p.x} >= 0 && ${p.y} >= 0)")
```

```tut  
refine[QuadrantI](Point(1, 3))

refine[QuadrantI](Point(3, -2))
```

[provided-predicates]: https://github.com/fthomas/refined#provided-predicates
