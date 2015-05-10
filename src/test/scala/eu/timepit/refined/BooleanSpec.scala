package eu.timepit.refined

import eu.timepit.refined.boolean._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class BooleanSpec extends Properties("boolean") {
  property("True") = secure {
    implicitly[Predicate[True, Int]].isValid(0)
  }

  property("Not[True]") = secure {
    implicitly[Predicate[Not[True], Int]].isInvalid(0)
  }

  property("False") = secure {
    implicitly[Predicate[False, Int]].isInvalid(0)
  }

  property("Not[False]") = secure {
    implicitly[Predicate[Not[False], Int]].isValid(0)
  }

  property("And") = secure {
    implicitly[Predicate[False And False, Int]].isInvalid(0) &&
      implicitly[Predicate[False And True, Int]].isInvalid(0) &&
      implicitly[Predicate[True And False, Int]].isInvalid(0) &&
      implicitly[Predicate[True And True, Int]].isValid(0)
  }

  property("Or") = secure {
    implicitly[Predicate[False Or False, Int]].isInvalid(0) &&
      implicitly[Predicate[False Or True, Int]].isValid(0) &&
      implicitly[Predicate[True Or False, Int]].isValid(0) &&
      implicitly[Predicate[True Or True, Int]].isValid(0)
  }
}
