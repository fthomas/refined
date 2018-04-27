package eu.timepit.refined

import eu.timepit.refined.api.Inference
import eu.timepit.refined.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class StringInferenceSpec extends Properties("StringInference") {

  property("EndsWith ==> EndsWith") = secure {
    Inference[EndsWith["cde"], EndsWith["de"]].isValid
  }

  property("EndsWith =!> EndsWith") = secure {
    Inference[EndsWith["de"], EndsWith["cde"]].notValid
  }

  property("StartsWith ==> StartsWith") = secure {
    Inference[StartsWith["cde"], StartsWith["cd"]].isValid
  }

  property("StartsWith =!> StartsWith") = secure {
    Inference[StartsWith["cde"], StartsWith["de"]].notValid
  }
}
