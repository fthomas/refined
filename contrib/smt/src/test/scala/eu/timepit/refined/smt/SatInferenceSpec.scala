package eu.timepit.refined
package smt

import eu.timepit.refined.api.{ Refined, TypedInference }
import eu.timepit.refined.auto._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.test.illTyped

class SatInferenceSpec extends Properties("SatInference") {

  property("(> x 10) ==> (> x 0)") = secure {
    TypedInference[Int, Sat[W.`"(> x 10)"`.T], Sat[W.`"(> x 0)"`.T]].isValid
  }

  property("(> x 0) =!> (> x 10)") = secure {
    TypedInference[Int, Sat[W.`"(> x 0)"`.T], Sat[W.`"(> x 10)"`.T]].notValid
  }

  property("(> x 2.3) ==> (> x 2.1)") = secure {
    TypedInference[Double, Sat[W.`"(> x 2.3)"`.T], Sat[W.`"(> x 2.1)"`.T]].isValid
  }

  property("(> x 2.1) =!> (> x 2.3)") = secure {
    TypedInference[Double, Sat[W.`"(> x 2.1)"`.T], Sat[W.`"(> x 2.3)"`.T]].notValid
  }

  property("autoInfer") = secure {
    val x: Int Refined Sat[W.`"(> x 2)"`.T] = 6
    val y: Int Refined Sat[W.`"(> x 0)"`.T] = x
    illTyped(
      """val z: Int Refined Sat[W.`"(> x 4)"`.T] = x""",
      """type mismatch \(invalid inference\).*"""
    )
    x == y
  }

  property("De Morgan's law 1 (substitution form, reversed)") = secure {
    type P = Sat[W.`"(and (> x 0) (< x 10))"`.T]
    type C = Sat[W.`"(not (or (not (> x 0)) (not (< x 10))))"`.T]
    TypedInference[Int, P, C].isValid
  }

  property("De Morgan's law 2 (substitution form, reversed)") = secure {
    type P = Sat[W.`"(or (< x 0) (> x 10))"`.T]
    type C = Sat[W.`"(not (and (not (< x 0)) (not (> x 10))))"`.T]
    TypedInference[Int, P, C].isValid
  }
}
