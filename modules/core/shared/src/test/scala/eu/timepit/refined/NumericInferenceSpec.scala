package eu.timepit.refined

import eu.timepit.refined.api.Inference
import eu.timepit.refined.boolean._
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class NumericInferenceSpec extends Properties("NumericInference") {

  property("Less[A] ==> Less[B]") = secure {
    Inference[Less[7.2], Less[7.5]].isValid
  }

  property("Less[A] =!> Less[B]") = secure {
    Inference[Less[7.5], Less[7.2]].notValid
  }

  property("LessEqual[A] ==> LessEqual[B]") = secure {
    Inference[LessEqual[7.2], LessEqual[7.5]].isValid
  }

  // Does not compile on 2.10 without a warning.
  /*
  property("LessEqual[A] ==> LessEqual[A]") = secure {
    Inference[LessEqual[1], LessEqual[1]].isValid
  }
   */

  property("LessEqual[A] =!> LessEqual[B]") = secure {
    Inference[LessEqual[7.5], LessEqual[7.2]].notValid
  }

  property("Greater[A] ==> Greater[B]") = secure {
    Inference[Greater[7.5], Greater[7.2]].isValid
  }

  property("Greater[A] =!> Greater[B]") = secure {
    Inference[Greater[7.2], Greater[7.5]].notValid
  }

  property("GreaterEqual[A] ==> GreaterEqual[B]") = secure {
    Inference[GreaterEqual[7.5], GreaterEqual[7.2]].isValid
  }

  // Does not compile on 2.10 without a warning.
  /*
  property("GreaterEqual[A] ==> GreaterEqual[A]") = secure {
    Inference[GreaterEqual[1], GreaterEqual[1]].isValid
  }
   */

  property("GreaterEqual[A] =!> GreaterEqual[B]") = secure {
    Inference[GreaterEqual[7.2], GreaterEqual[7.5]].notValid
  }

  property("Less[Nat] ==> Less[Nat]") = secure {
    Inference[Less[_5], Less[_10]].isValid
  }

  property("Less[Nat] =!> Less[Nat]") = secure {
    Inference[Less[_10], Less[_5]].notValid
  }

  property("Less[A] ==> Less[Nat]") = secure {
    Inference[Less[5], Less[_10]].isValid
  }

  property("Less[A] =!> Less[Nat]") = secure {
    Inference[Less[10], Less[_5]].notValid
  }

  property("Greater[Nat] ==> Greater[Nat]") = secure {
    Inference[Greater[_10], Greater[_5]].isValid
  }

  property("Greater[Nat] =!> Greater[Nat]") = secure {
    Inference[Greater[_5], Greater[_10]].notValid
  }

  property("Greater[A] ==> Greater[Nat]") = secure {
    Inference[Greater[10], Greater[_5]].isValid
  }

  property("Greater[A] =!> Greater[Nat]") = secure {
    Inference[Greater[5], Greater[_10]].notValid
  }

  property("Interval[Nat] ==> LessEqual[Nat]") = secure {
    Inference[Interval.Closed[_5, _10], LessEqual[_11]].isValid
  }

  property("Greater[A] ==> GreaterEqual[A]") = secure {
    Inference[Greater[0], GreaterEqual[0]].isValid
  }

  property("Less[A] ==> LessEqual[A]") = secure {
    Inference[Less[0], LessEqual[0]].isValid
  }

  /*
  property("Interval.Closed[Nat] ==> GreaterEqual[Nat]") = secure {
    Inference[Interval.Closed[_5, _10], GreaterEqual[_4]].isValid
  }

  property("Equal[Nat] ==> Greater[A]") = secure {
    Inference[Equal[_10], Greater[5]].isValid
  }

  property("Equal[Nat] =!> Greater[A]") = secure {
    Inference[Equal[_5], Greater[10]].notValid
  }
 */
}
