package eu.timepit.refined.predicates

import eu.timepit.refined

object char extends CharPredicates

trait CharPredicates {
  final type Digit = refined.char.Digit
  final val Digit = refined.char.Digit

  final type Letter = refined.char.Letter
  final val Letter = refined.char.Letter

  final type LowerCase = refined.char.LowerCase
  final val LowerCase = refined.char.LowerCase

  final type UpperCase = refined.char.UpperCase
  final val UpperCase = refined.char.UpperCase

  final type Whitespace = refined.char.Whitespace
  final val Whitespace = refined.char.Whitespace

  final type LetterOrDigit = refined.char.LetterOrDigit
}
