package eu.timepit.refined

import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.tag.@@
import shapeless.test.illTyped

class SyntaxSpec extends Properties("syntax") {
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
    illTyped("testRefineLit(refineLit(-1))")
    illTyped("testRefineLit(refineLit[Positive](-1))")
    illTyped("testRefineLit(refineLit[Positive][Int](-1))")
    true
  }
}
