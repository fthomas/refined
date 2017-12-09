package eu.timepit.refined.scodec.predicates

import eu.timepit.refined.api.Validate
import eu.timepit.refined.collection.Size
import eu.timepit.refined.internal.Resources
import scodec.bits.ByteVector

object byteVector extends ByteVectorValidate

private[refined] trait ByteVectorValidate {
  implicit def byteVectorSizeValidate[P, RP](
      implicit v: Validate.Aux[Long, P, RP]): Validate.Aux[ByteVector, Size[P], Size[v.Res]] =
    new Validate[ByteVector, Size[P]] {
      override type R = Size[v.Res]

      override def validate(t: ByteVector): Res = {
        val r = v.validate(t.size)
        r.as(Size(r))
      }

      override def showExpr(t: ByteVector): String =
        v.showExpr(t.size)

      override def showResult(t: ByteVector, r: Res): String = {
        val size = t.size
        val nested = v.showResult(size, r.detail.p)
        Resources.predicateTakingResultDetail(s"size($t) = $size", r, nested)
      }
    }
}
