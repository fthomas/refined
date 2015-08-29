package eu.timepit.refined

import eu.timepit.refined.RefType.ops._
import eu.timepit.refined.char.{Digit, LowerCase}
import eu.timepit.refined.collection.Forall
import eu.timepit.refined.implicits._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.MatchesRegex
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.tag.@@

abstract class RefTypeProperties[F[_, _]](name: String)(implicit rt: RefType[F]) extends Properties(s"RefType[$name]") {

  property("unsafeWrap.unwrap ~= id") = forAll { (s: String) =>
    rt.unsafeWrap(s).unwrap == s
  }

  property("RefineAux instance") = secure {
    val aux = rt.refine[Digit]
    aux('0').isRight
  }

  property("refine success with Less") = secure {
    rt.refine[Less[W.`100`.T]](-100).isRight
  }

  property("refine success with Greater") = secure {
    rt.refine[Greater[_5]](6).isRight
  }

  property("refine failure with Interval") = secure {
    rt.refine[Interval[W.`-0.5`.T, W.`0.5`.T]](0.6).isLeft
  }

  property("refine failure with Forall") = secure {
    rt.refine[Forall[LowerCase]]("Hallo").isLeft
  }

  property("refine success with MatchesRegex") = secure {
    type DigitsOnly = MatchesRegex[W.`"[0-9]+"`.T]
    rt.refine[DigitsOnly]("123").isRight
  }

  property("mapRefine success with Positive") = secure {
    rt.refine[Positive](5).right.flatMap(_.mapRefine(_.toDouble)).isRight
  }

  property("mapRefine failure with Positive") = secure {
    rt.refine[Positive](5).right.flatMap(_.mapRefine(_ - 10)).isLeft
  }

  property("implicit unwrap") = secure {
    rt.refine[Positive](5).right.map(_ + 1) == Right(6)
  }
}

class RefTypeSpecRefined extends RefTypeProperties[Refined]("Refined")

class RefTypeSpecTag extends RefTypeProperties[@@]("@@")
