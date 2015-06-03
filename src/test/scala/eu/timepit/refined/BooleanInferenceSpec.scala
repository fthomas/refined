package eu.timepit.refined

import eu.timepit.refined.boolean._
import eu.timepit.refined.char.{ Digit, Letter, UpperCase, Whitespace }
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.tag.@@
import shapeless.test.illTyped

class BooleanInferenceSpec extends Properties("BooleanInference") {

  property("double negation elimination") = secure {
    val a: Char @@ Not[Not[UpperCase]] = refineLit('A')
    val b: Char @@ UpperCase = a
    a == b
  }

  property("double negation elimination 2x") = secure {
    val a: Char @@ Not[Not[Not[Not[UpperCase]]]] = refineLit('A')
    val b: Char @@ UpperCase = a
    a == b
  }

  property("double negation elimination 3x") = secure {
    val a: Char @@ Not[Not[Not[Not[Not[Not[UpperCase]]]]]] = refineLit('A')
    val b: Char @@ UpperCase = a
    a == b
  }

  property("double negation elimination 4x") = secure {
    val a: Char @@ Not[Not[Not[Not[Not[Not[Not[Not[UpperCase]]]]]]]] = refineLit('A')
    val b: Char @@ UpperCase = a
    a == b
  }

  property("double negation introduction") = secure {
    val a: Char @@ UpperCase = refineLit('A')
    val b: Char @@ Not[Not[UpperCase]] = a
    a == b
  }

  property("double negation introduction 2x") = secure {
    val a: Char @@ UpperCase = refineLit('A')
    val b: Char @@ Not[Not[Not[Not[UpperCase]]]] = a
    a == b
  }

  property("conjunction associativity") = secure {
    val a: Char @@ ((UpperCase And Letter) And Not[Whitespace]) = refineLit('A')
    val b: Char @@ (UpperCase And (Letter And Not[Whitespace])) = a
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

  property("conjunction introduction") = secure {
    illTyped("""
      val a: Char @@ UpperCase = refineLit('A')
      val b: Char @@ (UpperCase And Digit) = a
      """)
    true
  }

  property("disjunction associativity") = secure {
    val a: Char @@ ((UpperCase Or Letter) Or Digit) = refineLit('A')
    val b: Char @@ (UpperCase Or (Letter Or Digit)) = a
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

  property("disjunction elimination") = secure {
    illTyped("""
      val a: Char @@ UpperCase Or Digit = refineLit('A')
      val b: Char @@ Digit = a
      """)
    true
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

  /*
  // These do not typecheck yet:

  property("De Morgan's law 1 (substitution form)") = secure {
    val a: Char @@ (UpperCase And Letter) = refineLit('A')
    val b: Char @@ Not[Not[UpperCase] Or Not[Letter]] = a
    a == b
  }

  property("De Morgan's law 2 (substitution form)") = secure {
    val a: Char @@ (UpperCase Or Letter) = refineLit('f')
    val b: Char @@ Not[Not[UpperCase] And Not[Letter]] = a
    a == b
  }
  */

  property("Xor commutativity") = secure {
    val a: Char @@ (Letter Xor Digit) = refineLit('A')
    val b: Char @@ (Digit Xor Letter) = a
    a == b
  }

  property("modus tollens") = secure {
    val a: Char @@ Not[Digit Xor Letter] = refineLit(' ')
    val b: Char @@ Not[Letter Xor Digit] = a
    a == b
  }
}
