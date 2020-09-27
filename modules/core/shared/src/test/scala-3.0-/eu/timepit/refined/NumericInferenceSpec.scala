package eu.timepit.refined

import eu.timepit.refined.api.Inference
import eu.timepit.refined.boolean._
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class NumericInferenceSpec extends Properties("NumericInference") {

  property("Less[5] ==> Less[10]") = secure {
    Inference[Less[W.`5`.T], Less[W.`10`.T]].isValid
  }

  property("Less[10] =!> Less[5]") = secure {
    Inference[Less[W.`10`.T], Less[W.`5`.T]].notValid
  }

  property("Less[7.2] ==> Less[7.5]") = secure {
    Inference[Less[W.`7.2`.T], Less[W.`7.5`.T]].isValid
  }

  property("Less[7.5] =!> Less[7.2]") = secure {
    Inference[Less[W.`7.5`.T], Less[W.`7.2`.T]].notValid
  }

  property("LessEqual[1] ==> LessEqual[1]") = secure {
    Inference[LessEqual[W.`1`.T], LessEqual[W.`1`.T]].isValid
  }

  property("LessEqual[7.2] ==> LessEqual[7.5]") = secure {
    Inference[LessEqual[W.`7.2`.T], LessEqual[W.`7.5`.T]].isValid
  }

  property("LessEqual[7.5] =!> LessEqual[7.2]") = secure {
    Inference[LessEqual[W.`7.5`.T], LessEqual[W.`7.2`.T]].notValid
  }

  property("Greater[10] ==> Greater[5]") = secure {
    Inference[Greater[W.`10`.T], Greater[W.`5`.T]].isValid
  }

  property("Greater[5] =!> Greater[10]") = secure {
    Inference[Greater[W.`5`.T], Greater[W.`10`.T]].notValid
  }

  property("Greater[7.5] ==> Greater[7.2]") = secure {
    Inference[Greater[W.`7.5`.T], Greater[W.`7.2`.T]].isValid
  }

  property("Greater[7.2] =!> Greater[7.5]") = secure {
    Inference[Greater[W.`7.2`.T], Greater[W.`7.5`.T]].notValid
  }

  property("GreaterEqual[1] ==> GreaterEqual[1]") = secure {
    Inference[GreaterEqual[W.`1`.T], GreaterEqual[W.`1`.T]].isValid
  }

  property("GreaterEqual[7.5] ==> GreaterEqual[7.2]") = secure {
    Inference[GreaterEqual[W.`7.5`.T], GreaterEqual[W.`7.2`.T]].isValid
  }

  property("GreaterEqual[7.2] =!> GreaterEqual[7.5]") = secure {
    Inference[GreaterEqual[W.`7.2`.T], GreaterEqual[W.`7.5`.T]].notValid
  }

  property("Greater[0] ==> GreaterEqual[0]") = secure {
    Inference[Greater[W.`0`.T], GreaterEqual[W.`0`.T]].isValid
  }

  property("Less[0] ==> LessEqual[0]") = secure {
    Inference[Less[W.`0`.T], LessEqual[W.`0`.T]].isValid
  }

  property("Interval.Closed[5, 10] ==> LessEqual[11]") = secure {
    Inference[Interval.Closed[W.`5`.T, W.`10`.T], LessEqual[W.`11`.T]].isValid
  }
}
