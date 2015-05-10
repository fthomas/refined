package eu.timepit.refined

import eu.timepit.refined.boolean.Not

object generic {
  sealed trait NonEmpty

  type Empty = Not[NonEmpty]

  sealed trait Length[P]
}
