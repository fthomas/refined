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
    val r = refine[Digit]
    r('0').isRight
  }

  property("refine success with Less") = secure {
    refine[Less[W.`100`.T]](-100).isRight
  }

  property("refine success with Greater") = secure {
    refine[Greater[_5]](6).isRight
  }

  property("refine failure with Interval") = secure {
    refine[Interval[W.`-0.5`.T, W.`0.5`.T]](0.6).isLeft
  }

  property("refine failure with Forall") = secure {
    refine[Forall[LowerCase]]("Hallo").isLeft
  }

  property("refine success with MatchesRegex") = secure {
    type DigitsOnly = MatchesRegex[W.`"[0-9]+"`.T]
    val res = refine[DigitsOnly][String]("123")
    res.isRight
  }
}
