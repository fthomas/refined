package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Inference
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.numeric.{Greater, Interval}
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.test.illTyped

class CollectionInferenceSpec extends Properties("CollectionInference") {

  property("Exists[A] ==> Exists[B]") = secure {
    Inference[Contains[W.`'5'`.T], Exists[Digit]].isValid
  }

  property("Exists ==> NonEmpty") = secure {
    Inference[Exists[Digit], NonEmpty].isValid
  }

  property("NonEmpty =!> Exists") = wellTyped {
    illTyped("Inference[NonEmpty, Exists[Digit]]", "could not find.*Inference.*")
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
    Inference[Index[W.`1`.T, Letter], Index[W.`1`.T, LetterOrDigit]].isValid
  }

  property("Index ==> Exists") = secure {
    Inference[Index[W.`1`.T, LowerCase], Exists[LowerCase]].isValid
  }

  property("Last[A] ==> Last[B]") = secure {
    Inference[Last[Letter], Last[LetterOrDigit]].isValid
  }

  property("Last ==> Exists") = secure {
    Inference[Last[Whitespace], Exists[Whitespace]].isValid
  }

  property("Last ==> NonEmpty") = secure {
    Inference[Last[Whitespace], NonEmpty].isValid
  }

  property("NonEmpty =!> Last") = wellTyped {
    illTyped("Inference[NonEmpty, Last[Whitespace]]", "could not find.*Inference.*")
  }

  property("Size[A] ==> Size[B]") = secure {
    Inference[Size[Greater[W.`5`.T]], Size[Greater[W.`4`.T]]].isValid
  }

  property("Size[Greater[_1]] ==> NonEmpty") = secure {
    Inference[Size[Greater[_1]], NonEmpty].isValid
  }

  property("Size[Interval.Closed[_2, _5]] ==> NonEmpty") = secure {
    Inference[Size[Interval.Closed[_2, _5]], NonEmpty].isValid
  }
}
