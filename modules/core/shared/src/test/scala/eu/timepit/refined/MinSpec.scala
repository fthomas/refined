package eu.timepit.refined

import eu.timepit.refined.api.{Min, Refined}
import eu.timepit.refined.numeric._
import eu.timepit.refined.types.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class MinSpec extends Properties("Min") {

  property("Min[Int Refined Greater[W.`1`.T]]") = secure {
    Min[Int Refined Greater[W.`1`.T]].min =? refineMV[Greater[W.`1`.T]](2)
  }

  property("Min[Int Refined Greater[_0]]") = secure {
    Min[Int Refined Greater[_0]].min =? refineMV[Greater[_0]](1)
  }

  property("Min[Long Refined Greater[_0]]") = secure {
    Min[Long Refined Greater[_0]].min =? refineMV[Greater[_0]](1L)
  }

  property("Min[Byte Refined Less[_0]]") = secure {
    Min[Byte Refined Less[_0]].min =? refineMV[Less[_0]](Byte.MinValue)
  }

  property("Min[Short Refined Less[_0]]") = secure {
    Min[Short Refined Less[_0]].min =? refineMV[Less[_0]](Short.MinValue)
  }

  property("Min[Int Refined Less[_0]]") = secure {
    Min[Int Refined Less[_0]].min =? refineMV[Less[_0]](Int.MinValue)
  }

  property("Min[Long Refined Less[_0]]") = secure {
    Min[Long Refined Less[_0]].min =? refineMV[Less[_0]](Long.MinValue)
  }

  property("CompanionObject.min - Positive - Long") = secure {
    PosLong.min =? PosLong.unsafeFrom(1)
  }
}
