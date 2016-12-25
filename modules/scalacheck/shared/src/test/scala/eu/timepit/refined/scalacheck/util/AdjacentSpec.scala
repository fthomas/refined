package eu.timepit.refined.scalacheck.util

import org.scalacheck.Prop._
import org.scalacheck.Properties

class AdjacentSpec extends Properties("Adjacent") {

  property("nextUpOrSelf Char") = forAll { (c: Char) =>
    Adjacent[Char].nextUpOrSelf(c) >= c
  }

  property("nextUpOrSelf Double") = forAll { (d: Double) =>
    Adjacent[Double].nextUpOrSelf(d) >= d
  }

  property("nextDownOrSelf Float") = forAll { (f: Float) =>
    Adjacent[Float].nextDownOrSelf(f) <= f
  }

  property("nextDownOrSelf Long") = forAll { (l: Long) =>
    Adjacent[Long].nextDownOrSelf(l) <= l
  }
}
