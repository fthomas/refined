package eu.timepit.refined.types

import eu.timepit.refined.api.Refined
import eu.timepit.refined.char.{LowerCase, UpperCase}

trait CharTypes {

  /** A `Char` that is a lower case character. */
  type LowerCaseChar = Char Refined LowerCase

  /** A `Char` that is an upper case character. */
  type UpperCaseChar = Char Refined UpperCase
}
