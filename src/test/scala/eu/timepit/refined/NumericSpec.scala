package eu.timepit.refined

import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.tag.@@
import shapeless.test.illTyped

class NumericSpec extends Properties("numeric") {
  property("Less.isValid") = forAll { (d: Double) =>
    Predicate[Less[W.`1.0`.T], Double].isValid(d) ?= (d < 1.0)
  }

  property("Less.show") = secure {
    Predicate[Less[W.`1.0`.T], Double].show(0.0) ?= "(0.0 < 1.0)"
  }

  property("Greater.isValid") = forAll { (d: Double) =>
    Predicate[Greater[W.`1.0`.T], Double].isValid(d) ?= (d > 1.0)
  }

  property("Greater.show") = secure {
    Predicate[Greater[W.`1.0`.T], Double].show(0.0) ?= "(0.0 > 1.0)"
  }

  property("Less.Nat ~= Less") = forAll { (i: Int) =>
    Predicate[Less[_5], Int].validated(i) ?= Predicate[Less[W.`5`.T], Int].validated(i)
  }

  property("Greater.Nat ~= Greater") = forAll { (i: Int) =>
    Predicate[Greater[_5], Int].validated(i) ?= Predicate[Greater[W.`5`.T], Int].validated(i)
  }

  property("Less.Nat.isValid") = forAll { (i: Int) =>
    Predicate[Less[_5], Int].isValid(i) ?= (i < 5)
  }

  property("Less.Nat.show") = secure {
    Predicate[Less[_5], Int].show(0) ?= "(0 < 5)"
  }

  property("LessEqual.Nat.isValid") = forAll { (i: Int) =>
    Predicate[LessEqual[_5], Int].isValid(i) ?= (i <= 5)
  }

  property("LessEqual.Nat.show") = secure {
    Predicate[LessEqual[_5], Int].show(0) ?= "!(0 > 5)"
  }

  property("Greater.Nat.isValid") = forAll { (i: Int) =>
    Predicate[Greater[_5], Int].isValid(i) ?= (i > 5)
  }

  property("Greater.Nat.show") = secure {
    Predicate[Greater[_5], Int].show(0) ?= "(0 > 5)"
  }

  property("GreaterEqual.Nat.isValid") = forAll { (i: Int) =>
    Predicate[GreaterEqual[_5], Int].isValid(i) ?= (i >= 5)
  }

  property("GreaterEqual.Nat.show") = secure {
    Predicate[GreaterEqual[_5], Int].show(0) ?= "!(0 < 5)"
  }

  property("Interval[_0, _1].isValid") = forAll { (d: Double) =>
    Predicate[Interval[_0, _1], Double].isValid(d) ?= (d >= 0.0 && d <= 1.0)
  }

  property("Equal.Nat.isValid") = forAll { (i: Int) =>
    Predicate[Equal[_5], Int].isValid(i) ?= (i == 5)
  }

  property("Equal.Nat.show") = secure {
    Predicate[Equal[_5], Int].show(0) ?= "(0 == 5)"
  }

  property("Equal.Nat ~= Equal") = forAll { (i: Int) =>
    Predicate[Equal[_1], Int].validated(i) ?= Predicate[Equal[W.`1`.T], Int].validated(i)
  }

  property("Inference.Less") = secure {
    val x: Double @@ Less[W.`7.5`.T] = refine[Less[W.`7.2`.T]](1.0).right.get
    illTyped("val y: Double @@ Less[W.`7.2`.T] = refine[Less[W.`7.5`.T]](1.0).right.get")
    true
  }

  property("Inference.Greater") = secure {
    val x: Double @@ Greater[W.`7.2`.T] = refine[Greater[W.`7.5`.T]](10.0).right.get
    illTyped("val y: Double @@ Greater[W.`7.5`.T] = refine[Greater[W.`7.2`.T]](10.0).right.get")
    true
  }

  property("Inference.Less.Nat") = secure {
    val x: Int @@ Less[_10] = refine[Less[_5]](1).right.get
    illTyped("val y: Int @@ Less[_5] = refine[Less[_10]](1).right.get")
    true
  }

  property("Inference.Greater.Nat") = secure {
    val x: Int @@ Greater[_5] = refine[Greater[_10]](15).right.get
    illTyped("val y: Int @@ Greater[_10] = refine[Greater[_5]](15).right.get")
    true
  }
}
