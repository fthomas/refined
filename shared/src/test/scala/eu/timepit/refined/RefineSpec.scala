package eu.timepit.refined

import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.MatchesRegex
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._

class RefineSpec extends Properties("refine") {

  property("Refine instance") = secure {
    val r = refineT[Digit]
    r('0').isRight
  }

  property("refineT success with Less") = secure {
    refineT[Less[W.`100`.T]](-100).isRight
  }

  property("refineT success with Greater") = secure {
    refineT[Greater[_5]](6).isRight
  }

  property("refineT failure with Interval") = secure {
    refineT[Interval[W.`-0.5`.T, W.`0.5`.T]](0.6).isLeft
  }

  property("refineT failure with Forall") = secure {
    refineT[Forall[LowerCase]]("Hallo").isLeft
  }

  property("refineT success with MatchesRegex") = secure {
    type DigitsOnly = MatchesRegex[W.`"[0-9]+"`.T]
    val res = refineT[DigitsOnly][String]("123")
    res.isRight
  }
}
