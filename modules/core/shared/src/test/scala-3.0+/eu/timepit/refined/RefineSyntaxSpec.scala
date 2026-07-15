package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import eu.timepit.refined.test.ScalaVersionSpecific.illTyped

// Ported from the Scala 2 `RefineSyntaxSpec`. The `refineT`/`refineMT` (shapeless `@@`) variants are
// dropped, as are the point-free/curried `refineV`/`refineMV` application forms — on Scala 3 these are
// builder methods that require the predicate type argument (`refineV[P](t)` / `refineMV[P](t)`).
class RefineSyntaxSpec extends Properties("refine syntax") {

  def testRefineV(arg: Either[String, Int Refined Positive]): Boolean = true
  def testRefineMV(arg: Int Refined Positive): Boolean = true

  property("refineV success") = secure {
    testRefineV(refineV[Positive](1))
  }

  property("refineV failure") = secure {
    testRefineV(refineV[Positive](-1))
  }

  property("refineMV success") = secure {
    testRefineMV(1) // via autoRefineV
    testRefineMV(refineMV[Positive](1))
  }

  property("refineMV failure") = wellTyped {
    // `testRefineMV(-1)` fails because the `autoRefineV` conversion is rejected → type mismatch;
    // `refineMV[Positive](-1)` is a direct macro call → the "Predicate failed" abort surfaces.
    illTyped("testRefineMV(-1)", "Required: Int Refined .*Positive")
    illTyped("testRefineMV(refineMV[Positive](-1))", "Predicate.*fail.*")
  }
}
