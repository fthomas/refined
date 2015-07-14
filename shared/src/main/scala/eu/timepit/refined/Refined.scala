package eu.timepit.refined

final case class Refined[T, P](get: T) extends AnyVal
