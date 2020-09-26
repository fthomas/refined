package eu.timepit.refined.cats

import eu.timepit.refined.api.{Max, Min}
import org.scalacheck.{Arbitrary, Prop, Properties}
import org.scalacheck.Prop._

class NonNegShiftSpec extends Properties("NonNegShift") {
  final def createProperty[A: Arbitrary: Min: NonNegShift](implicit num: Numeric[A]): Prop = {
    import num.{abs, gteq, lt, plus, zero}

    forAll { a: A =>
      gteq(a, zero) ==> (NonNegShift[A].shift(a) == a)
    } &&
    forAll { a: A => lt(a, zero) ==> (NonNegShift[A].shift(a) == plus(a, abs(Min[A].min))) }
  }

  property("shift Byte") = createProperty[Byte]
  property("shift Short") = createProperty[Short]
  property("shift Int") = createProperty[Int]
  property("shift Long") = createProperty[Long]
}

class NegShiftSpec extends Properties("NegShift") {
  final def createProperty[A: Arbitrary: Max: NegShift](implicit num: Numeric[A]): Prop = {
    import num.{gteq, lt, minus, one, zero}

    forAll { a: A =>
      lt(a, zero) ==> (NegShift[A].shift(a) == a)
    } &&
    forAll { a: A => gteq(a, zero) ==> (NegShift[A].shift(a) == minus(minus(a, Max[A].max), one)) }
  }

  property("shift Byte") = createProperty[Byte]
  property("shift Short") = createProperty[Short]
  property("shift Int") = createProperty[Int]
  property("shift Long") = createProperty[Long]
}
