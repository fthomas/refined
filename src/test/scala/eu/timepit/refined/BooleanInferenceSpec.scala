package eu.timepit.refined

import eu.timepit.refined.boolean._
import eu.timepit.refined.char.{ Digit, Letter, UpperCase }
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.tag.@@

class BooleanInferenceSpec extends Properties("BooleanInference") {

  property("double negation elimination") = secure {
    val a: Char @@ Not[Not[UpperCase]] = refineLit('A')
    val b: Char @@ UpperCase = a
    a == b
  }

  property("double negation introduction") = secure {
    val a: Char @@ UpperCase = refineLit('A')
    val b: Char @@ Not[Not[UpperCase]] = a
    a == b
  }

  property("conjunction commutativity") = secure {
    val a: Char @@ (UpperCase And Letter) = refineLit('A')
    val b: Char @@ (Letter And UpperCase) = a
    a == b
  }

  property("conjunction elimination left") = secure {
    val a: Char @@ (UpperCase And Letter) = refineLit('A')
    val b: Char @@ UpperCase = a
    a == b
  }

  property("conjunction elimination right") = secure {
    val a: Char @@ (Letter And UpperCase) = refineLit('A')
    val b: Char @@ UpperCase = a
    a == b
  }

  property("disjunction commutativity") = secure {
    val a: Char @@ (UpperCase Or Letter) = refineLit('A')
    val b: Char @@ (Letter Or UpperCase) = a
    a == b
  }

  property("disjunction introduction left") = secure {
    val a: Char @@ Digit = refineLit('5')
    val b: Char @@ (Digit Or Letter) = a
    a == b
  }

  property("disjunction introduction right") = secure {
    val a: Char @@ Digit = refineLit('5')
    val b: Char @@ (Letter Or Digit) = a
    a == b
  }

  property("De Morgan's law 1") = secure {
    val a: Char @@ Not[UpperCase And Letter] = refineLit('a')
    val b: Char @@ (Not[UpperCase] Or Not[Letter]) = a
    a == b
  }

  property("De Morgan's law 2") = secure {
    val a: Char @@ Not[UpperCase Or Letter] = refineLit('5')
    val b: Char @@ (Not[UpperCase] And Not[Letter]) = a
    a == b
  }

  property("Xor commutativity") = secure {
    val a: Char @@ (Letter Xor Digit) = refineLit('A')
    val b: Char @@ (Digit Xor Letter) = a
    a == b
  }
}
