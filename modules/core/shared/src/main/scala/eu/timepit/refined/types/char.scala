package eu.timepit.refined.types

import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.char.{LowerCase, UpperCase}

/** Module for `Char` refined types. */
object char {

  /** A `Char` that is a lower case character. */
  type LowerCaseChar = Char Refined LowerCase

  object LowerCaseChar extends RefinedTypeOps[LowerCaseChar, Char]

  /** A `Char` that is an upper case character. */
  type UpperCaseChar = Char Refined UpperCase

  object UpperCaseChar extends RefinedTypeOps[UpperCaseChar, Char]
}

trait CharTypes {
  final type LowerCaseChar = char.LowerCaseChar
  final val LowerCaseChar = char.LowerCaseChar

  final type UpperCaseChar = char.UpperCaseChar
  final val UpperCaseChar = char.UpperCaseChar
}
