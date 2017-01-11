package eu.timepit.refined.types

/** Module for all predefined refined types. */
object all extends AllTypes

trait AllTypes extends CharTypes with NetTypes with NumericTypes with StringTypes with TimeTypes
