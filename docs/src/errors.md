```tut
import eu.timepit.refined._
import eu.timepit.refined.numeric._

refineV[Greater[W.`5`.T]].withErrors(6)

refineV[Greater[W.`5`.T]].withErrors(4)

refineV[GreaterEqual[W.`5`.T]].withErrors(4)

refineV[Interval.Closed[W.`5`.T, W.`10`.T]].withErrors(11)
```
