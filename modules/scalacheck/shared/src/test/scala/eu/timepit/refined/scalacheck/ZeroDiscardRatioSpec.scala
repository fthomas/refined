package eu.timepit.refined.scalacheck

import eu.timepit.refined.scalacheck.string._
import eu.timepit.refined.types.string.NonEmptyString
import org.scalacheck.Prop._
import org.scalacheck.{Properties, Test}

class ZeroDiscardRatioSpec extends Properties("ZeroDiscardRatioSpec") {
  override def overrideParameters(p: Test.Parameters): Test.Parameters =
    p.withMaxDiscardRatio(Float.MinPositiveValue)

  property("NonEmptyString") = forAll((_: NonEmptyString) => true)
}
