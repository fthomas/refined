package eu.timepit.refined

import eu.timepit.refined.api.Refined
import eu.timepit.refined.macros.refinedCompanion
import eu.timepit.refined.numeric.Positive

object types2 {

  @refinedCompanion type PosInt2 = Int Refined Positive

}
