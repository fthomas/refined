package eu.timepit.refined

import eu.timepit.refined.api._
import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.api.RefType.applyRef
import eu.timepit.refined.auto._
import eu.timepit.refined.boolean._
import eu.timepit.refined.numeric.{Greater, Positive}
import org.scalacheck.Prop._
import org.scalacheck.Properties

class ImplicitInferenceSpec extends Properties("ImplicitInference") {

  private def convert[B, A]: PartiallyApplied[B, A] = new PartiallyApplied[B, A]

  private class PartiallyApplied[B, A] {
    final def apply[T](value: T)(implicit I: B ==> A,
                                 V: Validate[T, B]): Either[String, T Refined A] = {
      val res = applyRef[T Refined B](value)
      res.map(v => v: T Refined A)
    }
  }

  property("Convert with AlwaysValid Inference") = secure {
    type RefT1 = Positive And Greater[W.`10`.T]
    type RefT1Inverse = Greater[W.`10`.T] And Positive
    type T1 = Int Refined RefT1
    type T1Inverse = Int Refined RefT1Inverse
    val expected: T1Inverse = 12

    convert[RefT1, RefT1Inverse](12) == Right(expected)
  }
}
