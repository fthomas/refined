package eu.timepit.refined

import eu.timepit.refined.generic._
import eu.timepit.refined.string.StartsWith
import org.scalacheck.Prop._
import org.scalacheck.Properties

class GenericInferenceSpec extends Properties("GenericInference") {

  property("Equal ==> StartsWith") = secure {
    InferenceRule[Equal[W.`"abcd"`.T], StartsWith[W.`"ab"`.T]].isValid
  }

  property("Equal =!> StartsWith") = secure {
    InferenceRule[Equal[W.`"abcd"`.T], StartsWith[W.`"cd"`.T]].notValid
  }
}
