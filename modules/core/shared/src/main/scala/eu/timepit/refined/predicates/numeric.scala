package eu.timepit.refined.predicates

import eu.timepit.refined

object numeric extends NumericPredicates

trait NumericPredicates {
  final type Less[N] = refined.numeric.Less[N]
  final val Less = refined.numeric.Less

  final type Greater[N] = refined.numeric.Greater[N]
  final val Greater = refined.numeric.Greater

  final type Modulo[N, O] = refined.numeric.Modulo[N, O]
  final val Modulo = refined.numeric.Modulo

  final type LessEqual[N] = refined.numeric.LessEqual[N]

  final type GreaterEqual[N] = refined.numeric.GreaterEqual[N]

  final type Positive = refined.numeric.Positive

  final type NonPositive = refined.numeric.NonPositive

  final type Negative = refined.numeric.Negative

  final type NonNegative = refined.numeric.NonNegative

  final type Divisible[N] = refined.numeric.Divisible[N]

  final type NonDivisible[N] = refined.numeric.NonDivisible[N]

  final type Even = refined.numeric.Even

  final type Odd = refined.numeric.Odd

  final val Interval = refined.numeric.Interval
}
