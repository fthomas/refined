package eu.timepit.refined

import eu.timepit.refined.api.{ RefinedType, RefType, Validate }
import org.scalacheck.{ Arbitrary, Gen, Prop }

package object scalacheck {

  def arbitraryRefType[F[_, _], T, P](gen: Gen[T])(implicit rt: RefType[F]): Arbitrary[F[T, P]] =
    Arbitrary(gen.map(rt.unsafeWrap))

  @deprecated("", "0.6.0")
  def checkArbitraryRefType[F[_, _], T, P](implicit arb: Arbitrary[F[T, P]], rt: RefType[F], v: Validate[T, P]): Prop =
    Prop.forAll((tp: F[T, P]) => v.isValid(rt.unwrap(tp)))

  def checkArbitraryRefinedType[FTP](implicit arb: Arbitrary[FTP], rt: RefinedType[FTP]): Prop =
    Prop.forAll((tp: FTP) => rt.validate.isValid(rt.unwrap(tp)))
}
