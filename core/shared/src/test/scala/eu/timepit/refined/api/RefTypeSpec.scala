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
import shapeless.<:!<
import shapeless.nat._
import shapeless.tag.@@
import shapeless.test.illTyped

abstract class RefTypeSpec[F[_, _]](name: String)(implicit rt: RefType[F]) extends Properties(s"RefType[$name]") {

  property("unsafeWrap.unwrap ~= id") = forAll { (s: String) =>
    rt.unsafeWrap(s).unwrap == s
  }

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

  property("refine.unsafeFrom success") = secure {
    rt.refine[Positive].unsafeFrom(5) ?= rt.unsafeWrap[Int, Positive](5)
  }

  property("refine.unsafeFrom failure") = secure {
    throws(classOf[IllegalArgumentException])(rt.refine[Positive].unsafeFrom(-5))
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

    val x: PositiveInt = RefType[Refined].refineM(5)
    val y: PositiveInt = 5
    val z = 5: PositiveInt
    illTyped("val a: PositiveInt = -5", "Predicate failed: \\(-5 > 0\\).*")
    x == y && y == z
  }

  property("refineMF alias") = secure {
    type Natural = Long Refined NonNegative
    val Natural = RefType[Refined].refineMF[Long, NonNegative]

    val x: Natural = Natural(1L)
    val y: Natural = 1L
    val z = 1L: Natural
    illTyped("Natural(-1L)", "Predicate.*fail.*")
    illTyped("Natural(1.3)", "type mismatch.*")
    x == y && y == z
  }

  property("applyRefM alias") = secure {
    type Natural = Long Refined NonNegative
    val Natural = RefType.applyRefM[Natural]

    val x: Natural = Natural(1L)
    val y: Natural = 1L
    val z = 1L: Natural
    illTyped("Natural(-1L)", "Predicate.*fail.*")
    illTyped("Natural(1.3)", "Cannot prove that.*")
    x == y && y == z
  }

  property("(T Refined P) <:! T") = wellTyped {
    val x = implicitly[(Int Refined Positive) <:!< Int]
  }
}

class RefTypeSpecTag extends RefTypeSpec[@@]("@@") {

  property("refineM alias") = wellTyped {
    type PositiveInt = Int @@ Positive

    // This is expected, see https://github.com/fthomas/refined/issues/21:
    illTyped("val x: PositiveInt = RefType[@@].refineM(5)", "could not find implicit value.*")
    illTyped("val y: PositiveInt = 5", "type mismatch.*")
    illTyped("val z: PositiveInt = -5", "type mismatch.*")
  }

  property("refineMF alias") = secure {
    val Natural = RefType[@@].refineMF[Long, NonNegative]

    val x: Long @@ NonNegative = Natural(1L)
    val y: Long @@ NonNegative = 1L
    val z = 1L: Long @@ NonNegative
    illTyped("Natural(-1L)", "Predicate.*fail.*")
    illTyped("Natural(1.3)", "type mismatch.*")
    (x: Long) == (y: Long) && (y: Long) == (z: Long)
  }

  property("(T @@ P) <: T") = wellTyped {
    val x = implicitly[(Int @@ Positive) <:< Int]
  }
}
