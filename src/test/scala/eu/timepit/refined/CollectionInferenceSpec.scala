package eu.timepit.refined

import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.numeric.Greater
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.test.illTyped

class CollectionInferenceSpec extends Properties("CollectionInference") {

  property("Exists[A] ==> Exists[B]") = secure {
    InferenceRule[Contains[W.`'5'`.T], Exists[Digit]].isValid
  }

  property("Exists ==> NonEmpty") = secure {
    InferenceRule[Exists[Digit], NonEmpty].isValid
  }

  property("NonEmpty =!> Exists") = secure {
    illTyped("InferenceRule[NonEmpty, Exists[Digit]]", "could not find.*InferenceRule.*")
    true
  }

  property("Head[A] ==> Head[B]") = secure {
    InferenceRule[Head[Digit], Head[LetterOrDigit]].isValid
  }

  property("Head[A] ==> Exists[A]") = secure {
    InferenceRule[Head[Digit], Exists[Digit]].isValid
  }

  property("Exists[A] =!> Head[A]") = secure {
    illTyped("InferenceRule[Exists[Digit], Head[Digit]]")
    true
  }

  property("Index[N, A] ==> Index[N, B]") = secure {
    InferenceRule[Index[_1, Letter], Index[_1, LetterOrDigit]].isValid
  }

  property("Index ==> Exists") = secure {
    InferenceRule[Index[W.`1`.T, LowerCase], Exists[LowerCase]].isValid
  }

  property("Last[A] ==> Last[B]") = secure {
    InferenceRule[Last[Letter], Last[LetterOrDigit]].isValid
  }

  property("Last ==> Exists") = secure {
    InferenceRule[Last[Whitespace], Exists[Whitespace]].isValid
  }

  property("Last ==> NonEmpty") = secure {
    InferenceRule[Last[Whitespace], NonEmpty].isValid
  }

  property("NonEmpty =!> Last") = secure {
    illTyped("InferenceRule[NonEmpty, Last[Whitespace]]")
    true
  }

  property("Size[A] ==> Size[B]") = secure {
    InferenceRule[Size[Greater[_5]], Size[Greater[_4]]].isValid
  }
}
