package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.{Inference, Validate}
import org.scalacheck.Properties

/**
 * Tests that ensure that `Validate` and `Inference` instances of
 * predicates are in their implicit scope and do not need to be imported
 * explicitly.
 */
class ImplicitScopeSpec extends Properties("implicit scope") {

  property("Validate[Char, LetterOrDigit]") = wellTyped {
    Validate[Char, char.LetterOrDigit]
  }

  property("Validate[Int, Positive]") = wellTyped {
    Validate[Int, numeric.Positive]
  }

  property("Validate[Int, NonPositive]") = wellTyped {
    Validate[Int, numeric.NonPositive]
  }

  property("Validate[Int, Interval.Closed[0, 10]]") = wellTyped {
    Validate[Int, numeric.Interval.Closed[W.`0`.T, W.`10`.T]]
  }

  property("Inference[And[UpperCase, Letter], And[Letter, UpperCase]]") = wellTyped {
    Inference[boolean.And[char.UpperCase, char.Letter], boolean.And[char.Letter, char.UpperCase]]
  }

  property("Inference[Greater[1], Greater[0]]") = wellTyped {
    Inference[numeric.Greater[W.`1`.T], numeric.Greater[W.`0`.T]]
  }
}
