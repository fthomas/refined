// Copyright: 2018 Sam Halliday
// License: https://opensource.org/licenses/MIT

package eu.timepit.refined.scalaz

import _root_.scalaz._, Scalaz._
import eu.timepit.refined.api._
import eu.timepit.refined.collection._
import org.scalacheck._
import org.scalacheck.Prop._

trait Decoder[A] {
  def decode(s: String): String \/ A
}
object Decoder {
  @inline def apply[A](implicit A: Decoder[A]): Decoder[A] = A

  implicit val string: Decoder[String] = _.right

  implicit val monad: MonadError[Decoder, String] =
    new MonadError[Decoder, String] {
      def point[A](a: => A): Decoder[A] = _ => a.right
      def raiseError[A](e: String): Decoder[A] = _ => e.left

      def bind[A, B](fa: Decoder[A])(f: A => Decoder[B]): Decoder[B] =
        (s => fa.decode(s) >>= (f(_).decode(s)))

      // grr... https://github.com/scalaz/scalaz/issues/1657
      // and... https://github.com/scalaz/scalaz/issues/1659
      def handleError[A](fa: Decoder[A])(f: String => Decoder[A]): Decoder[A] =
        (a => (fa.decode(a).swap >>= (f(_).decode(a).swap)).swap)
    }
}

class RefTypeSpecScalazMonadError extends Properties("scalaz.Contravariant") {
  // annoying that this import is needed!
  // https://github.com/scala/bug/issues/10753#issuecomment-369592913
  import Decoder._

  property("Refined via scalaz.MonadError[?, String]") = secure {
    val decoder = Decoder[String Refined NonEmpty]
    decoder.decode("").isLeft && decoder.decode("hello world").isRight
  }

  property("@@ via scalaz.MonadError[?, String]") = secure {
    val decoder = Decoder[String @@ NonEmpty]
    decoder.decode("").isLeft && decoder.decode("hello world").isRight
  }

}
