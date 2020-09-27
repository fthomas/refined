package eu.timepit.refined

import eu.timepit.refined.api.Inference
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.string.StartsWith
import org.scalacheck.Prop._
import org.scalacheck.Properties

class GenericInferenceSpec extends Properties("GenericInference") {

  property("""Equal["abcd"] ==> StartsWith["ab"]""") = secure {
    Inference[Equal[W.`"abcd"`.T], StartsWith[W.`"ab"`.T]].isValid
  }

  property("""Equal["abcd"] =!> StartsWith["cd"]""") = secure {
    Inference[Equal[W.`"abcd"`.T], StartsWith[W.`"cd"`.T]].notValid
  }

  property("Equal[10] ==> Greater[5]") = secure {
    Inference[Equal[W.`10`.T], Greater[W.`5`.T]].isValid
  }

  property("Equal[5] =!> Greater[10]") = secure {
    Inference[Equal[W.`5`.T], Greater[W.`10`.T]].notValid
  }
}
