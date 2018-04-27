package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Inference
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.numeric.Greater
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.test.illTyped

class CollectionInferenceSpec extends Properties("CollectionInference") {

  property("Exists[A] ==> Exists[B]") = secure {
    Inference[Contains['5'], Exists[Digit]].isValid
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
    illTyped("Inference[Exists[Digit], Head[Digit]]",
             "diverging implicit expansion for.*Inference.*")
  }

  property("Index[N, A] ==> Index[N, B]") = secure {
    Inference[Index[_1, Letter], Index[_1, LetterOrDigit]].isValid
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

  property("Last ==> NonEmpty") = secure {
    Inference[Last[Whitespace], NonEmpty].isValid
  }

  property("NonEmpty =!> Last") = wellTyped {
    illTyped("Inference[NonEmpty, Last[Whitespace]]", "could not find.*Inference.*")
  }

  property("Size[A] ==> Size[B]") = secure {
    Inference[Size[Greater[_5]], Size[Greater[_4]]].isValid
  }
}
