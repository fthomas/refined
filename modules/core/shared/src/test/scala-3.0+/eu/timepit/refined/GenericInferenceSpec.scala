package eu.timepit.refined

import eu.timepit.refined.api.Inference
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.string.StartsWith
import org.scalacheck.Prop._
import org.scalacheck.Properties

class GenericInferenceSpec extends Properties("GenericInference") {

  property("""Equal["abcd"] ==> StartsWith["ab"]""") = secure {
    Inference[Equal["abcd"], StartsWith["ab"]].isValid
  }

  property("""Equal["abcd"] =!> StartsWith["cd"]""") = secure {
    Inference[Equal["abcd"], StartsWith["cd"]].notValid
  }

  property("Equal[10] ==> Greater[5]") = secure {
    Inference[Equal[10], Greater[5]].isValid
  }

  property("Equal[5] =!> Greater[10]") = secure {
    Inference[Equal[5], Greater[10]].notValid
  }
}
