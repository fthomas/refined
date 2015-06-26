package eu.timepit.refined

import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.tag.@@
import shapeless.test.illTyped

class RefineSyntaxSpec extends Properties("refine syntax") {
  def testRefine(arg: Either[String, Int @@ Positive]): Boolean = true
  def testRefineLit(arg: Int @@ Positive): Boolean = true

  property("refine success") = secure {
    testRefine(refine(1))
    testRefine(refine[Positive](1))
    testRefine(refine[Positive][Int](1))
  }

  property("refine failure") = secure {
    testRefine(refine(-1))
    testRefine(refine[Positive](-1))
    testRefine(refine[Positive][Int](-1))
  }

  property("refineLit success") = secure {
    testRefineLit(refineLit(1))
    testRefineLit(refineLit[Positive](1))
    testRefineLit(refineLit[Positive][Int](1))
  }

  property("refineLit failure") = secure {
    illTyped("testRefineLit(refineLit(-1))", "could not find implicit value.*")
    illTyped("testRefineLit(refineLit[Positive](-1))", "Predicate.*fail.*")
    illTyped("testRefineLit(refineLit[Positive][Int](-1))", "Predicate.*fail.*")
    true
  }
}
