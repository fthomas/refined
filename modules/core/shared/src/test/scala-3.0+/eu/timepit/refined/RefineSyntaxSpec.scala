package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import eu.timepit.refined.test.ScalaVersionSpecific.illTyped

// Ported from the Scala 2 `RefineSyntaxSpec`. Only the `refineT`/`refineMT` (shapeless `@@`) variants
// are dropped. The point-free/curried `refineV`/`refineMV` forms work just like Scala 2 — the
// predicate `P` is inferred from the expected type (here the `testRefineV`/`testRefineMV` parameter).
class RefineSyntaxSpec extends Properties("refine syntax") {

  def testRefineV(arg: Either[String, Int Refined Positive]): Boolean = true
  def testRefineMV(arg: Int Refined Positive): Boolean = true

  property("refineV success") = secure {
    testRefineV(refineV(1))
    testRefineV(refineV[Positive](1))
    testRefineV(refineV[Positive][Int](1))
  }

  property("refineV failure") = secure {
    // `refineV` is a runtime refinement (returns `Either`), so these compile and yield `Left` at runtime.
    testRefineV(refineV(-1))
    testRefineV(refineV[Positive](-1))
    testRefineV(refineV[Positive][Int](-1))
  }

  property("refineMV success") = secure {
    testRefineMV(1) // via autoRefineV
    testRefineMV(refineMV(1))
    testRefineMV(refineMV[Positive](1))
    testRefineMV(refineMV[Positive][Int](1))
  }

  property("refineMV failure") = wellTyped {
    // A rejected `autoRefineV` conversion surfaces as a type mismatch against the required refined
    // type; a direct `refineMV[...]` macro call surfaces the "Predicate failed" abort. (`typeCheckErrors`
    // exposes the macro message only for the direct call — see ScalaVersionSpecific.illTyped.)
    illTyped("testRefineMV(-1)", "Required: Int Refined .*Positive")
    illTyped("testRefineMV(refineMV(-1))")
    illTyped("testRefineMV(refineMV[Positive](-1))", "Predicate.*fail.*")
    illTyped("testRefineMV(refineMV[Positive][Int](-1))", "Predicate.*fail.*")
  }
}
