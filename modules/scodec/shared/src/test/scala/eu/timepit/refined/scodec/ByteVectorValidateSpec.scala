package eu.timepit.refined.scodec

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.W
import eu.timepit.refined.collection.Size
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.scodec.byteVector._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scodec.bits.ByteVector

class ByteVectorValidateSpec extends Properties("ByteVectorValidate") {

  property("Size.isValid") = forAll { bytes: Array[Byte] =>
    val bv = ByteVector(bytes)
    isValid[Size[Greater[W.`5L`.T]]](bv) ?= (bv.size > 5L)
  }

  property("Size.showExpr") = secure {
    showExpr[Size[Equal[W.`5L`.T]]](ByteVector.fromValidHex("0xdeadbeef")) ?= "(4 == 5)"
  }

  property("Size.showResult") = secure {
    showResult[Size[Equal[W.`5L`.T]]](ByteVector.fromValidHex("0xdeadbeef")) ?=
      "Predicate taking size(ByteVector(4 bytes, 0xdeadbeef)) = 4 failed: Predicate failed: (4 == 5)."
  }

}
