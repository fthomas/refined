package eu.timepit.refined

import eu.timepit.refined.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class StringInferenceSpec extends Properties("StringInference") {

  property("EndsWith[cde] ==> EndsWith[de]") = secure {
    InferenceRule[EndsWith[W.`"cde"`.T], EndsWith[W.`"de"`.T]].isValid
  }

  property("EndsWith[de] =!> EndsWith[cde]") = secure {
    InferenceRule[EndsWith[W.`"de"`.T], EndsWith[W.`"cde"`.T]].notValid
  }

  property("StartsWith[cde] ==> StartsWith[cd]") = secure {
    InferenceRule[StartsWith[W.`"cde"`.T], StartsWith[W.`"cd"`.T]].isValid
  }

  property("StartsWith[cde] =!> StartsWith[de]") = secure {
    InferenceRule[StartsWith[W.`"cde"`.T], StartsWith[W.`"de"`.T]].notValid
  }
}
