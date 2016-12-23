package eu.timepit.refined.pureconfig.error

final case class PredicateFailedException(message: String)
    extends IllegalArgumentException(message)
