package eu.timepit.refined
package smt


trait Formula[P] extends Serializable {

  def subFormula(x: String): SubFormula
}

object Formula {

  def apply[P](implicit f: Formula[P]): Formula[P] = f

  def instance[P](f: String => SubFormula): Formula[P] =
    new Formula[P] {
      override def subFormula(x: String): SubFormula = f(x)
    }

  def simple[P](f: String => String): Formula[P] =
    instance(x => SubFormula(f(x)))

  implicit def default[P]: Formula[P] =
      instance(x => SubFormula("", List("")))

  implicit def pair[A, B](implicit fa: Formula[A], fb: Formula[B]): (Formula[A], Formula[B]) =
    (fa, fb)
}
