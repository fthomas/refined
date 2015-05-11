package eu.timepit.refined

import eu.timepit.refined.boolean._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class BooleanSpec extends Properties("boolean") {
  property("True") = secure {
    Predicate[True, Int].isValid(0)
  }

  property("Not[True]") = secure {
    Predicate[Not[True], Int].notValid(0)
  }

  property("False") = secure {
    Predicate[False, Int].notValid(0)
  }

  property("Not[False]") = secure {
    Predicate[Not[False], Int].isValid(0)
  }

  property("And") = secure {
    Predicate[False And False, Int].notValid(0) &&
      Predicate[False And True, Int].notValid(0) &&
      Predicate[True And False, Int].notValid(0) &&
      Predicate[True And True, Int].isValid(0)
  }

  property("Or") = secure {
    Predicate[False Or False, Int].notValid(0) &&
      Predicate[False Or True, Int].isValid(0) &&
      Predicate[True Or False, Int].isValid(0) &&
      Predicate[True Or True, Int].isValid(0)
  }
}
