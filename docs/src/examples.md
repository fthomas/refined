The examples from the [README.md](https://github.com/fthomas/refined/blob/master/README.md):

```tut:nofail
import eu.timepit.refined._
import eu.timepit.refined.numeric._

refineLit[Positive](5)

refineLit[Positive](-5)

refine[Positive](5)

refine[Positive](-5)
```

```tut:nofail
import shapeless.nat._
import shapeless.tag.@@

val a: Int @@ Greater[_5] = refineLit(10)

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

refineLit[NonEmpty]("Hello")

refineLit[NonEmpty]("")

type ZeroToOne = Not[Less[_0]] And Not[Greater[_1]]

refineLit[ZeroToOne](1.8)

refineLit[AnyOf[Digit :: Letter :: Whitespace :: HNil]]('F')

refineLit[MatchesRegex[W.`"[0-9]+"`.T]]("123.")

val d1: Char @@ Equal[W.`'3'`.T] = refineLit('3')

val d2: Char @@ Digit = d1

val d3: Char @@ Letter = d1
```
