package eu.timepit.refined.types

import eu.timepit.refined.api.Refined
import eu.timepit.refined.char.{LowerCase, UpperCase}
import eu.timepit.refined.macros.refinedCompanion

/** Module for `Char` refined types. */
object char extends CharTypes

trait CharTypes {

  /** A `Char` that is a lower case character. */
  @refinedCompanion
  type LowerCaseChar = Char Refined LowerCase

  /** A `Char` that is an upper case character. */
  @refinedCompanion
  type UpperCaseChar = Char Refined UpperCase
}
