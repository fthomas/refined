package eu.timepit.refined

//import eu.timepit.refined.api.Inference
import eu.timepit.refined.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class StringInferenceSpec extends Properties("StringInference") {
  import shim.Inference

  property("EndsWith ==> EndsWith") = secure {
    Inference[EndsWith[W.`"cde"`.T], EndsWith[W.`"de"`.T]].isValid
  }

  property("EndsWith =!> EndsWith") = secure {
    Inference[EndsWith[W.`"de"`.T], EndsWith[W.`"cde"`.T]].notValid
  }

  property("StartsWith ==> StartsWith") = secure {
    Inference[StartsWith[W.`"cde"`.T], StartsWith[W.`"cd"`.T]].isValid
  }

  property("StartsWith =!> StartsWith") = secure {
    Inference[StartsWith[W.`"cde"`.T], StartsWith[W.`"de"`.T]].notValid
  }
}
