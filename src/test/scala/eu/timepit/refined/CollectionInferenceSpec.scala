package eu.timepit.refined

import eu.timepit.refined.char.{ Digit, LowerCase, Whitespace }
import eu.timepit.refined.collection._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.tag.@@
import shapeless.test.illTyped

class CollectionInferenceSpec extends Properties("CollectionInference") {

  property("Exists ==> NonEmpty") = secure {
    val a: String @@ Exists[Digit] = refineLit("1a ")
    val b: String @@ NonEmpty = a
    a == b
  }

  property("NonEmpty =!> Exists") = secure {
    illTyped("""
      val a: String @@ NonEmpty = refineLit("abc")
      val b: String @@ Exists[Digit] = a
      """)
    true
  }

  property("Head ==> Exists") = secure {
    val a: String @@ Head[Digit] = refineLit("1a ")
    val b: String @@ Exists[Digit] = a
    a == b
  }

  property("Exists =!> Head") = secure {
    illTyped("""
      val a: String @@ Exists[Digit] = refineLit("1a ")
      val b: String @@ Head[Digit] = a
      """)
    true
  }

  property("Index ==> Exists") = secure {
    val a: String @@ Index[W.`1`.T, LowerCase] = refineLit("1a ")
    val b: String @@ Exists[LowerCase] = a
    a == b
  }

  property("Last ==> Exists") = secure {
    val a: String @@ Last[Whitespace] = refineLit("1a ")
    val b: String @@ Exists[Whitespace] = a
    a == b
  }
}
