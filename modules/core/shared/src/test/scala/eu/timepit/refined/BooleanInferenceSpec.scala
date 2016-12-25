package eu.timepit.refined

import eu.timepit.refined.TestUtils.wellTyped
import eu.timepit.refined.api.Inference
import eu.timepit.refined.boolean._
import eu.timepit.refined.char.{Digit, Letter, UpperCase, Whitespace}
import eu.timepit.refined.numeric.Greater
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.test.illTyped

class BooleanInferenceSpec extends Properties("BooleanInference") {

  property("double negation elimination with Greater") = secure {
    Inference[Not[Not[Greater[_5]]], Greater[_4]] ?=
      Inference(5 > 4, "doubleNegationElimination(greaterInferenceNat(5, 4))")
  }

  property("double negation elimination") = secure {
    Inference[Not[Not[UpperCase]], UpperCase].isValid
  }

  property("double negation elimination 2x") = secure {
    Inference[Not[Not[Not[Not[UpperCase]]]], UpperCase].isValid
  }

  property("double negation elimination 3x") = secure {
    Inference[Not[Not[Not[Not[Not[Not[UpperCase]]]]]], UpperCase].isValid
  }

  property("double negation elimination 4x") = secure {
    Inference[Not[Not[Not[Not[Not[Not[Not[Not[UpperCase]]]]]]]], UpperCase].isValid
  }

  property("double negation introduction with Greater") = secure {
    Inference[Greater[_5], Not[Not[Greater[_4]]]].isValid
  }

  property("double negation introduction") = secure {
    Inference[UpperCase, Not[Not[UpperCase]]].isValid
  }

  property("double negation introduction 2x") = secure {
    Inference[UpperCase, Not[Not[Not[Not[UpperCase]]]]].isValid
  }

  property("conjunction associativity") = secure {
    Inference[(UpperCase And Letter) And Not[Whitespace],
              UpperCase And (Letter And Not[Whitespace])].isValid
  }

  property("conjunction commutativity") = secure {
    Inference[UpperCase And Letter, Letter And UpperCase].isValid
  }

  property("conjunction elimination left") = secure {
    Inference[UpperCase And Letter, UpperCase].isValid
  }

  property("conjunction elimination right") = secure {
    Inference[Letter And UpperCase, UpperCase].isValid
  }

  property("conjunction introduction") = wellTyped {
    illTyped("Inference[UpperCase, UpperCase And Digit]", "could not find.*Inference.*")
  }

  property("disjunction associativity") = secure {
    Inference[(UpperCase Or Letter) Or Digit, UpperCase Or (Letter Or Digit)].isValid
  }

  property("disjunction commutativity") = secure {
    Inference[UpperCase Or Letter, Letter Or UpperCase].isValid
  }

  property("disjunction introduction left") = secure {
    Inference[Digit, Digit Or Letter].isValid
  }

  property("disjunction introduction right") = secure {
    Inference[Digit, Letter Or Digit].isValid
  }

  property("disjunction elimination") = wellTyped {
    illTyped("Inference[UpperCase Or Digit, Digit]", "could not find.*Inference.*")
  }

  property("De Morgan's law 1") = secure {
    Inference[Not[UpperCase And Letter], Not[UpperCase] Or Not[Letter]].isValid
  }

  /*
  property("De Morgan's law 1 (reversed)") = secure {
    Inference[Not[UpperCase] Or Not[Letter], Not[UpperCase And Letter]].isValid
  }
   */

  property("De Morgan's law 2") = secure {
    Inference[Not[UpperCase Or Letter], Not[UpperCase] And Not[Letter]].isValid
  }

  /*
  property("De Morgan's law 2 (reversed)") = secure {
    Inference[Not[UpperCase] And Not[Letter], Not[UpperCase Or Letter]].isValid
  }
   */

  /*
  property("De Morgan's law 1 (substitution form)") = secure {
    Inference[Not[Not[UpperCase] Or Not[Letter]], UpperCase And Letter].isValid
  }
   */

  /*
  property("De Morgan's law 1 (substitution form, reversed)") = secure {
    Inference[UpperCase And Letter, Not[Not[UpperCase] Or Not[Letter]]].isValid
  }
   */

  /*
  property("De Morgan's law 2 (substitution form)") = secure {
    Inference[Not[Not[UpperCase] And Not[Letter]], UpperCase Or Letter].isValid
  }
   */

  /*
  property("De Morgan's law 2 (substitution form, reversed)") = secure {
    Inference[UpperCase Or Letter, Not[Not[UpperCase] And Not[Letter]]].isValid
  }
   */

  property("Xor commutativity") = secure {
    Inference[Letter Xor Digit, Digit Xor Letter].isValid
  }

  property("Nand commutativity") = secure {
    Inference[Letter Nand Digit, Digit Nand Letter].isValid
  }

  property("Nor commutativity") = secure {
    Inference[Letter Nor Digit, Digit Nor Letter].isValid
  }

  property("modus tollens") = secure {
    Inference[Not[Digit Xor Letter], Not[Letter Xor Digit]] ?=
      Inference.alwaysValid("modusTollens(xorCommutativity)")
  }
}
