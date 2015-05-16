package eu.timepit.refined

import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.tag.@@
import shapeless.test.illTyped

class RefinedSpec extends Properties("refined") {
  property("refine success") = secure {
    refine[Greater[_5], Int](6).isRight
  }

  property("refine failure") = secure {
    refine[Forall[LowerCase], String]("Hallo").isLeft
  }

  property("refineLit success with String") = secure {
    def ignore: String @@ Forall[LowerCase] = refineLit[Forall[LowerCase], String]("hello")
    true
  }

  property("refineLit failure with String") = secure {
    illTyped("""refineLit[Forall[UpperCase], String]("hello")""")
    true
  }

  /*
  fails on fresh builds:
  property("refineLit success with Int") = secure {
    def ignore = refineLit[Greater[_10], Int](15)
    true
  }
  */

  property("refineLit failure with Int") = secure {
    illTyped("""refineLit[Greater[_10], Int](5)""")
    true
  }

  property("refineLit success with custom Predicate") = secure {
    type ShortString = Size[LessEqual[_10]]
    def ignore: String @@ ShortString = refineLit[ShortString, String]("abc")
    true
  }

  property("refineLit failure with custom Predicate") = secure {
    type ShortString = Size[LessEqual[_10]]
    illTyped("""refineLit[ShortString, String]("abcdefghijklmnopqrstuvwxyz")""")
    true
  }

  property("refineLit success with Char") = secure {
    def ignore: Char @@ LowerCase = refineLit[LowerCase, Char]('c')
    true
  }
}
