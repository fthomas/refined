// Copyright: 2018 Sam Halliday
// License: https://opensource.org/licenses/MIT

package eu.timepit.refined.scalaz

import _root_.scalaz.{@@, Contravariant}
import eu.timepit.refined.api._
import eu.timepit.refined.collection._
import org.scalacheck.Prop._
import org.scalacheck.Properties

trait Encoder[A] {
  def encode(a: A): String
}
object Encoder {
  @inline def apply[A](implicit A: Encoder[A]): Encoder[A] = A
  @inline def instance[A](f: A => String): Encoder[A] = new Encoder[A] {
    override def encode(a: A): String = f(a)
  }

  implicit val string: Encoder[String] = instance(identity)

  implicit val contravariant: Contravariant[Encoder] =
    new Contravariant[Encoder] {
      override def contramap[A, B](fa: Encoder[A])(f: B => A): Encoder[B] =
        instance(b => fa.encode(f(b)))
    }
}

class RefTypeSpecScalazContravariant extends Properties("scalaz.Contravariant") {
  // annoying that this import is needed!
  // https://github.com/scala/bug/issues/10753#issuecomment-369592913
  import Encoder._
  import eu.timepit.refined.scalaz.derivation._

  property("Refined via scalaz.Contravariant") = secure {
    import eu.timepit.refined.auto._

    val x: String Refined NonEmpty = "hello world"

    Encoder[String Refined NonEmpty].encode(x) == "hello world"
  }

  property("@@ via scalaz.Contravariant") = secure {
    import eu.timepit.refined.scalaz.auto._

    val x: String @@ NonEmpty = "hello world"

    Encoder[String @@ NonEmpty].encode(x) == "hello world"
  }

}
