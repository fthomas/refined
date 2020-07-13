package eu.timepit.refined.cats

import _root_.cats.Contravariant
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties

trait Encoder[A] {
  def encode(a: A): String
}

object Encoder {
  def apply[A](implicit e: Encoder[A]): Encoder[A] = e

  def instance[A](f: A => String): Encoder[A] =
    new Encoder[A] {
      override def encode(a: A): String = f(a)
    }

  implicit val encoderContravariant: Contravariant[Encoder] =
    new Contravariant[Encoder] {
      override def contramap[A, B](fa: Encoder[A])(f: B => A): Encoder[B] =
        instance(b => fa.encode(f(b)))
    }

  implicit val intEncoder: Encoder[Int] =
    instance(_.toString)
}

class RefTypeContravariantSpec extends Properties("Contravariant") {

  property("Encoder[Int]") = secure {
    Encoder[Int].encode(1) ?= "1"
  }

  property("derive Encoder[PosInt] via Contravariant[Encoder]") = secure {
    // This import is needed because of https://github.com/scala/bug/issues/10753
    import Encoder.encoderContravariant
    import eu.timepit.refined.cats.derivation._

    Encoder[PosInt].encode(PosInt.unsafeFrom(1)) ?= "1"
  }
}
