package eu.timepit.refined
package smt

final case class SubFormula(expr: String, definitions: List[String] = List.empty)
