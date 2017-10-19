package eu.timepit.refined.types

import eu.timepit.refined.api.Refined
import eu.timepit.refined.macros.refinedCompanion
import eu.timepit.refined.numeric.{Negative, NonNegative, NonPositive, Positive}

/** Module for numeric refined types. */
object numeric extends NumericTypes

trait NumericTypes {

  /** An `Int` in the range from 1 to `Int.MaxValue`. */
  @refinedCompanion
  type PosInt = Int Refined Positive

  /** An `Int` in the range from 0 to `Int.MaxValue`. */
  @refinedCompanion
  type NonNegInt = Int Refined NonNegative

  /** An `Int` in the range from `Int.MinValue` to -1. */
  @refinedCompanion
  type NegInt = Int Refined Negative

  /** An `Int` in the range from `Int.MinValue` to 0. */
  @refinedCompanion
  type NonPosInt = Int Refined NonPositive

  /** A `Long` in the range from 1 to `Long.MaxValue`. */
  @refinedCompanion
  type PosLong = Long Refined Positive

  /** A `Long` in the range from 0 to `Long.MaxValue`. */
  type NonNegLong = Long Refined NonNegative

  /** A `Long` in the range from `Long.MinValue` to -1. */
  type NegLong = Long Refined Negative

  /** A `Long` in the range from `Long.MinValue` to 0. */
  type NonPosLong = Long Refined NonPositive
}
