package eu.timepit.refined

import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.tag.@@
import shapeless.test.illTyped

class RefineSyntaxSpec extends Properties("refine syntax") {
  def testRefine(arg: Either[String, Int @@ Positive]): Boolean = true
  def testRefineLit(arg: Int @@ Positive): Boolean = true

  property("refine success w/ types") = secure {
    testRefine(refine[Positive, Int](1))
  }

  property("refine failure w/ types") = secure {
    testRefine(refine[Positive, Int](0))
  }

  property("refine success w/o types") = secure {
    testRefine(refine(1))
  }

  property("refine failure w/o types") = secure {
    testRefine(refine(0))
  }

  property("refineLit success w/ types") = secure {
    testRefineLit(refineLit[Positive, Int](1))
  }

  property("refineLit failure w/ types") = secure {
    illTyped("testRefineLit(refineLit[Positive, Int](0))")
    true
  }

  property("refineLit success w/o types") = secure {
    testRefineLit(refineLit(1))
  }

  property("refineLit failure w/o types") = secure {
    illTyped("testRefineLit(refineLit(0))")
    true
  }
}
