package eu.timepit.refined

import eu.timepit.refined.api.RefType
import org.scalacheck.{ Arbitrary, Gen }

package object scalacheck {

  def arbitraryRefType[F[_, _], T, P](gen: Gen[T])(implicit rt: RefType[F]): Arbitrary[F[T, P]] =
    Arbitrary(gen.map(rt.unsafeWrap))
}
