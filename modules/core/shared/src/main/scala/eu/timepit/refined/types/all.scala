package eu.timepit.refined.types

/** Module for all predefined refined types. */
object all extends AllTypes

trait AllTypes
    extends CharTypes
    with DigestTypes
    with NetTypes
    with NumericTypes
    with NumericTypesBinCompat1
    with StringTypes
    with TimeTypes
