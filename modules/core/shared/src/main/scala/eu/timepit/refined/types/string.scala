package eu.timepit.refined.types

import eu.timepit.refined.W
import eu.timepit.refined.api.{Refined, RefinedType, RefinedTypeOps}
import eu.timepit.refined.collection.{MaxSize, NonEmpty}
import eu.timepit.refined.string.MatchesRegex

/** Module for `String` refined types. */
object string {

  /** A `String` with length less than or equal to `N`. */
  type FiniteString[N] = String Refined MaxSize[N]

  object FiniteString {

    /** Creates a companion object for `FiniteString[N]` with a fixed `N`. */
    def apply[N](
        implicit rt: RefinedType.AuxT[FiniteString[N], String]
    ): RefinedTypeOps[FiniteString[N], String] =
      new RefinedTypeOps[FiniteString[N], String]
  }

  /** A `String` that is not empty. */
  type NonEmptyString = String Refined NonEmpty

  object NonEmptyString extends RefinedTypeOps[NonEmptyString, String]

  /** A `String` that contains no leading or trailing whitespace. */
  type TrimmedString = String Refined MatchesRegex[W.`"""^(?!\\s).*(?<!\\s)"""`.T]

  object TrimmedString extends RefinedTypeOps[TrimmedString, String]
}

trait StringTypes {
  final type FiniteString[N] = string.FiniteString[N]
  final val FiniteString = string.FiniteString

  final type NonEmptyString = string.NonEmptyString
  final val NonEmptyString = string.NonEmptyString

  final type TrimmedString = string.TrimmedString
  final val TrimmedString = string.TrimmedString
}
