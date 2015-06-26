package eu.timepit.refined

import eu.timepit.refined.boolean._
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class NumericInferenceSpec extends Properties("NumericInference") {

  property("Less[A] ==> Less[B]") = secure {
    InferenceRule[Less[W.`7.2`.T], Less[W.`7.5`.T]].isValid
  }

  property("Less[A] =!> Less[B]") = secure {
    InferenceRule[Less[W.`7.5`.T], Less[W.`7.2`.T]].notValid
  }

  property("LessEqual[A] ==> LessEqual[B]") = secure {
    InferenceRule[LessEqual[W.`7.2`.T], LessEqual[W.`7.5`.T]].isValid
  }

  property("LessEqual[A] ==> LessEqual[A]") = secure {
    InferenceRule[LessEqual[W.`1`.T], LessEqual[W.`1`.T]].isValid
  }

  property("LessEqual[A] =!> LessEqual[B]") = secure {
    InferenceRule[LessEqual[W.`7.5`.T], LessEqual[W.`7.2`.T]].notValid
  }

  property("Greater[A] ==> Greater[B]") = secure {
    InferenceRule[Greater[W.`7.5`.T], Greater[W.`7.2`.T]].isValid
  }

  property("Greater[A] =!> Greater[B]") = secure {
    InferenceRule[Greater[W.`7.2`.T], Greater[W.`7.5`.T]].notValid
  }

  property("GreaterEqual[A] ==> GreaterEqual[B]") = secure {
    InferenceRule[GreaterEqual[W.`7.5`.T], GreaterEqual[W.`7.2`.T]].isValid
  }

  property("GreaterEqual[A] ==> GreaterEqual[A]") = secure {
    InferenceRule[GreaterEqual[W.`1`.T], GreaterEqual[W.`1`.T]].isValid
  }

  property("GreaterEqual[A] =!> GreaterEqual[B]") = secure {
    InferenceRule[GreaterEqual[W.`7.2`.T], GreaterEqual[W.`7.5`.T]].notValid
  }

  property("Less[Nat] ==> Less[Nat]") = secure {
    InferenceRule[Less[_5], Less[_10]].isValid
  }

  property("Less[Nat] =!> Less[Nat]") = secure {
    InferenceRule[Less[_10], Less[_5]].notValid
  }

  property("Greater[Nat] ==> Greater[Nat]") = secure {
    InferenceRule[Greater[_10], Greater[_5]].isValid
  }

  property("Greater[Nat] =!> Greater[Nat]") = secure {
    InferenceRule[Greater[_5], Greater[_10]].notValid
  }

  property("Interval[Nat] ==> LessEqual[Nat]") = secure {
    InferenceRule[Interval[_5, _10], LessEqual[_11]].isValid
  }

  property("Interval[Nat] ==> GreaterEqual[Nat]") = secure {
    InferenceRule[Interval[_5, _10], GreaterEqual[_4]].isValid
  }
}
