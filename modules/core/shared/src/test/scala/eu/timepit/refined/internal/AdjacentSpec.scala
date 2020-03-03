package eu.timepit.refined.internal

import eu.timepit.refined.TestUtils.wellTyped
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.test.illTyped

class AdjacentSpec extends Properties("Adjacent") {

  property("nextUp.Int") = forAll((i: Int) => Adjacent[Int].nextUp(i) >= i)

  property("nextDown.Int") = forAll((i: Int) => Adjacent[Int].nextDown(i) <= i)

  property("nextUp.Double") = forAll((d: Double) => Adjacent[Double].nextUp(d) >= d)

  property("nextDown.Double") = forAll((d: Double) => Adjacent[Double].nextDown(d) <= d)

  property("BigDecimal instance is non-derivable") = wellTyped {
    illTyped("Adjacent[BigDecimal]", "could not find implicit value.*")
  }
}
