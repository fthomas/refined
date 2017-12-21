package eu.timepit.refined

import eu.timepit.refined.api.{Min, Refined}
import eu.timepit.refined.numeric._
import eu.timepit.refined.types.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class MinSpec extends Properties("Min") {

  property("Min - Greater - Wit") = secure {
    Min[Int Refined Greater[W.`1`.T]].min =? refineMV[Greater[W.`1`.T]](2)
  }

  property("Min - Greater - Nat") = secure {
    Min[Int Refined Greater[_0]].min =? refineMV[Greater[_0]](1)
  }

  property("Min - Less - Int") = secure {
    Min[Int Refined Less[_0]].min =? refineMV[Less[_0]](Int.MinValue)
  }

  property("CompanionObject.min - Positive - Long") = secure {
    PosLong.min =? PosLong.unsafeFrom(1)
  }
}
