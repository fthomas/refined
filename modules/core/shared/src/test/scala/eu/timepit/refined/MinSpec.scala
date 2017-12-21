package eu.timepit.refined

import eu.timepit.refined.api.{InhabitantsOf, Min, Refined}
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class MinSpec extends Properties("Min") {

  property("InhabitantsOf - Interval.Closed - W") = secure {
    InhabitantsOf[Int Refined Interval.Closed[W.`1`.T, W.`10`.T]].all =? (1 to 10)
      .map(refineV[Interval.Closed[W.`1`.T, W.`10`.T]](_).right.get)
      .toStream
  }

  property("Min - Greater - Wit") = secure {
    Min[Int Refined Greater[W.`1`.T]].min =? refineMV[Greater[W.`1`.T]](2)
  }

  property("Min - Greater - Nat") = secure {
    Min[Int Refined Greater[_0]].min =? refineMV[Greater[_0]](1)
  }

  property("Min - Less - Int") = secure {
    Min[Int Refined Less[_0]].min =? refineMV[Less[_0]](Int.MinValue)
  }
}
