package eu.timepit.refined

import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.tag.@@
import shapeless.test.illTyped

class RefineSyntaxSpec extends Properties("refine syntax") {

  def testRefine(arg: Either[String, Int @@ Positive]): Boolean = true
  def testRefineMT(arg: Int @@ Positive): Boolean = true

  property("refine success") = secure {
    testRefine(refineT(1))
    testRefine(refineT[Positive](1))
    testRefine(refineT[Positive][Int](1))
  }

  property("refine failure") = secure {
    testRefine(refineT(-1))
    testRefine(refineT[Positive](-1))
    testRefine(refineT[Positive][Int](-1))
  }

  property("refineLit success") = secure {
    testRefineMT(refineMT(1))
    testRefineMT(refineMT[Positive](1))
    testRefineMT(refineMT[Positive][Int](1))
  }

  property("refineLit failure") = secure {
    illTyped("testRefineMT(refineMT(-1))", "could not find implicit value.*")
    illTyped("testRefineMT(refineMT[Positive](-1))", "Predicate.*fail.*")
    illTyped("testRefineMT(refineMT[Positive][Int](-1))", "Predicate.*fail.*")
    true
  }
}
