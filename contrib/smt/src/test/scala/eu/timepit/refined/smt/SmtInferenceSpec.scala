package eu.timepit.refined
package smt

import eu.timepit.refined.api.{ Refined, TypedInference }
import eu.timepit.refined.auto._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.test.illTyped

class SmtInferenceSpec extends Properties("SmtInference") {

  property("(> x 10) ==> (> x 0)") = secure {
    TypedInference[Int, Smt[W.`"(> x 10)"`.T], Smt[W.`"(> x 0)"`.T]].isValid
  }

  property("(> x 0) =!> (> x 10)") = secure {
    TypedInference[Int, Smt[W.`"(> x 0)"`.T], Smt[W.`"(> x 10)"`.T]].notValid
  }

  property("(> x 2.3) ==> (> x 2.1)") = secure {
    TypedInference[Double, Smt[W.`"(> x 2.3)"`.T], Smt[W.`"(> x 2.1)"`.T]].isValid
  }

  property("(> x 2.1) =!> (> x 2.3)") = secure {
    TypedInference[Double, Smt[W.`"(> x 2.1)"`.T], Smt[W.`"(> x 2.3)"`.T]].notValid
  }

  property("autoInfer") = secure {
    val x: Int Refined Smt[W.`"(> x 2)"`.T] = 6
    val y: Int Refined Smt[W.`"(> x 0)"`.T] = x
    illTyped("""val z: Int Refined Smt[W.`"(> x 4)"`.T] = x""", "(?s)invalid inference.*")
    x == y
  }

  property("De Morgan's law 1 (substitution form, reversed)") = secure {
    type P = Smt[W.`"(and (> x 0) (< x 10))"`.T]
    type C = Smt[W.`"(not (or (not (> x 0)) (not (< x 10))))"`.T]
    TypedInference[Int, P, C].isValid
  }

  property("De Morgan's law 2 (substitution form, reversed)") = secure {
    type P = Smt[W.`"(or (< x 0) (> x 10))"`.T]
    type C = Smt[W.`"(not (and (not (< x 0)) (not (> x 10))))"`.T]
    TypedInference[Int, P, C].isValid
  }
}
