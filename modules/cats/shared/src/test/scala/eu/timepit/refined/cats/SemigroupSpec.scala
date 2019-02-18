package eu.timepit.refined.cats

import cats.implicits._
import eu.timepit.refined.types.numeric._
import org.scalacheck.Prop.secure
import org.scalacheck.Properties

class SemigroupSpec extends Properties("Semigroup") {

  property("Positive Integral types overflow") =
    secure { (PosByte.MaxValue |+| PosByte.unsafeFrom(1)) === PosByte.unsafeFrom(1) } &&
      secure { (PosShort.MaxValue |+| PosShort.unsafeFrom(1)) === PosShort.unsafeFrom(1) } &&
      secure { (PosInt.MaxValue |+| PosInt.unsafeFrom(1)) === PosInt.unsafeFrom(1) } &&
      secure { (PosLong.MaxValue |+| PosLong.unsafeFrom(1)) === PosLong.unsafeFrom(1) }

  property("NonNegative Integral types overflow") =
    secure { (NonNegByte.MaxValue |+| NonNegByte.unsafeFrom(1)) === NonNegByte.unsafeFrom(0) } &&
      secure { (NonNegShort.MaxValue |+| NonNegShort.unsafeFrom(1)) === NonNegShort.unsafeFrom(0) } &&
      secure { (NonNegInt.MaxValue |+| NonNegInt.unsafeFrom(1)) === NonNegInt.unsafeFrom(0) } &&
      secure { (NonNegLong.MaxValue |+| NonNegLong.unsafeFrom(1)) === NonNegLong.unsafeFrom(0) }

  property("Negative Integral types overflow") =
    secure { (NegByte.MinValue |+| NegByte.unsafeFrom(-1)) === NegByte.unsafeFrom(-1) } &&
      secure { (NegShort.MinValue |+| NegShort.unsafeFrom(-1)) === NegShort.unsafeFrom(-1) } &&
      secure { (NegInt.MinValue |+| NegInt.unsafeFrom(-1)) === NegInt.unsafeFrom(-1) } &&
      secure { (NegLong.MinValue |+| NegLong.unsafeFrom(-1)) === NegLong.unsafeFrom(-1) }

}
