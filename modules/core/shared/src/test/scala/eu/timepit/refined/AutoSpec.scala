package eu.timepit.refined

import eu.timepit.refined.auto._
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties

class AutoSpec extends Properties("auto") {

  property("autoUnwrap: PosInt: Int") = secure {
    val a: PosInt = PosInt.unsafeFrom(1)
    val b: Int = a
    a.value == b
  }

  property("autoUnwrap: PosInt + PosInt") = secure {
    val a = PosInt.unsafeFrom(1)
    val b = PosInt.unsafeFrom(2)
    (a + b) == 3
  }
}
