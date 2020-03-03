package eu.timepit.refined.scodec

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scodec._
import scodec.bits._
import scodec.codecs.int8
import shapeless.test.illTyped

class RefTypeCodecSpec extends Properties("RefTypeCodec") {

  type PosInt = Int Refined Positive
  implicit val intCodec = int8

  property("decode success") = secure {
    Codec.summon[PosInt].decode(bin"00000101") ?=
      Attempt.successful(DecodeResult(5: PosInt, BitVector.empty))
  }

  property("decode failure") = secure {
    Codec.summon[PosInt].decode(bin"10000101") ?=
      Attempt.failure(Err("Predicate failed: (-123 > 0)."))
  }

  property("encode success") = secure {
    Codec.summon[PosInt].encode(5) ?=
      Attempt.successful(bin"00000101")
  }

  property("encode failure") = wellTyped {
    illTyped("""Codec.summon[PosInt].encode(-5)""", "Predicate failed.*")
  }

  property("sizeBound") = secure {
    Codec.summon[PosInt].sizeBound ?= intCodec.sizeBound
  }
}
