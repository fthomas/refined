package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.tag.@@
import shapeless.test.illTyped

class RefineSyntaxSpec extends Properties("refine syntax") {

  def testRefineV(arg: Either[String, Int Refined Positive]): Boolean = true
  def testRefineT(arg: Either[String, Int @@ Positive]): Boolean = true
  def testRefineMV(arg: Int Refined Positive): Boolean = true
  def testRefineMT(arg: Int @@ Positive): Boolean = true

  property("refineV success") = secure {
    testRefineV(refineV(1))
    testRefineV(refineV[Positive](1))
    testRefineV(refineV[Positive][Int](1))
  }

  property("refineV failure") = secure {
    testRefineV(refineV(-1))
    testRefineV(refineV[Positive](-1))
    testRefineV(refineV[Positive][Int](-1))
  }

  property("refineT success") = secure {
    testRefineT(refineT(1))
    testRefineT(refineT[Positive](1))
    testRefineT(refineT[Positive][Int](1))
  }

  property("refineT failure") = secure {
    testRefineT(refineT(-1))
    testRefineT(refineT[Positive](-1))
    testRefineT(refineT[Positive][Int](-1))
  }

  property("refineMV success") = secure {
    testRefineMV(1)
    testRefineMV(refineMV(1))
    testRefineMV(refineMV[Positive](1))
    testRefineMV(refineMV[Positive][Int](1))
  }

  property("refineMT success") = secure {
    testRefineMT(1)
    testRefineMT(refineMT(1))
    testRefineMT(refineMT[Positive](1))
    testRefineMT(refineMT[Positive][Int](1))
  }

  property("refineMV failure") = wellTyped {
    illTyped("testRefineMV(-1)", "Predicate.*fail.*")
    // We don't check the compiler error in this case because it changed with 2.13.2,
    // see https://github.com/fthomas/refined/issues/718.
    illTyped("testRefineMV(refineMV(-1))")
    illTyped("testRefineMV(refineMV[Positive](-1))", "Predicate.*fail.*")
    illTyped("testRefineMV(refineMV[Positive][Int](-1))", "Predicate.*fail.*")
  }

  property("refineMT failure") = wellTyped {
    illTyped("testRefineMT(-1)", "Predicate.*fail.*")
    // We don't check the compiler error in this case because it changed with 2.13.2,
    // see https://github.com/fthomas/refined/issues/718.
    illTyped("testRefineMT(refineMT(-1))")
    illTyped("testRefineMT(refineMT[Positive](-1))", "Predicate.*fail.*")
    illTyped("testRefineMT(refineMT[Positive][Int](-1))", "Predicate.*fail.*")
  }
}
