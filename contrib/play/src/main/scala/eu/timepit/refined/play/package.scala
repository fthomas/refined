package eu.timepit.refined

import eu.timepit.refined.api._
import _root_.play.api.libs.json._

import scala.language.higherKinds

package object play {
  implicit def writeRefined[T, P, F[_, _]](
    implicit
    writesT: Writes[T],
    reftype: RefType[F]
  ): Writes[F[T, P]] = Writes(value => writesT.writes(reftype.unwrap(value)))

  implicit def readRefined[T, P, F[_, _]](
    implicit
    readsT: Reads[T],
    reftype: RefType[F],
    validate: Validate[T, P]
  ): Reads[F[T, P]] = Reads(jsValue => readsT.reads(jsValue).flatMap { valueT =>
    reftype.refine[P](valueT) match {
      case Right(valueP) => JsSuccess(valueP)
      case Left(error) => JsError(error)
    }
  })
}
