package eu.timepit.refined

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.boolean._
import eu.timepit.refined.collection._
import eu.timepit.refined.impure.NonNull
import eu.timepit.refined.numeric._
import eu.timepit.refined.string._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class ImpureValidateSpec extends Properties("ImpureValidate") {

  property("NonNull safe for String") = secure {
    import eu.timepit.refined.collection.NonEmpty

    val string: String = null
    notValid[NonNull[NonEmpty]](string)
  }

  property("NonNull.showResult - String - invalid") = secure {
    import eu.timepit.refined.collection.NonEmpty

    val string: String = null
    showResult[NonNull[NonEmpty]](string) ?= "Predicate failed: source is null."
  }

  property("NonNull safe for List") = secure {
    val list: List[Int] = null
    notValid[NonNull[NonEmpty]](list)
  }

  property("NonNull valid String") = forAll { string: String =>
    string.nonEmpty ==>
      isValid[NonNull[NonEmpty]](string)
  }

  property("NonNull.showResult - String - left valid") = secure {
    showResult[NonNull[NonEmpty]]("") ?= "Predicate failed: Left predicate of NonNull passed failed: !isEmpty()."
  }

  property("NonNull.showResult - String - both valid") = forAll { string: String =>
    string.nonEmpty ==>
      (showResult[NonNull[NonEmpty]](string) ?=
        s"Predicate passed: Both predicates of NonNull and !isEmpty($string) passed..")
  }

  property("NonNull.showResult - List - invalid") = forAll { list: List[Int] =>
    list.nonEmpty ==>
      isValid[NonNull[NonEmpty]](list)
  }

  property("NonNull safe for non nullable values") = forAll { int: Int =>
    (int > 0) ==>
      isValid[NonNull[Positive]](int)
  }

  property("NonNull compound String refinements") = secure {
    isValid[NonNull[StartsWith[W.`"r"`.T] And EndsWith[W.`"e"`.T]]]("refine")
  }

}
