package eu.timepit.refined

import _root_.scodec._
import _root_.scodec.bits.BitVector
import eu.timepit.refined.api.RefinedType

package object scodec {

  implicit def refTypeCodec[FTP, T](
    implicit
    codec: Codec[T], rt: RefinedType.AuxT[FTP, T]
  ): Codec[FTP] =
    new Codec[FTP] {
      override def sizeBound: SizeBound =
        codec.sizeBound

      override def decode(bits: BitVector): Attempt[DecodeResult[FTP]] =
        codec.decode(bits).flatMap { t =>
          rt.refine(t.value) match {
            case Right(tp) => Attempt.successful(DecodeResult(tp, t.remainder))
            case Left(err) => Attempt.failure(Err(err))
          }
        }

      override def encode(value: FTP): Attempt[BitVector] =
        codec.encode(rt.unwrap(value))
    }
}
