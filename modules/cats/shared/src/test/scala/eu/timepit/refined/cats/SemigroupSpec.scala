package eu.timepit.refined.cats

import cats.implicits._
import eu.timepit.refined.types.numeric._
import org.scalacheck.Prop.secure
import org.scalacheck.Properties

class SemigroupSpec extends Properties("Semigroup") {

  property("Positive Integral types overflow") =
    secure { (PosByte.MaxValue |+| PosByte.unsafeFrom(1)) === PosByte.MinValue } &&
      secure { (PosShort.MaxValue |+| PosShort.unsafeFrom(1)) === PosShort.MinValue } &&
      secure { (PosInt.MaxValue |+| PosInt.unsafeFrom(1)) === PosInt.MinValue } &&
      secure { (PosLong.MaxValue |+| PosLong.unsafeFrom(1)) === PosLong.MinValue }

  property("NonNegative Integral types overflow") =
    secure { (NonNegByte.MaxValue |+| NonNegByte.unsafeFrom(1)) === NonNegByte.MinValue } &&
      secure { (NonNegShort.MaxValue |+| NonNegShort.unsafeFrom(1)) === NonNegShort.MinValue } &&
      secure { (NonNegInt.MaxValue |+| NonNegInt.unsafeFrom(1)) === NonNegInt.MinValue } &&
      secure { (NonNegLong.MaxValue |+| NonNegLong.unsafeFrom(1)) === NonNegLong.MinValue }

  property("Negative Integral types overflow") =
    secure { (NegByte.MinValue |+| NegByte.unsafeFrom(-1)) === NegByte.MaxValue } &&
      secure { (NegShort.MinValue |+| NegShort.unsafeFrom(-1)) === NegShort.MaxValue } &&
      secure { (NegInt.MinValue |+| NegInt.unsafeFrom(-1)) === NegInt.MaxValue } &&
      secure { (NegLong.MinValue |+| NegLong.unsafeFrom(-1)) === NegLong.MaxValue }

}
