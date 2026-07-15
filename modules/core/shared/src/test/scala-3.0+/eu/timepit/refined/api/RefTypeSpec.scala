package eu.timepit.refined.api

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.api.RefType.ops._
import eu.timepit.refined.auto._
import eu.timepit.refined.char.{Digit, LowerCase}
import eu.timepit.refined.collection.Forall
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.test.ScalaVersionSpecific.illTyped
import org.scalacheck.Prop._
import org.scalacheck.Properties

// Ported from the Scala 2 `RefTypeSpec`. Only the `Refined` carrier is exercised: the shapeless `@@`
// tag has no `RefType` instance on Scala 3, so the `RefTypeSpecTag` subclass is dropped, as is the
// `<:!<` (shapeless) subtyping test.
abstract class RefTypeSpec[F[_, _]](name: String)(implicit rt: RefType[F])
    extends Properties(s"RefType[$name]") {

  property("unsafeWrap.unwrap ~= id") = forAll((s: String) => rt.unsafeWrap(s).unwrap == s)

  property("unsafeRewrap.unsafeRewrap ~= id") = forAll { (c: Char) =>
    trait A
    trait B
    val c1: F[Char, A] = rt.unsafeWrap(c)
    val c2: F[Char, B] = rt.unsafeRewrap(c1)
    val c3: F[Char, A] = rt.unsafeRewrap(c2)
    c1 == c3
  }

  property("RefinePartiallyApplied instance") = secure {
    val pa = rt.refine[Digit]
    pa('0').isRight
  }

  property("refine success with Less") = secure {
    rt.refine[Less[100]](-100).isRight
  }

  property("refine failure with Interval.Closed") = secure {
    rt.refine[Interval.Closed[-0.5, 0.5]](0.6).isLeft
  }

  property("refine failure with Forall") = secure {
    rt.refine[Forall[LowerCase]]("Hallo").isLeft
  }

  property("refine success with MatchesRegex") = secure {
    type DigitsOnly = MatchesRegex["[0-9]+"]
    rt.refine[DigitsOnly]("123").isRight
  }

  property("refine.unsafeFrom success") = secure {
    rt.refine[Positive].unsafeFrom(5) ?= rt.unsafeWrap[Int, Positive](5)
  }

  property("refine.unsafeFrom failure") = secure {
    throws(classOf[IllegalArgumentException])(rt.refine[Positive].unsafeFrom(-5))
  }

  property("mapRefine success with Positive") = secure {
    rt.refine[Positive](5).flatMap(_.mapRefine(_.toDouble)).isRight
  }

  property("mapRefine failure with Positive") = secure {
    rt.refine[Positive](5).flatMap(_.mapRefine(_ - 10)).isLeft
  }

  property("coflatMapRefine success with Positive") = secure {
    rt.refine[Positive](5).flatMap(_.coflatMapRefine(_.unwrap)).isRight
  }

  property("implicit unwrap") = secure {
    rt.refine[Positive](5).map(_ + 1) == Right(6)
  }

  property("refine ~= RefType.applyRef") = forAll { (i: Int) =>
    type PosInt = F[Int, Positive]
    rt.refine[Positive](i) ?= RefType.applyRef[PosInt](i)
  }

  property("RefType.applyRef.unsafeFrom success") = secure {
    RefType.applyRef[F[Int, Positive]].unsafeFrom(5) ?= rt.unsafeWrap[Int, Positive](5)
  }

  property("RefType.applyRef.unsafeFrom failure") = secure {
    throws(classOf[IllegalArgumentException])(RefType.applyRef[F[Int, Positive]].unsafeFrom(-5))
  }
}

class RefTypeSpecRefined extends RefTypeSpec[Refined]("Refined") {

  property("refineM alias") = secure {
    type PositiveInt = Int Refined Positive

    val x: PositiveInt = RefType[Refined].refineM[Positive](5)
    val y: PositiveInt = 5
    val z = 5: PositiveInt
    // Self-contained snippet (no block-local `PositiveInt`); the failed `autoRefineV` conversion
    // surfaces as a type mismatch against the required refined type rather than "Predicate failed".
    illTyped("val a: Int Refined Positive = -5", "Required: Int Refined .*Positive")
    x == y && y == z
  }

  property("applyRefM alias") = secure {
    type Natural = Long Refined NonNegative
    val Natural = RefType.applyRefM[Natural]

    val x: Natural = Natural(1L)
    val y: Natural = 1L
    val z = 1L: Natural
    illTyped("RefType.applyRefM[Long Refined NonNegative](-1L)", "Predicate.*fail.*")
    illTyped("RefType.applyRefM[Long Refined NonNegative](1.3)", "Cannot prove that")
    x == y && y == z
  }
}
