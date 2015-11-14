package eu.timepit.refined
package api

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.api.RefType.ops._
import eu.timepit.refined.auto._
import eu.timepit.refined.char.{ Digit, LowerCase }
import eu.timepit.refined.collection.Forall
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.MatchesRegex
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.tag.@@
import shapeless.test.illTyped

abstract class RefTypeSpec[F[_, _]](name: String)(implicit rt: RefType[F]) extends Properties(s"RefType[$name]") {

  property("unsafeWrap.unwrap ~= id") = forAll { (s: String) =>
    rt.unsafeWrap(s).unwrap == s
  }

  property("RefinePartiallyApplied instance") = secure {
    val pa = rt.refine[Digit]
    pa('0').isRight
  }

  property("refine success with Less") = secure {
    rt.refine[Less[W.`100`.T]](-100).isRight
  }

  property("refine success with Greater") = secure {
    rt.refine[Greater[_5]](6).isRight
  }

  property("refine failure with Interval.Closed") = secure {
    rt.refine[Interval.Closed[W.`-0.5`.T, W.`0.5`.T]](0.6).isLeft
  }

  property("refine failure with Forall") = secure {
    rt.refine[Forall[LowerCase]]("Hallo").isLeft
  }

  property("refine success with MatchesRegex") = secure {
    type DigitsOnly = MatchesRegex[W.`"[0-9]+"`.T]
    rt.refine[DigitsOnly]("123").isRight
  }

  property("refine.force success") = secure {
    rt.refine[Positive].force(5) ?= rt.unsafeWrap[Int, Positive](5)
  }

  property("refine.force failure") = secure {
    throws(classOf[IllegalArgumentException])(rt.refine[Positive].force(-5))
  }

  property("mapRefine success with Positive") = secure {
    rt.refine[Positive](5).right.flatMap(_.mapRefine(_.toDouble)).isRight
  }

  property("mapRefine failure with Positive") = secure {
    rt.refine[Positive](5).right.flatMap(_.mapRefine(_ - 10)).isLeft
  }

  property("coflatMapRefine success with Positive") = secure {
    rt.refine[Positive](5).right.flatMap(_.coflatMapRefine(_.unwrap)).isRight
  }

  property("implicit unwrap") = secure {
    rt.refine[Positive](5).right.map(_ + 1) == Right(6)
  }

  property("refine ~= RefType.applyRef") = forAll { (i: Int) =>
    type PosInt = F[Int, Positive]
    rt.refine[Positive](i) ?= RefType.applyRef[PosInt](i)
  }
}

class RefTypeSpecRefined extends RefTypeSpec[Refined]("Refined") {

  property("refineM with type alias") = secure {
    type PositiveInt = Int Refined Positive

    val x: PositiveInt = RefType[Refined].refineM(5)
    val y: PositiveInt = 5
    val z = 5: PositiveInt
    illTyped("val a: PositiveInt = -5", "Predicate failed: \\(-5 > 0\\).*")
    x == y && y == z
  }

  property("(T Refined P) <!: T") = wellTyped {
    illTyped("implicitly[(Int Refined Positive) <:< Int]", "Cannot prove.*")
  }
}

class RefTypeSpecTag extends RefTypeSpec[@@]("@@") {

  property("refineM with type alias") = wellTyped {
    type PositiveInt = Int @@ Positive

    // This is expected, see https://github.com/fthomas/refined/issues/21:
    illTyped("val x: PositiveInt = RefType[@@]refineM(5)", "could not find implicit value.*")
    illTyped("val y: PositiveInt = 5", "(?s)type mismatch.*")
    illTyped("val z: PositiveInt = -5", "(?s)type mismatch.*")
  }

  property("(T @@ P) <: T") = wellTyped {
    val x = implicitly[(Int @@ Positive) <:< Int]
  }
}
