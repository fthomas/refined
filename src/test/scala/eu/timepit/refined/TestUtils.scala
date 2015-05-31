package eu.timepit.refined

object TestUtils {
  def consistent[P, T](p: Predicate[P, T]): T => Boolean =
    t => p.isValid(t) == p.validated(t).isEmpty
}
