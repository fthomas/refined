# Nonnegative Integer

[Erik Erlandson][erikerlandson] wrote a very good [introduction about
the benefits of a dedicated nonnegative integer type][non-negative-numerics]
(i.e. an `Int` that is greater than or equal to zero). The main benefit
is that such a type precisely states all the valid values of a function
parameter and that it prevents the creation of invalid values. This is
one aspect of Yaron Minsky's principle of
[making illegal states unrepresentable][effective-ml-revisited].

Erik's idea is similar to what *refined* provides but uses exceptions
to prevent the creation of invalid values. This example shows how his
`NonNegInt` can be recreated with *refined*:

```tut
import eu.timepit.refined.implicits._
import eu.timepit.refined.numeric.NonNegative
import eu.timepit.refined.Refined

// define a type alias for Int refined by the NonNegative predicate
type NonNegInt = Int Refined NonNegative

// due to an implicit conversion, a NonNegInt can be passed to
// functions that require an Int
def element[T](seq: Seq[T], j: NonNegInt) = seq(j)

// call element function with a regular Int index
element(Vector(1, 2, 3), 1)
```

```tut:fail
// trying to call element with a negative Int fails to compile
element(Vector(1, 2, 3), -1)
```

[erikerlandson]: http://erikerlandson.github.io
[non-negative-numerics]: http://erikerlandson.github.io/blog/2015/08/18/lightweight-non-negative-numerics-for-better-scala-type-signatures/
[effective-ml-revisited]: https://blogs.janestreet.com/effective-ml-revisited/
