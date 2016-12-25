package eu.timepit.refined

import _root_.scodec._
import _root_.scodec.bits.BitVector
import eu.timepit.refined.api.{RefType, Validate}

package object scodec {

  implicit def refTypeCodec[F[_, _], T, P](
      implicit codec: Codec[T],
      refType: RefType[F],
      validate: Validate[T, P]
  ): Codec[F[T, P]] =
    new Codec[F[T, P]] {
      override def sizeBound: SizeBound =
        codec.sizeBound

      override def decode(bits: BitVector): Attempt[DecodeResult[F[T, P]]] =
        codec.decode(bits).flatMap { t =>
          refType.refine[P](t.value) match {
            case Right(tp) => Attempt.successful(DecodeResult(tp, t.remainder))
            case Left(err) => Attempt.failure(Err(err))
          }
        }

      override def encode(value: F[T, P]): Attempt[BitVector] =
        codec.encode(refType.unwrap(value))
    }
}
