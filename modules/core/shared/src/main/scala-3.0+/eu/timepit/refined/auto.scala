package eu.timepit.refined

import eu.timepit.refined.api.{Inference, RefType, Refined, Validate}
import eu.timepit.refined.macros.Macros

import scala.language.implicitConversions
import scala.quoted.*

object auto {

  implicit inline def autoRefineV[T, P](inline t: T)(implicit
      inline v: Validate[T, P]
  ): Refined[T, P] = ${
    Macros.autoRefineV[T, P]('t, 'v)
  }

  implicit inline def autoInfer[T, A, B](inline ta: Refined[T, A])(implicit
      inline ir: Inference[A, B]
  ): Refined[T, B] = ${
    Macros.autoInfer[T, A, B]('ta, 'ir)
  }

  implicit def autoUnwrap[F[_, _], T, P](tp: F[T, P])(implicit rt: RefType[F]): T =
    rt.unwrap(tp)
}
