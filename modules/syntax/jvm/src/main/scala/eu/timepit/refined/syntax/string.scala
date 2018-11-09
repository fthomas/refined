package eu.timepit.refined
package syntax

import contextual._
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.{NonEmptyString, TrimmedString}

trait NonEmptyStringSyntax {
  implicit class NonEmptyStringOps(nes: NonEmptyString) {
    def *(n: Int): NonEmptyString = NonEmptyString.unsafeFrom(nes.value * n)
    def +(s: String): NonEmptyString = NonEmptyString.unsafeFrom(nes.value + s)
    def size: PosInt = PosInt.unsafeFrom(nes.value.size)
    def trim: TrimmedString = TrimmedString.unsafeFrom(nes.value.trim)
  }
}

trait NonEmptyStringInterpolation {
  // $COVERAGE-OFF$ SCoverage does not report this correctly
  implicit class NonEmptyStringContext(sc: StringContext) {
    val nes = Prefix(NonEmptyStringInterpolation.NESInterpolator, sc)
  }
  // $COVERAGE-ON$
}

object NonEmptyStringInterpolation {
  object NESInterpolator extends Interpolator {
    type Output = NonEmptyString

    def contextualize(interpolation: StaticInterpolation): Seq[ContextType] = {
      val lit @ Literal(_, s) = interpolation.parts.head
      NonEmptyString
        .from(s)
        .fold(
          _ => interpolation.abort(lit, 0, "The string was empty"),
          _ => Nil
        )
    }

    def evaluate(interpolation: RuntimeInterpolation): NonEmptyString =
      NonEmptyString.unsafeFrom(interpolation.literals.head)
  }
}
