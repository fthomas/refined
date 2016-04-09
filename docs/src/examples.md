The examples from the [README](https://github.com/fthomas/refined/blob/master/README.md):

```tut:nofail
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

val i1: Int Refined Positive = 5

val i2: Int Refined Positive = -5

refineMV[Positive](5)

refineV[Positive](5)

refineV[Positive](-5)
```

```tut:nofail
import shapeless.nat._

val a: Int Refined Greater[_5] = 10

val b: Int Refined Greater[_4] = a

val c: Int Refined Greater[_6] = a
```

```tut:nofail
import eu.timepit.refined.boolean._
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.generic._
import eu.timepit.refined.string._
import shapeless.{ ::, HNil }

refineMV[NonEmpty]("Hello")

refineMV[NonEmpty]("")

type ZeroToOne = Not[Less[_0]] And Not[Greater[_1]]

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
