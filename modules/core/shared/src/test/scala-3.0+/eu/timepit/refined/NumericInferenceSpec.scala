package eu.timepit.refined

import eu.timepit.refined.api.Inference
import eu.timepit.refined.boolean._
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class NumericInferenceSpec extends Properties("NumericInference") {

  property("Less[5] ==> Less[10]") = secure {
    Inference[Less[5], Less[10]].isValid
  }

  property("Less[10] =!> Less[5]") = secure {
    Inference[Less[10], Less[5]].notValid
  }

  property("Less[7.2] ==> Less[7.5]") = secure {
    Inference[Less[7.2], Less[7.5]].isValid
  }

  property("Less[7.5] =!> Less[7.2]") = secure {
    Inference[Less[7.5], Less[7.2]].notValid
  }

  property("LessEqual[1] ==> LessEqual[1]") = secure {
    Inference[LessEqual[1], LessEqual[1]].isValid
  }

  property("LessEqual[7.2] ==> LessEqual[7.5]") = secure {
    Inference[LessEqual[7.2], LessEqual[7.5]].isValid
  }

  property("LessEqual[7.5] =!> LessEqual[7.2]") = secure {
    Inference[LessEqual[7.5], LessEqual[7.2]].notValid
  }

  property("Greater[10] ==> Greater[5]") = secure {
    Inference[Greater[10], Greater[5]].isValid
  }

  property("Greater[5] =!> Greater[10]") = secure {
    Inference[Greater[5], Greater[10]].notValid
  }

  property("Greater[7.5] ==> Greater[7.2]") = secure {
    Inference[Greater[7.5], Greater[7.2]].isValid
  }

  property("Greater[7.2] =!> Greater[7.5]") = secure {
    Inference[Greater[7.2], Greater[7.5]].notValid
  }

  property("GreaterEqual[1] ==> GreaterEqual[1]") = secure {
    Inference[GreaterEqual[1], GreaterEqual[1]].isValid
  }

  property("GreaterEqual[7.5] ==> GreaterEqual[7.2]") = secure {
    Inference[GreaterEqual[7.5], GreaterEqual[7.2]].isValid
  }

  property("GreaterEqual[7.2] =!> GreaterEqual[7.5]") = secure {
    Inference[GreaterEqual[7.2], GreaterEqual[7.5]].notValid
  }

  property("Greater[0] ==> GreaterEqual[0]") = secure {
    Inference[Greater[0], GreaterEqual[0]].isValid
  }

  property("Less[0] ==> LessEqual[0]") = secure {
    Inference[Less[0], LessEqual[0]].isValid
  }

  property("Interval.Closed[5, 10] ==> LessEqual[11]") = secure {
    Inference[Interval.Closed[5, 10], LessEqual[11]].isValid
  }
}
