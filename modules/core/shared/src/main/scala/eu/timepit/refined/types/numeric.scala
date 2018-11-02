package eu.timepit.refined.types

import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.numeric.{Negative, NonNegative, NonPositive, Positive}

/** Module for numeric refined types. */
object numeric {

  /** A `Byte` in the range from 1 to `Byte.MaxValue`. */
  type PosByte = Byte Refined Positive

  object PosByte extends RefinedTypeOps.Numeric[PosByte, Byte]

  /** A `Byte` in the range from 0 to `Byte.MaxValue`. */
  type NonNegByte = Byte Refined NonNegative

  object NonNegByte extends RefinedTypeOps.Numeric[NonNegByte, Byte]

  /** A `Byte` in the range from `Byte.MinValue` to -1. */
  type NegByte = Byte Refined Negative

  object NegByte extends RefinedTypeOps.Numeric[NegByte, Byte]

  /** A `Byte` in the range from `Byte.MinValue` to 0. */
  type NonPosByte = Byte Refined NonPositive

  object NonPosByte extends RefinedTypeOps.Numeric[NonPosByte, Byte]

  /** A `Short` in the range from 1 to `Short.MaxValue`. */
  type PosShort = Short Refined Positive

  object PosShort extends RefinedTypeOps.Numeric[PosShort, Short]

  /** A `Short` in the range from 0 to `Short.MaxValue`. */
  type NonNegShort = Short Refined NonNegative

  object NonNegShort extends RefinedTypeOps.Numeric[NonNegShort, Short]

  /** A `Short` in the range from `Short.MinValue` to -1. */
  type NegShort = Short Refined Negative

  object NegShort extends RefinedTypeOps.Numeric[NegShort, Short]

  /** A `Short` in the range from `Short.MinValue` to 0. */
  type NonPosShort = Short Refined NonPositive

  object NonPosShort extends RefinedTypeOps.Numeric[NonPosShort, Short]

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

  /** A `BigInt` greater than 0. */
  type PosBigInt = BigInt Refined Positive

  object PosBigInt extends RefinedTypeOps[PosBigInt, BigInt]

  /** A `BigInt` greater than or equal to 0. */
  type NonNegBigInt = BigInt Refined NonNegative

  object NonNegBigInt extends RefinedTypeOps[NonNegBigInt, BigInt]

  /** A `BigInt` less than or equal to 0. */
  type NegBigInt = BigInt Refined Negative

  object NegBigInt extends RefinedTypeOps[NegBigInt, BigInt]

  /** A `BigInt` less than or equal to 0. */
  type NonPosBigInt = BigInt Refined NonPositive

  object NonPosBigInt extends RefinedTypeOps[NonPosBigInt, BigInt]

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

  /** A `BigDecimal` greater than 0. */
  type PosBigDecimal = BigDecimal Refined Positive

  object PosBigDecimal extends RefinedTypeOps[PosBigDecimal, BigDecimal]

  /** A `BigDecimal` greater than or equal to 0. */
  type NonNegBigDecimal = BigDecimal Refined NonNegative

  object NonNegBigDecimal extends RefinedTypeOps[NonNegBigDecimal, BigDecimal]

  /** A `BigDecimal` less than 0. */
  type NegBigDecimal = BigDecimal Refined Negative

  object NegBigDecimal extends RefinedTypeOps[NegBigDecimal, BigDecimal]

  /** A `BigDecimal` less than or equal to 0. */
  type NonPosBigDecimal = BigDecimal Refined NonPositive

  object NonPosBigDecimal extends RefinedTypeOps[NonPosBigDecimal, BigDecimal]
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

trait NumericTypesBinCompat1 {
  final type PosByte = numeric.PosByte
  final val PosByte = numeric.PosByte

  final type NonNegByte = numeric.NonNegByte
  final val NonNegByte = numeric.NonNegByte

  final type NegByte = numeric.NegByte
  final val NegByte = numeric.NegByte

  final type NonPosByte = numeric.NonPosByte
  final val NonPosByte = numeric.NonPosByte

  final type PosShort = numeric.PosShort
  final val PosShort = numeric.PosShort

  final type NonNegShort = numeric.NonNegShort
  final val NonNegShort = numeric.NonNegShort

  final type NegShort = numeric.NegShort
  final val NegShort = numeric.NegShort

  final type NonPosShort = numeric.NonPosShort
  final val NonPosShort = numeric.NonPosShort

  final type PosBigInt = numeric.PosBigInt
  final val PosBigInt = numeric.PosBigInt

  final type NonNegBigInt = numeric.NonNegBigInt
  final val NonNegBigInt = numeric.NonNegBigInt

  final type NegBigInt = numeric.NegBigInt
  final val NegBigInt = numeric.NegBigInt

  final type NonPosBigInt = numeric.NonPosBigInt
  final val NonPosBigInt = numeric.NonPosBigInt

  final type PosBigDecimal = numeric.PosBigDecimal
  final val PosBigDecimal = numeric.PosBigDecimal

  final type NonNegBigDecimal = numeric.NonNegBigDecimal
  final val NonNegBigDecimal = numeric.NonNegBigDecimal

  final type NegBigDecimal = numeric.NegBigDecimal
  final val NegBigDecimal = numeric.NegBigDecimal

  final type NonPosBigDecimal = numeric.NonPosBigDecimal
  final val NonPosBigDecimal = numeric.NonPosBigDecimal
}
