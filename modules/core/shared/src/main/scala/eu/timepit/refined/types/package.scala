package eu.timepit.refined

package object types {

  /** Module for all predefined refined types. */
  object all extends AllTypes

  /** Module for `Char` refined types. */
  object char extends CharTypes

  /** Module for numeric refined types. */
  object numeric extends NumericTypes

  /** Module for date and time related refined types. */
  object time extends TimeTypes
}
