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

class RefineMSpec extends Properties("refineM") {

  property("RefineM instance") = secure {
    val r = refineMT[Digit]
    r('0') == '0'
  }

  property("refineMT with Forall") = secure {
    def ignore: String @@ Forall[LowerCase] = refineMT[Forall[LowerCase]]("hello")
    illTyped("""refineMT[Forall[UpperCase]]("hello")""", "Predicate.*fail.*")
    true
  }

  property("refineMT with Greater") = secure {
    def ignore: Int @@ Greater[_10] = refineMT[Greater[_10]](15)
    illTyped("""refineMT[Greater[_10]](5)""", "Predicate.*fail.*")
    true
  }

  property("refineMT with Size") = secure {
    type ShortString = Size[LessEqual[_10]]
    def ignore: String @@ ShortString = refineMT[ShortString]("abc")
    illTyped("""refineMT[ShortString]("abcdefghijklmnopqrstuvwxyz")""", "Predicate.*fail.*")
    true
  }

  property("refineMT with LowerCase") = secure {
    def ignore: Char @@ LowerCase = refineMT[LowerCase]('c')
    illTyped("refineMT[LowerCase]('C')", "Predicate.*failed.*")
    true
  }

  property("refineMT with MatchesRegex") = secure {
    def ignore: String @@ MatchesRegex[W.`"[0-9]+"`.T] = refineMT("123")
    illTyped("""refineMT[MatchesRegex[W.`"[0-9]+"`.T]]("abc")""", "Predicate.*fail.*")
    true
  }

  property("refineMT with Contains") = secure {
    def ignore: String @@ Contains[W.`'c'`.T] = refineMT("abcd")
    illTyped("""refineMT[Contains[W.`'c'`.T]]("abde")""", "Predicate.*fail.*")
    true
  }

  property("refineMT with Double Witness") = secure {
    def ignore: Double @@ Greater[W.`2.3`.T] = refineMT(2.4)
    illTyped("refineMT[Greater[W.`2.3`.T]](2.2)", "Predicate.*fail.*")
    true
  }

  property("refineMT fails on non-literals") = secure {
    illTyped("refineMT[NonEmpty](List(1, 2, 3))", "refineM only supports literals.*")
    true
  }
}
