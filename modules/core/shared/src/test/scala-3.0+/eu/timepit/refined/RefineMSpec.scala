package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Refined
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.MatchesRegex
import org.scalacheck.Prop._
import org.scalacheck.Properties
import eu.timepit.refined.test.ScalaVersionSpecific.illTyped

// Ported from the Scala 2 `RefineMSpec`. The `refineMT` (shapeless `@@`) variants are dropped
// because the tag encoding is not available on Scala 3; only the `refineMV` (`Refined`) cases remain.
class RefineMSpec extends Properties("refineM") {

  property("RefineMVBuilder instance") = secure {
    val rv = refineMV[Digit]
    rv('0') == Refined.unsafeApply('0')
  }

  property("refineM with Forall") = wellTyped {
    def ignore1: String Refined Forall[LowerCase] = refineMV[Forall[LowerCase]]("hello")
    illTyped("""refineMV[Forall[UpperCase]]("hello")""", "Predicate.*fail.*")
  }

  property("refineM with Greater") = wellTyped {
    def ignore1: Int Refined Greater[10] = refineMV[Greater[10]](15)
    illTyped("""refineMV[Greater[10]](5)""", "Predicate.*fail.*")
  }

  property("refineM with Size") = wellTyped {
    type ShortString = Size[LessEqual[10]]
    def ignore1: String Refined ShortString = refineMV[ShortString]("abc")
    illTyped("""refineMV[Size[LessEqual[10]]]("abcdefghijklmnopqrstuvwxyz")""", "Predicate.*fail.*")
  }

  property("refineM with LowerCase") = wellTyped {
    def ignore1: Char Refined LowerCase = refineMV[LowerCase]('c')
    illTyped("refineMV[LowerCase]('C')", "Predicate.*failed.*")
  }

  property("refineM with MatchesRegex") = wellTyped {
    def ignore1: String Refined MatchesRegex["[0-9]+"] = refineMV[MatchesRegex["[0-9]+"]]("123")
    illTyped("""refineMV[MatchesRegex["[0-9]+"]]("abc")""", "Predicate.*fail.*")
  }

  property("refineM with Contains") = wellTyped {
    def ignore1: String Refined Contains['c'] = refineMV[Contains['c']]("abcd")
    illTyped("""refineMV[Contains['c']]("abde")""", "Predicate.*fail.*")
  }

  property("refineM with Double Witness") = wellTyped {
    def ignore1: Double Refined Greater[2.3] = refineMV[Greater[2.3]](2.4)
    illTyped("refineMV[Greater[2.3]](2.2)", "Predicate.*fail.*")
  }

  property("refineM failure with non-literals") = wellTyped {
    // hearth's `semiEval` can evaluate more than plain literals at compile time (e.g. `List(1, 2, 3)`),
    // so the failure case must use a genuinely non-compile-time-constant value — here a `def`.
    illTyped(
      "{ def xs: List[Int] = List(1, 2, 3); refineMV[NonEmpty](xs) }",
      "Cannot evaluate expression at compile time"
    )
  }
}
