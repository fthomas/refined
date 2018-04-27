package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Refined
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

  property("RefineMPartiallyApplied instance") = secure {
    val rv = refineMV[Digit]
    val rt = refineMT[Digit]
    rv('0') == Refined.unsafeApply('0') && rt('0') == '0'
  }

  property("refineM with Forall") = wellTyped {
    def ignore1: String Refined Forall[LowerCase] = refineMV[Forall[LowerCase]]("hello")
    def ignore2: String @@ Forall[LowerCase] = refineMT[Forall[LowerCase]]("hello")
    illTyped("""refineMV[Forall[UpperCase]]("hello")""", "Predicate.*fail.*")
    illTyped("""refineMT[Forall[UpperCase]]("hello")""", "Predicate.*fail.*")
  }

  property("refineM with Greater") = wellTyped {
    def ignore1: Int Refined Greater[_10] = refineMV[Greater[_10]](15)
    def ignore2: Int @@ Greater[_10] = refineMT[Greater[_10]](15)
    illTyped("""refineMV[Greater[_10]](5)""", "Predicate.*fail.*")
    illTyped("""refineMT[Greater[_10]](5)""", "Predicate.*fail.*")
  }

  property("refineM with Size") = wellTyped {
    type ShortString = Size[LessEqual[_10]]
    def ignore1: String Refined ShortString = refineMV[ShortString]("abc")
    def ignore2: String @@ ShortString = refineMT[ShortString]("abc")
    illTyped("""refineMV[ShortString]("abcdefghijklmnopqrstuvwxyz")""", "Predicate.*fail.*")
    illTyped("""refineMT[ShortString]("abcdefghijklmnopqrstuvwxyz")""", "Predicate.*fail.*")
  }

  property("refineM with LowerCase") = wellTyped {
    def ignore1: Char Refined LowerCase = refineMV[LowerCase]('c')
    def ignore2: Char @@ LowerCase = refineMT[LowerCase]('c')
    illTyped("refineMV[LowerCase]('C')", "Predicate.*failed.*")
    illTyped("refineMT[LowerCase]('C')", "Predicate.*failed.*")
  }

  property("refineM with MatchesRegex") = wellTyped {
    def ignore1: String Refined MatchesRegex["[0-9]+"] = refineMV("123")
    def ignore2: String @@ MatchesRegex["[0-9]+"] = refineMT("123")
    illTyped("""refineMV[MatchesRegex["[0-9]+"]]("abc")""", "Predicate.*fail.*")
    illTyped("""refineMT[MatchesRegex["[0-9]+"]]("abc")""", "Predicate.*fail.*")
  }

  property("refineM with Contains") = wellTyped {
    def ignore1: String Refined Contains['c'] = refineMV("abcd")
    def ignore2: String @@ Contains['c'] = refineMT("abcd")
    illTyped("""refineMV[Contains['c']]("abde")""", "Predicate.*fail.*")
    illTyped("""refineMT[Contains['c']]("abde")""", "Predicate.*fail.*")
  }

  property("refineM with Double Witness") = wellTyped {
    def ignore1: Double Refined Greater[2.3] = refineMV(2.4)
    def ignore2: Double @@ Greater[2.3] = refineMT(2.4)
    illTyped("refineMT[Greater[2.3]](2.2)", "Predicate.*fail.*")
    illTyped("refineMV[Greater[2.3]](2.2)", "Predicate.*fail.*")
  }

  property("refineM failure with non-literals") = wellTyped {
    illTyped("refineMV[NonEmpty](List(1, 2, 3))", "compile-time refinement.*")
    illTyped("refineMT[NonEmpty](List(1, 2, 3))", "compile-time refinement.*")
  }
}
