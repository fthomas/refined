package eu.timepit.refined.types

import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.numeric.{Negative, NonNegative, NonPositive, Positive}

/** Module for numeric refined types. */
object numeric extends NumericTypes

trait NumericTypes {

  /** An `Int` in the range from 1 to `Int.MaxValue`. */
  type PosInt = Int Refined Positive

  object PosInt extends RefinedTypeOps.Numeric[PosInt, Int]

  /** An `Int` in the range from 0 to `Int.MaxValue`. */
  type NonNegInt = Int Refined NonNegative

  object NonNegInt extends RefinedTypeOps.Numeric[NonNegInt, Int]

  /** An `Int` in the range from `Int.MinValue` to -1. */
  type NegInt = Int Refined Negative

  object NegInt extends RefinedTypeOps.Numeric[NegInt, Int]

  /** An `Int` in the range from `Int.MinValue` to 0. */
  type NonPosInt = Int Refined NonPositive

  object NonPosInt extends RefinedTypeOps.Numeric[NonPosInt, Int]

  /** A `Long` in the range from 1 to `Long.MaxValue`. */
  type PosLong = Long Refined Positive

  object PosLong extends RefinedTypeOps.Numeric[PosLong, Long]

  /** A `Long` in the range from 0 to `Long.MaxValue`. */
  type NonNegLong = Long Refined NonNegative

  object NonNegLong extends RefinedTypeOps.Numeric[NonNegLong, Long]

  /** A `Long` in the range from `Long.MinValue` to -1. */
  type NegLong = Long Refined Negative

  object NegLong extends RefinedTypeOps.Numeric[NegLong, Long]

  /** A `Long` in the range from `Long.MinValue` to 0. */
  type NonPosLong = Long Refined NonPositive

  object NonPosLong extends RefinedTypeOps.Numeric[NonPosLong, Long]
}
