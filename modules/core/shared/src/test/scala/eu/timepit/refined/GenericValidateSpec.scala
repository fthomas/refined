package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.generic._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.Nat._

class GenericValidateSpec extends Properties("GenericValidate") {

  property("Equal.isValid") = secure {
    isValid[Equal[W.`1.4`.T]](1.4)
  }

  property("Equal.notValid") = secure {
    notValid[Equal[W.`1.4`.T]](2.4)
  }

  property("Equal.showExpr") = secure {
    showExpr[Equal[W.`1.4`.T]](0.4) ?= "(0.4 == 1.4)"
  }

  property("Equal.object.isValid") = secure {
    object Foo
    isValid[Equal[Foo.type]](Foo)
  }

  property("Equal.Symbol.isValid") = secure {
    isValid[Equal[W.`'foo`.T]](Symbol("foo"))
  }

  property("Equal.Symbol.notValid") = secure {
    notValid[Equal[W.`'foo`.T]](Symbol("bar"))
  }

  property("Equal.Symbol.showExpr") = secure {
    showExpr[Equal[W.`'foo`.T]](Symbol("bar")) ?= "('bar == 'foo)"
  }

  property("Equal.Nat.Int.isValid") = forAll { (i: Int) =>
    isValid[Equal[_0]](i) ?= (i == 0)
  }

  property("Equal.Nat.Long.isValid") = forAll { (l: Long) =>
    isValid[Equal[_0]](l) ?= (l == 0L)
  }

  property("Equal.Nat.Double.isValid") = forAll { (d: Double) =>
    isValid[Equal[_0]](d) ?= (d == 0.0)
  }

  property("Equal.Nat.showExpr") = secure {
    showExpr[Equal[_5]](0) ?= "(0 == 5)"
  }

  property("Equal.Nat ~= Equal.Int") = forAll { (i: Int) =>
    showResult[Equal[_1]](i) ?= showResult[Equal[W.`1`.T]](i)
  }
}
