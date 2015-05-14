package eu.timepit.refined

import eu.timepit.refined.boolean.Not

object generic {
  sealed trait Empty

  type NonEmpty = Not[Empty]

  sealed trait Length[P]

  sealed trait LowerCase

  sealed trait UpperCase
}
