package eu.timepit.refined.types

import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.numeric.{Negative, NonNegative, NonPositive, Positive}

/** Module for numeric refined types. */
object numeric {

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

  /** A `Float` greater than 0. */
  type PosFloat = Float Refined Positive

  object PosFloat extends RefinedTypeOps.Numeric[PosFloat, Float]

  /** A `Float` greater than or equal to 0. */
  type NonNegFloat = Float Refined NonNegative

  object NonNegFloat extends RefinedTypeOps.Numeric[NonNegFloat, Float]

  /** A `Float` less than 0. */
  type NegFloat = Float Refined Negative

  object NegFloat extends RefinedTypeOps.Numeric[NegFloat, Float]

  /** A `Float` less than or equal to 0. */
  type NonPosFloat = Float Refined NonPositive

  object NonPosFloat extends RefinedTypeOps.Numeric[NonPosFloat, Float]

  /** A `Double` greater than 0. */
  type PosDouble = Double Refined Positive

  object PosDouble extends RefinedTypeOps.Numeric[PosDouble, Double]

  /** A `Double` greater than or equal to 0. */
  type NonNegDouble = Double Refined NonNegative

  object NonNegDouble extends RefinedTypeOps.Numeric[NonNegDouble, Double]

  /** A `Double` less than 0. */
  type NegDouble = Double Refined Negative

  object NegDouble extends RefinedTypeOps.Numeric[NegDouble, Double]

  /** A `Double` less than or equal to 0. */
  type NonPosDouble = Double Refined NonPositive

  object NonPosDouble extends RefinedTypeOps.Numeric[NonPosDouble, Double]
}

trait NumericTypes {
  final type PosInt = numeric.PosInt
  final val PosInt = numeric.PosInt

  final type NonNegInt = numeric.NonNegInt
  final val NonNegInt = numeric.NonNegInt

  final type NegInt = numeric.NegInt
  final val NegInt = numeric.NegInt

  final type NonPosInt = numeric.NonPosInt
  final val NonPosInt = numeric.NonPosInt

  final type PosLong = numeric.PosLong
  final val PosLong = numeric.PosLong

  final type NonNegLong = numeric.NonNegLong
  final val NonNegLong = numeric.NonNegLong

  final type NegLong = numeric.NegLong
  final val NegLong = numeric.NegLong

  final type NonPosLong = numeric.NonPosLong
  final val NonPosLong = numeric.NonPosLong

  final type PosFloat = numeric.PosFloat
  final val PosFloat = numeric.PosFloat

  final type NonNegFloat = numeric.NonNegFloat
  final val NonNegFloat = numeric.NonNegFloat

  final type NegFloat = numeric.NegFloat
  final val NegFloat = numeric.NegFloat

  final type NonPosFloat = numeric.NonPosFloat
  final val NonPosFloat = numeric.NonPosFloat

  final type PosDouble = numeric.PosDouble
  final val PosDouble = numeric.PosDouble

  final type NonNegDouble = numeric.NonNegDouble
  final val NonNegDouble = numeric.NonNegDouble

  final type NegDouble = numeric.NegDouble
  final val NegDouble = numeric.NegDouble

  final type NonPosDouble = numeric.NonPosDouble
  final val NonPosDouble = numeric.NonPosDouble
}
