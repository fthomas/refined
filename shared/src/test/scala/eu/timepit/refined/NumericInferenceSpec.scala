package eu.timepit.refined

import eu.timepit.refined.api.Inference
import eu.timepit.refined.boolean._
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class NumericInferenceSpec extends Properties("NumericInference") {

  property("Less[A] ==> Less[B]") = secure {
    Inference[Less[W.`7.2`.T], Less[W.`7.5`.T]].isValid
  }

  property("Less[A] =!> Less[B]") = secure {
    Inference[Less[W.`7.5`.T], Less[W.`7.2`.T]].notValid
  }

  property("LessEqual[A] ==> LessEqual[B]") = secure {
    Inference[LessEqual[W.`7.2`.T], LessEqual[W.`7.5`.T]].isValid
  }

  property("LessEqual[A] ==> LessEqual[A]") = secure {
    Inference[LessEqual[W.`1`.T], LessEqual[W.`1`.T]].isValid
  }

  property("LessEqual[A] =!> LessEqual[B]") = secure {
    Inference[LessEqual[W.`7.5`.T], LessEqual[W.`7.2`.T]].notValid
  }

  property("Greater[A] ==> Greater[B]") = secure {
    Inference[Greater[W.`7.5`.T], Greater[W.`7.2`.T]].isValid
  }

  property("Greater[A] =!> Greater[B]") = secure {
    Inference[Greater[W.`7.2`.T], Greater[W.`7.5`.T]].notValid
  }

  property("GreaterEqual[A] ==> GreaterEqual[B]") = secure {
    Inference[GreaterEqual[W.`7.5`.T], GreaterEqual[W.`7.2`.T]].isValid
  }

  property("GreaterEqual[A] ==> GreaterEqual[A]") = secure {
    Inference[GreaterEqual[W.`1`.T], GreaterEqual[W.`1`.T]].isValid
  }

  property("GreaterEqual[A] =!> GreaterEqual[B]") = secure {
    Inference[GreaterEqual[W.`7.2`.T], GreaterEqual[W.`7.5`.T]].notValid
  }

  property("Less[Nat] ==> Less[Nat]") = secure {
    Inference[Less[_5], Less[_10]].isValid
  }

  property("Less[Nat] =!> Less[Nat]") = secure {
    Inference[Less[_10], Less[_5]].notValid
  }

  property("Less[A] ==> Less[Nat]") = secure {
    Inference[Less[W.`5`.T], Less[_10]].isValid
  }

  property("Less[A] =!> Less[Nat]") = secure {
    Inference[Less[W.`10`.T], Less[_5]].notValid
  }

  property("Greater[Nat] ==> Greater[Nat]") = secure {
    Inference[Greater[_10], Greater[_5]].isValid
  }

  property("Greater[Nat] =!> Greater[Nat]") = secure {
    Inference[Greater[_5], Greater[_10]].notValid
  }

  property("Greater[A] ==> Greater[Nat]") = secure {
    Inference[Greater[W.`10`.T], Greater[_5]].isValid
  }

  property("Greater[A] =!> Greater[Nat]") = secure {
    Inference[Greater[W.`5`.T], Greater[_10]].notValid
  }

  property("Interval[Nat] ==> LessEqual[Nat]") = secure {
    Inference[Interval[_5, _10], LessEqual[_11]].isValid
  }

  property("Interval[Nat] ==> GreaterEqual[Nat]") = secure {
    Inference[Interval[_5, _10], GreaterEqual[_4]].isValid
  }

  property("Equal[Nat] ==> Greater[A]") = secure {
    Inference[Equal[_10], Greater[W.`5`.T]].isValid
  }

  property("Equal[Nat] =!> Greater[A]") = secure {
    Inference[Equal[_5], Greater[W.`10`.T]].notValid
  }
}
