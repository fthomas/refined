package eu.timepit.refined
package smt

// call this Proposition
// the combination of two propositions to do inference is an implication?
// definitions could also be declarations
final case class SubFormula(expr: String, definitions: List[String] = List.empty)
