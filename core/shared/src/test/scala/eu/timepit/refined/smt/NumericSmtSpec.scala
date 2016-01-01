package eu.timepit.refined
package smt

import eu.timepit.refined.boolean._
import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class NumericSmtSpec extends Properties("NumericSmt") {
  /*
  property("Less") = secure {
    Formula[Less[W.`5`.T]].subFormula("x").expr ?= "(< x 5)"
  }

  property("Greater") = secure {
    Formula[Greater[W.`5`.T]].subFormula("x").expr ?= "(> x 5)"
  }

  property("LessEqual") = secure {
    Formula[LessEqual[W.`5`.T]].subFormula("x").expr ?= "(not (> x 5))"
  }

  property("GreaterEqual") = secure {
    Formula[GreaterEqual[W.`5`.T]].subFormula("x").expr ?= "(not (< x 5))"
  }

  property("Interval") = secure {
    Formula[Interval[W.`5`.T, W.`12`.T]].subFormula("x").expr ?= "(and (not (< x 5)) (not (> x 12)))"
  }*/
}
