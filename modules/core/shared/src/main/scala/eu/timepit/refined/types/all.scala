package eu.timepit.refined.types

/** Module for all predefined refined types. */
object all extends AllTypes with AllTypesBinCompat1 with AllTypesBinCompat2 with AllTypesBinCompat3

trait AllTypes
    extends CharTypes
    with DigestTypes
    with NetTypes
    with NumericTypes
    with StringTypes
    with TimeTypes

trait AllTypesBinCompat1 extends NumericTypesBinCompat1

trait AllTypesBinCompat2 extends NumericTypesBinCompat2

trait AllTypesBinCompat3 extends StringTypesBinCompat1
