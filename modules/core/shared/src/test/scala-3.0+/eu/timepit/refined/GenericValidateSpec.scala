package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.generic._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class GenericValidateSpec extends Properties("GenericValidate") {

  property("isValid[Equal[1.4]](1.4)") = secure {
    isValid[Equal[1.4]](1.4)
  }

  property("notValid[Equal[1.4]](2.4)") = secure {
    notValid[Equal[1.4]](2.4)
  }

  property("showExpr[Equal[1.4]](0.4)") = secure {
    showExpr[Equal[1.4]](0.4) ?= "(0.4 == 1.4)"
  }

  property("isValid[Equal[Foo.type]](Foo)") = secure {
    object Foo
    isValid[Equal[Foo.type]](Foo)
  }

  property("isValid[Equal[0]](i: Int)") = forAll { (i: Int) =>
    isValid[Equal[0]](i) ?= (i == 0)
  }

  property("isValid[Equal[0]](l: Long)") = forAll { (l: Long) =>
    isValid[Equal[0]](l) ?= (l == 0L)
  }

  property("isValid[Equal[0]](d: Double)") = forAll { (d: Double) =>
    isValid[Equal[0]](d) ?= (d == 0.0)
  }

  property("showExpr[Equal[5]](0)") = secure {
    showExpr[Equal[5]](0) ?= "(0 == 5)"
  }
}
