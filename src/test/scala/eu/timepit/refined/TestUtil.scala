package eu.timepit.refined

object TestUtil {
  def consistent[P, T](p: Predicate[P, T]): T => Boolean =
    t => {
      val isValid = p.isValid(t)
      val validated = p.validated(t).isEmpty
      isValid == validated && isValid != p.notValid(t)
    }
}
