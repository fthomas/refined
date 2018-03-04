// Copyright: 2018 Sam Halliday
// License: https://opensource.org/licenses/MIT

package eu.timepit.refined.scalaz

import _root_.scalaz.{@@, \/, IsomorphismMonadError, MonadError, ReaderT}
import _root_.scalaz.Isomorphism.{<~>, IsoFunctorTemplate}
import _root_.scalaz.syntax.either._
import eu.timepit.refined.api._
import eu.timepit.refined.collection._
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

  type Out[a] = String \/ a
  type MT[a] = ReaderT[Out, String, a]
  implicit val isoReaderT: Decoder <~> MT =
    new IsoFunctorTemplate[Decoder, MT] {
      def from[A](fa: MT[A]) = instance(fa.run(_))
      def to[A](fa: Decoder[A]) = ReaderT[Out, String, A](fa.decode)
    }
  // will be cleaner after https://github.com/scalaz/scalaz/pull/1661
  private[this] val ME = MonadError[MT, String]
  implicit def monad: MonadError[Decoder, String] =
    new IsomorphismMonadError[Decoder, MT, String] {
      override implicit val G: MonadError[MT, String] = ME
      override val iso: Decoder <~> MT = isoReaderT
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
