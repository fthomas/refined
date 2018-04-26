package eu.timepit.refined.types

import eu.timepit.refined.W
import eu.timepit.refined.api.{Refined, RefinedType, RefinedTypeOps}
import eu.timepit.refined.collection.{MaxSize, NonEmpty}
import eu.timepit.refined.string.MatchesRegex
import shapeless.Witness

/** Module for `String` refined types. */
object string {

  /** A `String` with length less than or equal to `N`. */
  type FiniteString[N] = String Refined MaxSize[N]

  object FiniteString {
    class FiniteStringOps[N <: Int](
        implicit
        rt: RefinedType.AuxT[FiniteString[N], String],
        wn: Witness.Aux[N]
    ) extends RefinedTypeOps[FiniteString[N], String] {

      /** The maximum length of a `FiniteString[N]`. */
      final val maxLength: N = wn.value

      /**
       * Creates a `FiniteString[N]` from `t` by truncating it
       * if it is longer than `N`.
       */
      def truncate(t: String): FiniteString[N] =
        Refined.unsafeApply(t.substring(0, math.min(t.length, maxLength)))
    }

    /** Creates a "companion object" for `FiniteString[N]` with a fixed `N`. */
    def apply[N <: Int](
        implicit
        rt: RefinedType.AuxT[FiniteString[N], String],
        wn: Witness.Aux[N]
    ): FiniteStringOps[N] = new FiniteStringOps[N]
  }

  /** A `String` that is not empty. */
  type NonEmptyString = String Refined NonEmpty

  object NonEmptyString extends RefinedTypeOps[NonEmptyString, String]

  // scalastyle:off no.whitespace.after.left.bracket
  /**
   * A `String` that contains no leading or trailing whitespace.
   *
   * Note that a line separator (`\u2028') is not considered whitespace for the
   * purposes of trimming.
   */
  type TrimmedString = String Refined MatchesRegex[
    W.`"""^(?![\\x{0000}-\\x{0020}])(?s:.*)(?<![\\x{0000}-\\x{0020}])"""`.T]
  // scalastyle:on no.whitespace.after.left.bracket

  object TrimmedString extends RefinedTypeOps[TrimmedString, String] {

    /**
     * Trim a string into a TrimmedString by removing leading and trailing
     * whitespace.
     *
     * Example: {{{
     * scala> import eu.timepit.refined.types.string._
     *
     * scala> TrimmedString.trim(" \n a b c ")
     * res0: TrimmedString = a b c
     * }}}
     */
    def trim(s: String): TrimmedString = Refined.unsafeApply(s.trim)
  }

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
