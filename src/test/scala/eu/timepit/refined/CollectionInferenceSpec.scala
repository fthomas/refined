package eu.timepit.refined

import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.implicits._
import eu.timepit.refined.numeric.Greater
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.tag.@@
import shapeless.test.illTyped

class CollectionInferenceSpec extends Properties("CollectionInference") {

  property("Exists[A] ==> Exists[B]") = secure {
    InferenceRule[Contains[W.`'5'`.T], Exists[Digit]].isValid
  }

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

  property("Head[A] ==> Head[B]") = secure {
    val a: String @@ Head[Digit] = refineLit("1a ")
    val b: String @@ Head[LetterOrDigit] = a
    a == b
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

  property("Index[N, B] ==> Index[N, B]") = secure {
    InferenceRule[Index[_1, Letter], Index[_1, LetterOrDigit]].isValid
  }

  property("Index ==> Exists") = secure {
    val a: String @@ Index[W.`1`.T, LowerCase] = refineLit("1a ")
    val b: String @@ Exists[LowerCase] = a
    a == b
  }

  property("Last[A] ==> Last[B]") = secure {
    InferenceRule[Last[Letter], Last[LetterOrDigit]].isValid
  }

  property("Last ==> Exists") = secure {
    val a: String @@ Last[Whitespace] = refineLit("1a ")
    val b: String @@ Exists[Whitespace] = a
    a == b
  }

  property("Last ==> NonEmpty") = secure {
    val a: String @@ Last[Whitespace] = refineLit("1a ")
    val b: String @@ NonEmpty = a
    a == b
  }

  property("NonEmpty =!> Last") = secure {
    illTyped("""
      val a: String @@ NonEmpty = refineLit("1a ")
      val b: String @@ Last[Whitespace] = a
      """)
    true
  }

  property("Length[A] ==> Length[B]") = secure {
    val a: String @@ Size[Greater[_5]] = refineLit("123456")
    val b: String @@ Size[Greater[_4]] = a
    a == b
  }
}
