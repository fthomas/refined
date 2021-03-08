package eu.timepit.refined

import eu.timepit.refined.api.Inference
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class StringInferenceSpec extends Properties("StringInference") {

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

  property("UUID ==> NonEmpty ") = secure {
    Inference[Uuid, NonEmpty].isValid
  }

  property("Url ==> NonEmpty ") = secure {
    Inference[Url, NonEmpty].isValid
  }

  property("Xml ==> NonEmpty ") = secure {
    Inference[Xml, NonEmpty].isValid
  }

  property("ValidByte ==> NonEmpty ") = secure {
    Inference[ValidByte, NonEmpty].isValid
  }

  property("ValidShort ==> NonEmpty ") = secure {
    Inference[ValidShort, NonEmpty].isValid
  }

  property("ValidInt ==> NonEmpty ") = secure {
    Inference[ValidInt, NonEmpty].isValid
  }
  property("ValidLong ==> NonEmpty ") = secure {
    Inference[ValidLong, NonEmpty].isValid
  }

  property("ValidFloat ==> NonEmpty ") = secure {
    Inference[ValidFloat, NonEmpty].isValid
  }

  property("ValidDouble ==> NonEmpty ") = secure {
    Inference[ValidDouble, NonEmpty].isValid
  }

  property("ValidBigInt ==> NonEmpty ") = secure {
    Inference[ValidBigInt, NonEmpty].isValid
  }

  property("ValidBigDecimal ==> NonEmpty ") = secure {
    Inference[ValidBigDecimal, NonEmpty].isValid
  }

}
