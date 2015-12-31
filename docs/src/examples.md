The examples from the [README](https://github.com/fthomas/refined/blob/master/README.md):

```tut:nofail
import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import shapeless.tag.@@

val i1: Int @@ Positive = 5

val i2: Int @@ Positive = -5

refineMT[Positive](5)

refineT[Positive](5)

refineT[Positive](-5)
```

```tut:nofail
import shapeless.nat._

val a: Int @@ Greater[_5] = 10

val b: Int @@ Greater[_4] = a

val c: Int @@ Greater[_6] = a
```

```tut:nofail
import shapeless.{ ::, HNil }
import eu.timepit.refined.boolean._
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.generic._
import eu.timepit.refined.string._

refineMT[NonEmpty]("Hello")

refineMT[NonEmpty]("")

type ZeroToOne = Not[Less[_0]] And Not[Greater[_1]]

refineMT[ZeroToOne](1.8)

refineMT[AnyOf[Digit :: Letter :: Whitespace :: HNil]]('F')

refineMT[MatchesRegex[W.`"[0-9]+"`.T]]("123.")

val d1: Char @@ Equal[W.`'3'`.T] = '3'

val d2: Char @@ Digit = d1

val d3: Char @@ Letter = d1

val r1: String @@ Regex = "(a|b)"

val r2: String @@ Regex = "(a|b"

val u1: String @@ Url = "htp://example.com"
```
