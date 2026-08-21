package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Inference
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.numeric.{Greater, Interval}
import org.scalacheck.Prop._
import org.scalacheck.Properties
import eu.timepit.refined.test.ScalaVersionSpecific.illTyped

class CollectionInferenceSpec extends Properties("CollectionInference") {

  property("Exists[A] ==> Exists[B]") = secure {
    Inference[Contains['5'], Exists[Digit]].isValid
  }

  property("Exists ==> NonEmpty") = secure {
    Inference[Exists[Digit], NonEmpty].isValid
  }

  property("NonEmpty =!> Exists") = wellTyped {
    illTyped("Inference[NonEmpty, Exists[Digit]]", "No given instance")
  }

  property("Head[A] ==> Head[B]") = secure {
    Inference[Head[Digit], Head[LetterOrDigit]].isValid
  }

  property("Head[A] ==> Exists[A]") = secure {
    Inference[Head[Digit], Exists[Digit]].isValid
  }

  property("Exists[A] =!> Head[A]") = wellTyped {
    illTyped("Inference[Exists[Digit], Head[Digit]]")
  }

  property("Index[N, A] ==> Index[N, B]") = secure {
    Inference[Index[1, Letter], Index[1, LetterOrDigit]].isValid
  }

  property("Index ==> Exists") = secure {
    Inference[Index[1, LowerCase], Exists[LowerCase]].isValid
  }

  property("Last[A] ==> Last[B]") = secure {
    Inference[Last[Letter], Last[LetterOrDigit]].isValid
  }

  property("Last ==> Exists") = secure {
    Inference[Last[Whitespace], Exists[Whitespace]].isValid
  }

  // `Last ==> NonEmpty` (Scala 2) is not ported: it holds only transitively via `Last ==> Exists` and
  // `Exists ==> NonEmpty`, and the transitivity rule (`hypotheticalSyllogism`) is omitted on Scala 3
  // because its free intermediate type makes implicit search ambiguous. See boolean.scala.

  property("NonEmpty =!> Last") = wellTyped {
    illTyped("Inference[NonEmpty, Last[Whitespace]]", "No given instance")
  }

  property("Size[A] ==> Size[B]") = secure {
    Inference[Size[Greater[5]], Size[Greater[4]]].isValid
  }

  property("Size[Greater[1]] ==> NonEmpty") = secure {
    Inference[Size[Greater[1]], NonEmpty].isValid
  }

  property("Size[Interval.Closed[2, 5]] ==> NonEmpty") = secure {
    Inference[Size[Interval.Closed[2, 5]], NonEmpty].isValid
  }
}
