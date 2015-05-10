package refined

trait Predicate[P, X] {
  def validate(p: P, x: X): Option[String]
}
