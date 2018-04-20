package eu.timepit.refined.types

import eu.timepit.refined.W
import eu.timepit.refined.api.{Refined, RefinedType, RefinedTypeOps}
import eu.timepit.refined.collection.{MaxSize, NonEmpty}
import eu.timepit.refined.internal.AsValueOf
import eu.timepit.refined.string.MatchesRegex

/** Module for `String` refined types. */
object string {

  /** A `String` with length less than or equal to `N`. */
  type FiniteString[N] = String Refined MaxSize[N]

  object FiniteString {
    class FiniteStringOps[N](
        implicit
        rt: RefinedType.AuxT[FiniteString[N], String],
        vn: AsValueOf[N, Int]
    ) extends RefinedTypeOps[FiniteString[N], String] {

      /** The maximum length of a `FiniteString[N]`. */
      final val maxLength: Int =
        vn.snd

      /**
       * Creates a `FiniteString[N]` from `t` by truncating it
       * if it is longer than `N`.
       */
      def truncate(t: String): FiniteString[N] =
        Refined.unsafeApply(t.substring(0, math.min(t.length, maxLength)))
    }

    /** Creates a "companion object" for `FiniteString[N]` with a fixed `N`. */
    def apply[N](
        implicit
        rt: RefinedType.AuxT[FiniteString[N], String],
        vn: AsValueOf[N, Int]
    ): FiniteStringOps[N] = new FiniteStringOps[N]
  }

  /** A `String` that is not empty. */
  type NonEmptyString = String Refined NonEmpty

  object NonEmptyString extends RefinedTypeOps[NonEmptyString, String]

  /** A `String` that contains no leading or trailing whitespace. */
  type TrimmedString = String Refined MatchesRegex[W.`"""^(?!\\s).*(?<!\\s)"""`.T]

  object TrimmedString extends RefinedTypeOps[TrimmedString, String]

  /** A `String` representing a hexadecimal number */
  type HexStringSpec = MatchesRegex[W.`"""^(([0-9a-f]+)|([0-9A-F]+))$"""`.T]
  type HexString = String Refined HexStringSpec
  object HexString extends RefinedTypeOps[HexString, String]
}

trait StringTypes {
  final type FiniteString[N] = string.FiniteString[N]
  final val FiniteString = string.FiniteString

  final type NonEmptyString = string.NonEmptyString
  final val NonEmptyString = string.NonEmptyString

  final type TrimmedString = string.TrimmedString
  final val TrimmedString = string.TrimmedString

  final type HexString = string.HexString
  final val HexString = string.HexString
}
