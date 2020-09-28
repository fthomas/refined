package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.generic._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class GenericValidateSpec extends Properties("GenericValidate") {

  property("isValid[Equal[1.4]](1.4)") = secure {
    isValid[Equal[W.`1.4`.T]](1.4)
  }

  property("notValid[Equal[1.4]](2.4)") = secure {
    notValid[Equal[W.`1.4`.T]](2.4)
  }

  property("showExpr[Equal[1.4]](0.4)") = secure {
    showExpr[Equal[W.`1.4`.T]](0.4) ?= "(0.4 == 1.4)"
  }

  property("isValid[Equal[Foo.type]](Foo)") = secure {
    object Foo
    isValid[Equal[Foo.type]](Foo)
  }

  property("isValid[Equal[0]](i: Int)") = forAll { (i: Int) =>
    isValid[Equal[W.`0`.T]](i) ?= (i == 0)
  }

  property("isValid[Equal[0]](l: Long)") = forAll { (l: Long) =>
    isValid[Equal[W.`0`.T]](l) ?= (l == 0L)
  }

  property("isValid[Equal[0]](d: Double)") = forAll { (d: Double) =>
    isValid[Equal[W.`0`.T]](d) ?= (d == 0.0)
  }

  property("showExpr[Equal[5]](0)") = secure {
    showExpr[Equal[W.`5`.T]](0) ?= "(0 == 5)"
  }
}
