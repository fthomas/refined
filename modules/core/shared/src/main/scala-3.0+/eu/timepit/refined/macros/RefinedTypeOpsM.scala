package eu.timepit.refined.macros

import hearth.*
import eu.timepit.refined.api.{Refined, Validate}

import scala.quoted.*

trait RefinedTypeOpsM[FTP, T] {

  inline def apply[P](
      inline t: T
  )(implicit inline ev: Refined[T, P] =:= FTP, inline v: Validate[T, P]): FTP =
    ${ Macros.applyRef[FTP, T, P]('t, 'v) }
}
