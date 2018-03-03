// Copyright: 2018 Sam Halliday
// License: https://opensource.org/licenses/MIT

package eu.timepit.refined.scalaz

import _root_.scalaz.{@@, \/, MonadError}
import _root_.scalaz.syntax.bind._
import _root_.scalaz.syntax.either._
import eu.timepit.refined.api._
import eu.timepit.refined.collection._
import eu.timepit.refined.scalaz._
import org.scalacheck._
import org.scalacheck.Prop._

trait Decoder[A] {
  def decode(s: String): String \/ A
}
object Decoder {
  @inline def apply[A](implicit A: Decoder[A]): Decoder[A] = A
  @inline def instance[A](f: String => String \/ A): Decoder[A] =
    new Decoder[A] {
      override def decode(s: String): String \/ A = f(s)
    }

  implicit val string: Decoder[String] = instance(_.right)

  implicit val monad: MonadError[Decoder, String] =
    new MonadError[Decoder, String] {
      def point[A](a: => A): Decoder[A] = instance(_ => a.right)
      def raiseError[A](e: String): Decoder[A] = instance(_ => e.left)

      def bind[A, B](fa: Decoder[A])(f: A => Decoder[B]): Decoder[B] =
        instance(s => fa.decode(s) >>= (f(_).decode(s)))

      // grr... https://github.com/scalaz/scalaz/issues/1657
      // and... https://github.com/scalaz/scalaz/issues/1659
      def handleError[A](fa: Decoder[A])(f: String => Decoder[A]): Decoder[A] =
        instance(a => (fa.decode(a).swap >>= (f(_).decode(a).swap)).swap)
    }
}

class RefTypeSpecScalazMonadError extends Properties("scalaz.Contravariant") {
  // annoying that this import is needed!
  // https://github.com/scala/bug/issues/10753#issuecomment-369592913
  import Decoder.monad

  property("Refined via scalaz.MonadError[?, String]") = secure {
    val decoder = Decoder[String Refined NonEmpty]
    decoder.decode("").isLeft && decoder.decode("hello world").isRight
  }

  property("@@ via scalaz.MonadError[?, String]") = secure {
    val decoder = Decoder[String @@ NonEmpty]
    decoder.decode("").isLeft && decoder.decode("hello world").isRight
  }

}
