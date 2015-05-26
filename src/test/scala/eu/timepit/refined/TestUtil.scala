package eu.timepit.refined

object TestUtil {
  def consistent[P, T](p: Predicate[P, T]): T => Boolean =
    t => p.isValid(t) == p.validated(t).isEmpty
}
