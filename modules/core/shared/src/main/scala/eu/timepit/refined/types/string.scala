package eu.timepit.refined.types

import eu.timepit.refined.api.{Refined, RefinedType, RefinedTypeOps}
import eu.timepit.refined.collection.{MaxSize, NonEmpty, Size}
import eu.timepit.refined.numeric.Interval
import eu.timepit.refined.string.{HexStringSpec, Trimmed}
import shapeless.Nat._1
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
       *
       * Example: {{{
       * scala> import eu.timepit.refined.W
       *      | import eu.timepit.refined.types.string.FiniteString
       *
       * scala> FiniteString[W.`3`.T].truncate("abcde")
       * res1: FiniteString[W.`3`.T] = abc
       * }}}
       */
      def truncate(t: String): FiniteString[N] =
        Refined.unsafeApply(t.substring(0, math.min(t.length, maxLength)))
    }

    /**
     * Creates a "companion object" for `FiniteString[N]` with a fixed `N`.
     *
     * Example: {{{
     * scala> import eu.timepit.refined.W
     *      | import eu.timepit.refined.types.string.FiniteString
     *
     * scala> val FString4 = FiniteString[W.`4`.T]
     * scala> FString4.from("abcd")
     * res1: Either[String, FiniteString[W.`4`.T]] = Right(abcd)
     * }}}
     */
    def apply[N <: Int](
        implicit
        rt: RefinedType.AuxT[FiniteString[N], String],
        wn: Witness.Aux[N]
    ): FiniteStringOps[N] = new FiniteStringOps[N]
  }

  /** A `String` that is not empty. */
  type NonEmptyString = String Refined NonEmpty

  object NonEmptyString extends RefinedTypeOps[NonEmptyString, String]

  /** A `String` that is not empty with length less than or equal to `N`. */
  type NonEmptyFiniteString[N] = String Refined Size[Interval.Closed[_1, N]]

  object NonEmptyFiniteString {
    class NonEmptyFiniteStringOps[N <: Int](
        implicit
        rt: RefinedType.AuxT[NonEmptyFiniteString[N], String],
        wn: Witness.Aux[N]
    ) extends RefinedTypeOps[NonEmptyFiniteString[N], String] {

      /** The maximum length of a `NonEmptyFiniteString[N]`. */
      final val maxLength: N = wn.value

      /**
       * Creates a `NonEmptyFiniteString[N]` from `t` by truncating it
       * if it is longer than `N`. Returns `None` if `t` is empty.
       *
       * Example: {{{
       * scala> import eu.timepit.refined.W
       *      | import eu.timepit.refined.types.string.NonEmptyFiniteString
       *
       * scala> NonEmptyFiniteString[W.`3`.T].truncate("abcde")
       * res1: Option[NonEmptyFiniteString[W.`3`.T]] = Some(abc)
       * }}}
       */
      def truncate(t: String): Option[NonEmptyFiniteString[N]] =
        if (t.isEmpty) None
        else Some(Refined.unsafeApply(t.substring(0, math.min(t.length, maxLength))))
    }

    /**
     * Creates a "companion object" for `NonEmptyFiniteString[N]` with a fixed `N`.
     *
     * Example: {{{
     * scala> import eu.timepit.refined.W
     *      | import eu.timepit.refined.types.string.NonEmptyFiniteString
     *
     * scala> val NEFString4 = NonEmptyFiniteString[W.`4`.T]
     * scala> NEFString4.from("abcd")
     * res1: Either[String, NonEmptyFiniteString[W.`4`.T]] = Right(abcd)
     * }}}
     */
    def apply[N <: Int](
        implicit
        rt: RefinedType.AuxT[NonEmptyFiniteString[N], String],
        wn: Witness.Aux[N]
    ): NonEmptyFiniteStringOps[N] = new NonEmptyFiniteStringOps[N]
  }

  /** A `String` that contains no leading or trailing whitespace. */
  type TrimmedString = String Refined Trimmed

  object TrimmedString extends RefinedTypeOps[TrimmedString, String] {

    /** Creates a `TrimmedString` from `s` by trimming it. */
    def trim(s: String): TrimmedString = Refined.unsafeApply(s.trim)
  }

  /** A `String` representing a hexadecimal number. */
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

trait StringTypesBinCompat1 {
  final type NonEmptyFiniteString[N] = string.NonEmptyFiniteString[N]
  final val NonEmptyFiniteString = string.NonEmptyFiniteString
}
