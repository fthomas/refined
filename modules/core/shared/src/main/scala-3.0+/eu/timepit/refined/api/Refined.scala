package eu.timepit.refined.api

opaque type Refined[T, P] = T

object Refined {

  def unsafeApply[T, P](t: T): Refined[T, P] = t

  def unapply[T, P](r: Refined[T, P]): Some[T] = Some(r.value)

  extension [T, P](r: Refined[T, P]) {
    def value: T = r
  }
}
