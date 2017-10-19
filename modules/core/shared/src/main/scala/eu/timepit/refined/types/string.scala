package eu.timepit.refined.types

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.macros.refinedCompanion

/** Module for `String` refined types. */
object string extends StringTypes

trait StringTypes {

  /** A `String` that is not empty. */
  @refinedCompanion
  type NonEmptyString = String Refined NonEmpty
}
