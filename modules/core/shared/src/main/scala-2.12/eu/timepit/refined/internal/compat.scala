package eu.timepit.refined.internal

object compat {
  type ValueOf[A] = shapeless.Witness.Aux[A]
}
