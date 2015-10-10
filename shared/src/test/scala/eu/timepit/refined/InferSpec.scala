package eu.timepit.refined

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.char._
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.StartsWith
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.test.illTyped

class InferSpec extends Properties("infer") {

  property("Equal[_] ==> Digit") = secure {
    val a: Char Refined Equal[W.`'0'`.T] = '0'
    val b: Char Refined Digit = a
    illTyped("val c: Char Refined UpperCase = a", "invalid inference.*")
    a == b
  }

  property("GreaterEqual[A] ==> GreaterEqual[B]") = secure {
    val a: Double Refined GreaterEqual[W.`0.6`.T] = 0.6
    val b: Double Refined GreaterEqual[W.`0.5`.T] = a
    illTyped("val c: Double Refined GreaterEqual[W.`0.7`.T] = a", "invalid inference.*")
    a == b
  }

  property("Greater[Nat] ==> Greater[Nat]") = secure {
    val a: Int Refined Greater[_6] = 7
    val b: Int Refined Greater[_5] = a
    illTyped("val c: Int Refined Greater[_7] = a", "invalid inference.*")
    a == b
  }

  property("StartsWith[A] ==> StartsWith[B]") = secure {
    val a: String Refined StartsWith[W.`"abc"`.T] = "abcd"
    val b: String Refined StartsWith[W.`"ab"`.T] = a
    illTyped("val c: String Refined StartsWith[W.`\"abcde\"`.T] = a", "invalid inference.*")
    a == b
  }

  property("Equal[Nat] ==> Greater[A] ==> unrefined") = secure {
    val a: Double Refined Equal[_10] = 10.0
    val b: Double Refined Greater[W.`5.0`.T] = a
    val c: Double = b
    (a == b) && (b.get == c)
  }
}
