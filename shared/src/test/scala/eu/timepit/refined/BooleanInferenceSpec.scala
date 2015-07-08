package eu.timepit.refined

import eu.timepit.refined.boolean._
import eu.timepit.refined.char.{ Digit, Letter, UpperCase, Whitespace }
import eu.timepit.refined.numeric.Greater
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.test.illTyped

class BooleanInferenceSpec extends Properties("BooleanInference") {

  property("double negation elimination with Greater") = secure {
    InferenceRule[Not[Not[Greater[_5]]], Greater[_4]] ?=
      InferenceRule(5 > 4, "doubleNegationElimination(greaterInferenceNat(5, 4))")
  }

  property("double negation elimination") = secure {
    InferenceRule[Not[Not[UpperCase]], UpperCase].isValid
  }

  property("double negation elimination 2x") = secure {
    InferenceRule[Not[Not[Not[Not[UpperCase]]]], UpperCase].isValid
  }

  property("double negation elimination 3x") = secure {
    InferenceRule[Not[Not[Not[Not[Not[Not[UpperCase]]]]]], UpperCase].isValid
  }

  property("double negation elimination 4x") = secure {
    InferenceRule[Not[Not[Not[Not[Not[Not[Not[Not[UpperCase]]]]]]]], UpperCase].isValid
  }

  property("double negation introduction with Greater") = secure {
    InferenceRule[Greater[_5], Not[Not[Greater[_4]]]].isValid
  }

  property("double negation introduction") = secure {
    InferenceRule[UpperCase, Not[Not[UpperCase]]].isValid
  }

  property("double negation introduction 2x") = secure {
    InferenceRule[UpperCase, Not[Not[Not[Not[UpperCase]]]]].isValid
  }

  property("conjunction associativity") = secure {
    InferenceRule[(UpperCase And Letter) And Not[Whitespace], UpperCase And (Letter And Not[Whitespace])].isValid
  }

  property("conjunction commutativity") = secure {
    InferenceRule[UpperCase And Letter, Letter And UpperCase].isValid
  }

  property("conjunction elimination left") = secure {
    InferenceRule[UpperCase And Letter, UpperCase].isValid
  }

  property("conjunction elimination right") = secure {
    InferenceRule[Letter And UpperCase, UpperCase].isValid
  }

  property("conjunction introduction") = secure {
    illTyped("InferenceRule[UpperCase, UpperCase And Digit]", "could not find.*InferenceRule.*")
    true
  }

  property("disjunction associativity") = secure {
    InferenceRule[(UpperCase Or Letter) Or Digit, UpperCase Or (Letter Or Digit)].isValid
  }

  property("disjunction commutativity") = secure {
    InferenceRule[UpperCase Or Letter, Letter Or UpperCase].isValid
  }

  property("disjunction introduction left") = secure {
    InferenceRule[Digit, Digit Or Letter].isValid
  }

  property("disjunction introduction right") = secure {
    InferenceRule[Digit, Letter Or Digit].isValid
  }

  property("disjunction elimination") = secure {
    illTyped("InferenceRule[UpperCase Or Digit, Digit]", "could not find.*InferenceRule.*")
    true
  }

  property("De Morgan's law 1") = secure {
    InferenceRule[Not[UpperCase And Letter], Not[UpperCase] Or Not[Letter]].isValid
  }

  /*
  property("De Morgan's law 1 (reversed)") = secure {
    InferenceRule[Not[UpperCase] Or Not[Letter], Not[UpperCase And Letter]].isValid
  }
  */

  property("De Morgan's law 2") = secure {
    InferenceRule[Not[UpperCase Or Letter], Not[UpperCase] And Not[Letter]].isValid
  }

  /*
  property("De Morgan's law 2 (reversed)") = secure {
    InferenceRule[Not[UpperCase] And Not[Letter], Not[UpperCase Or Letter]].isValid
  }
  */

  /*
  property("De Morgan's law 1 (substitution form)") = secure {
    InferenceRule[Not[Not[UpperCase] Or Not[Letter]], UpperCase And Letter].isValid
  }
  */

  /*
  property("De Morgan's law 1 (substitution form, reversed)") = secure {
    InferenceRule[UpperCase And Letter, Not[Not[UpperCase] Or Not[Letter]]].isValid
  }
  */

  /*
  property("De Morgan's law 2 (substitution form)") = secure {
    InferenceRule[Not[Not[UpperCase] And Not[Letter]], UpperCase Or Letter].isValid
  }
  */

  /*
  property("De Morgan's law 2 (substitution form, reversed)") = secure {
    InferenceRule[UpperCase Or Letter, Not[Not[UpperCase] And Not[Letter]]].isValid
  }
  */

  property("Xor commutativity") = secure {
    InferenceRule[Letter Xor Digit, Digit Xor Letter].isValid
  }

  property("modus tollens") = secure {
    InferenceRule[Not[Digit Xor Letter], Not[Letter Xor Digit]] ?=
      InferenceRule.alwaysValid("modusTollens(xorCommutativity)")
  }
}
