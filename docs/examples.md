The examples from the [README](https://github.com/fthomas/refined/blob/master/README.md):

```scala
scala> import eu.timepit.refined._
import eu.timepit.refined._

scala> import eu.timepit.refined.numeric._
import eu.timepit.refined.numeric._

scala> refineLit[Positive](5)
res0: shapeless.tag.@@[Int,eu.timepit.refined.numeric.Positive] = 5

scala> refineLit[Positive](-5)
<console>:18: error: Predicate failed: (-5 > 0).
       refineLit[Positive](-5)
                          ^

scala> refine[Positive](5)
res2: Either[String,shapeless.tag.@@[Int,eu.timepit.refined.numeric.Positive]] = Right(5)

scala> refine[Positive](-5)
res3: Either[String,shapeless.tag.@@[Int,eu.timepit.refined.numeric.Positive]] = Left(Predicate failed: (-5 > 0).)
```

```scala
scala> import eu.timepit.refined.implicits._
import eu.timepit.refined.implicits._

scala> import shapeless.nat._
import shapeless.nat._

scala> import shapeless.tag.@@
import shapeless.tag.$at$at

scala> val a: Int @@ Greater[_5] = refineLit(10)
a: shapeless.tag.@@[Int,eu.timepit.refined.numeric.Greater[shapeless.nat._5]] = 10

scala> val b: Int @@ Greater[_4] = a
b: shapeless.tag.@@[Int,eu.timepit.refined.numeric.Greater[shapeless.nat._4]] = 10

scala> val c: Int @@ Greater[_6] = a
<console>:25: error: invalid inference: eu.timepit.refined.numeric.Greater[shapeless.nat._5] ==> eu.timepit.refined.numeric.Greater[shapeless.nat._6]
       val c: Int @@ Greater[_6] = a
                                   ^
```

```scala
scala> import shapeless.{ ::, HNil }
import shapeless.{$colon$colon, HNil}

scala> import eu.timepit.refined.boolean._
import eu.timepit.refined.boolean._

scala> import eu.timepit.refined.char._
import eu.timepit.refined.char._

scala> import eu.timepit.refined.collection._
import eu.timepit.refined.collection._

scala> import eu.timepit.refined.generic._
import eu.timepit.refined.generic._

scala> import eu.timepit.refined.string._
import eu.timepit.refined.string._

scala> refineLit[NonEmpty]("Hello")
res4: shapeless.tag.@@[String,eu.timepit.refined.collection.NonEmpty] = Hello

scala> refineLit[NonEmpty]("")
<console>:41: error: Predicate isEmpty() did not fail.
       refineLit[NonEmpty]("")
                          ^

scala> type ZeroToOne = Not[Less[_0]] And Not[Greater[_1]]
defined type alias ZeroToOne

scala> refineLit[ZeroToOne](1.8)
<console>:42: error: Right predicate of (!(1.8 < 0) && !(1.8 > 1)) failed: Predicate (1.8 > 1) did not fail.
       refineLit[ZeroToOne](1.8)
                           ^

scala> refineLit[AnyOf[Digit :: Letter :: Whitespace :: HNil]]('F')
res7: shapeless.tag.@@[Char,eu.timepit.refined.boolean.AnyOf[shapeless.::[eu.timepit.refined.char.Digit,shapeless.::[eu.timepit.refined.char.Letter,shapeless.::[eu.timepit.refined.char.Whitespace,shapeless.HNil]]]]] = F

scala> refineLit[MatchesRegex[W.`"[0-9]+"`.T]]("123.")
<console>:41: error: Predicate failed: "123.".matches("[0-9]+").
       refineLit[MatchesRegex[W.`"[0-9]+"`.T]]("123.")
                                              ^

scala> val d1: Char @@ Equal[W.`'3'`.T] = '3'
d1: shapeless.tag.@@[Char,eu.timepit.refined.generic.Equal[Char('3')]] = 3

scala> val d2: Char @@ Digit = d1
d2: shapeless.tag.@@[Char,eu.timepit.refined.char.Digit] = 3

scala> val d3: Char @@ Letter = d1
<console>:41: error: invalid inference: eu.timepit.refined.generic.Equal[Char('3')] ==> eu.timepit.refined.char.Letter
       val d3: Char @@ Letter = d1
                                ^

scala> val r1: String @@ Regex = "(a|b)"
r1: shapeless.tag.@@[String,eu.timepit.refined.string.Regex] = (a|b)

scala> val r2: String @@ Regex = "(a|b"
<console>:40: error: Predicate isValidRegex("(a|b") failed: Unclosed group near index 4
(a|b
    ^
       val r2: String @@ Regex = "(a|b"
                                 ^
```
