package eu.timepit.refined

import eu.timepit.refined.boolean.{ False, True }
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.MatchesRegex
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.tag.@@
import shapeless.test.illTyped

class RefineSpec extends Properties("refine") {
  property("refine success with Less") = secure {
    refine[Less[W.`100`.T]](-100).isRight
  }

  property("refine success with Greater") = secure {
    refine[Greater[_5]](6).isRight
  }

  property("refine failure with Forall") = secure {
    refine[Forall[LowerCase]]("Hallo").isLeft
  }

  property("refine success with MatchesRegex") = secure {
    type DigitsOnly = MatchesRegex[W.`"[0-9]+"`.T]
    val res = refine[DigitsOnly]("123"): Either[String, String @@ DigitsOnly]
    res.isRight
  }

  property("refineLit success with Forall") = secure {
    def ignore: String @@ Forall[LowerCase] = refineLit[Forall[LowerCase]]("hello")
    true
  }

  property("refineLit failure with Forall") = secure {
    illTyped("""refineLit[Forall[UpperCase]]("hello")""")
    true
  }

  property("refineLit success with Greater") = secure {
    def ignore: Int @@ Greater[_10] = refineLit[Greater[_10]](15)
    true
  }

  property("refineLit failure with Greater") = secure {
    illTyped("""refineLit[Greater[_10]](5)""")
    true
  }

  property("refineLit success with custom Predicate") = secure {
    type ShortString = Size[LessEqual[_10]]
    def ignore: String @@ ShortString = refineLit[ShortString]("abc")
    true
  }

  property("refineLit failure with custom Predicate") = secure {
    type ShortString = Size[LessEqual[_10]]
    illTyped("""refineLit[ShortString]("abcdefghijklmnopqrstuvwxyz")""")
    true
  }

  property("refineLit success with LowerCase") = secure {
    def ignore: Char @@ LowerCase = refineLit[LowerCase]('c')
    true
  }

  property("refineLit success with MatchesRegex") = secure {
    def ignore: String @@ MatchesRegex[W.`"[0-9]+"`.T] =
      refineLit[MatchesRegex[W.`"[0-9]+"`.T]]("123")
    true
  }

  property("refineLit failure with MatchesRegex") = secure {
    illTyped("""refineLit[MatchesRegex[W.`"[0-9]+"`.T]]("abc")""")
    true
  }

  property("refineLit success with Contains") = secure {
    type P = Contains[W.`'c'`.T]
    def ignore: String @@ P = refineLit[P]("abcd")
    true
  }

  property("refineLit failure with Contains") = secure {
    type P = Contains[W.`'c'`.T]
    illTyped("""refineLit[P]("abde")""")
    true
  }

  property("instantiate refineLit") = secure {
    refineLit[True] != refineLit[False]
  }
}
