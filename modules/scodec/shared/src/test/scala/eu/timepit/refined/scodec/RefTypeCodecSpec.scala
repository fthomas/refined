package eu.timepit.refined.scodec

import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scodec._
import scodec.bits._
import scodec.codecs.int8

class RefTypeCodecSpec extends Properties("RefTypeCodec") {

  implicit val intCodec: Codec[Int] = int8

  property("decode success") = secure {
    Codec[PosInt].decode(bin"00000101") ?=
      Attempt.successful(DecodeResult(PosInt.unsafeFrom(5), BitVector.empty))
  }

  property("decode failure") = secure {
    Codec[PosInt].decode(bin"10000101") ?=
      Attempt.failure(Err("Predicate failed: (-123 > 0)."))
  }

  property("encode success") = secure {
    Codec[PosInt].encode(PosInt.unsafeFrom(5)) ?=
      Attempt.successful(bin"00000101")
  }

  property("sizeBound") = secure {
    Codec[PosInt].sizeBound ?= intCodec.sizeBound
  }
}
