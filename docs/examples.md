The examples from the [README](https://github.com/fthomas/refined/blob/master/README.md):

```scala
scala> import eu.timepit.refined._
import eu.timepit.refined._

scala> import eu.timepit.refined.api.Refined
import eu.timepit.refined.api.Refined

scala> import eu.timepit.refined.auto._
import eu.timepit.refined.auto._

scala> import eu.timepit.refined.numeric._
import eu.timepit.refined.numeric._

scala> val i1: Int Refined Positive = 5
i1: eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Positive] = 5

scala> val i2: Int Refined Positive = -5
<console>:22: error: Predicate failed: (-5 > 0).
       val i2: Int Refined Positive = -5
                                       ^

scala> refineMV[Positive](5)
res0: eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Positive] = 5

scala> refineV[Positive](5)
res1: Either[String,eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Positive]] = Right(5)

scala> refineV[Positive](-5)
res2: Either[String,eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Positive]] = Left(Predicate failed: (-5 > 0).)
```

```scala
scala> import shapeless.nat._
import shapeless.nat._

scala> val a: Int Refined Greater[_5] = 10
a: eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Greater[shapeless.nat._5]] = 10

scala> val b: Int Refined Greater[_4] = a
b: eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Greater[shapeless.nat._4]] = 10

scala> val c: Int Refined Greater[_6] = a
<console>:26: error: type mismatch (invalid inference):
 eu.timepit.refined.numeric.Greater[shapeless.nat._5] does not imply
 eu.timepit.refined.numeric.Greater[shapeless.nat._6]
       val c: Int Refined Greater[_6] = a
                                        ^
```

```scala
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

scala> import shapeless.{ ::, HNil }
import shapeless.{$colon$colon, HNil}

scala> refineMV[NonEmpty]("Hello")
res3: eu.timepit.refined.api.Refined[String,eu.timepit.refined.collection.NonEmpty] = Hello

scala> refineMV[NonEmpty]("")
<console>:42: error: Predicate isEmpty() did not fail.
       refineMV[NonEmpty]("")
                         ^

scala> type ZeroToOne = Not[Less[_0]] And Not[Greater[_1]]
defined type alias ZeroToOne

scala> refineMV[ZeroToOne](1.8)
<console>:43: error: Right predicate of (!(1.8 < 0) && !(1.8 > 1)) failed: Predicate (1.8 > 1) did not fail.
       refineMV[ZeroToOne](1.8)
                          ^

scala> refineMV[AnyOf[Digit :: Letter :: Whitespace :: HNil]]('F')
res6: eu.timepit.refined.api.Refined[Char,eu.timepit.refined.boolean.AnyOf[shapeless.::[eu.timepit.refined.char.Digit,shapeless.::[eu.timepit.refined.char.Letter,shapeless.::[eu.timepit.refined.char.Whitespace,shapeless.HNil]]]]] = F

scala> refineMV[MatchesRegex[W.`"[0-9]+"`.T]]("123.")
<console>:42: error: Predicate failed: "123.".matches("[0-9]+").
       refineMV[MatchesRegex[W.`"[0-9]+"`.T]]("123.")
                                             ^

scala> val d1: Char Refined Equal[W.`'3'`.T] = '3'
d1: eu.timepit.refined.api.Refined[Char,eu.timepit.refined.generic.Equal[Char('3')]] = 3

scala> val d2: Char Refined Digit = d1
d2: eu.timepit.refined.api.Refined[Char,eu.timepit.refined.char.Digit] = 3

scala> val d3: Char Refined Letter = d1
<console>:42: error: type mismatch (invalid inference):
 eu.timepit.refined.generic.Equal[Char('3')] does not imply
 eu.timepit.refined.char.Letter
       val d3: Char Refined Letter = d1
                                     ^

scala> val r1: String Refined Regex = "(a|b)"
r1: eu.timepit.refined.api.Refined[String,eu.timepit.refined.string.Regex] = (a|b)

scala> val r2: String Refined Regex = "(a|b"
<console>:41: error: Regex predicate failed: Unclosed group near index 4
(a|b
    ^
       val r2: String Refined Regex = "(a|b"
                                      ^

scala> val u1: String Refined Url = "htp://example.com"
<console>:41: error: Url predicate failed: unknown protocol: htp
       val u1: String Refined Url = "htp://example.com"
                                    ^
```
