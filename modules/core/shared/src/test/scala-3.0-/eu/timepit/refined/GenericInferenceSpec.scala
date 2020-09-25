package eu.timepit.refined

import eu.timepit.refined.api.Inference
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.string.StartsWith
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.Nat

class GenericInferenceSpec extends Properties("GenericInference") {

  property("Equal[S1] ==> StartsWith[S2]") = secure {
    Inference[Equal[W.`"abcd"`.T], StartsWith[W.`"ab"`.T]].isValid
  }

  property("Equal[S1] =!> StartsWith[S2]") = secure {
    Inference[Equal[W.`"abcd"`.T], StartsWith[W.`"cd"`.T]].notValid
  }

  property("Equal[Nat] ==> Greater[I]") = secure {
    Inference[Equal[Nat._10], Greater[W.`5`.T]].isValid
  }

  property("Equal[Nat] =!> Greater[I]") = secure {
    Inference[Equal[Nat._5], Greater[W.`10`.T]].notValid
  }
}
