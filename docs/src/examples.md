The examples from the [README](https://github.com/fthomas/refined/blob/master/README.md):

```tut:silent
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
```
```tut:nofail
val i1: Int Refined Positive = 5

val i2: Int Refined Positive = -5

refineMV[Positive](5)

val x = 42

refineV[Positive](x)

refineV[Positive](-x)
```

```tut:nofail
val a: Int Refined Greater[W.`5`.T] = 10

val b: Int Refined Greater[W.`4`.T] = a

val c: Int Refined Greater[W.`6`.T] = a
```

```tut:silent
import eu.timepit.refined.boolean._
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.generic._
import eu.timepit.refined.string._
import shapeless.{ ::, HNil }
```
```tut:nofail
refineMV[NonEmpty]("Hello")

refineMV[NonEmpty]("")

type ZeroToOne = Not[Less[W.`0.0`.T]] And Not[Greater[W.`1.0`.T]]

refineMV[ZeroToOne](1.8)

refineMV[AnyOf[Digit :: Letter :: Whitespace :: HNil]]('F')

refineMV[MatchesRegex[W.`"[0-9]+"`.T]]("123.")

val d1: Char Refined Equal[W.`'3'`.T] = '3'

val d2: Char Refined Digit = d1

val d3: Char Refined Letter = d1

val r1: String Refined Regex = "(a|b)"

val r2: String Refined Regex = "(a|b"

val u1: String Refined Url = "htp://example.com"
```
