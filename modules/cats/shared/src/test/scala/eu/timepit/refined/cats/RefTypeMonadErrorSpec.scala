package eu.timepit.refined.cats

import _root_.cats.MonadError
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

trait Decoder[A] {
  def decode(s: String): Either[String, A]
}

object Decoder {
  def apply[A](implicit d: Decoder[A]): Decoder[A] = d

  def instance[A](f: String => Either[String, A]): Decoder[A] =
    new Decoder[A] {
      override def decode(s: String): Either[String, A] = f(s)
    }

  implicit val decoderMonadError: MonadError[Decoder, String] =
    new MonadError[Decoder, String] {
      override def flatMap[A, B](fa: Decoder[A])(f: A => Decoder[B]): Decoder[B] =
        instance { s =>
          fa.decode(s) match {
            case Right(a)  => f(a).decode(s)
            case Left(err) => Left(err)
          }
        }

      override def tailRecM[A, B](a: A)(f: A => Decoder[Either[A, B]]): Decoder[B] = {
        @tailrec
        def step(s: String, a1: A): Either[String, B] =
          f(a1).decode(s) match {
            case Right(Right(b)) => Right(b)
            case Right(Left(a2)) => step(s, a2)
            case Left(err)       => Left(err)
          }

        instance(s => step(s, a))
      }

      override def raiseError[A](e: String): Decoder[A] =
        instance(_ => Left(e))

      override def handleErrorWith[A](fa: Decoder[A])(f: String => Decoder[A]): Decoder[A] =
        instance { s =>
          fa.decode(s) match {
            case Right(a)  => Right(a)
            case Left(err) => f(err).decode(s)
          }
        }

      override def pure[A](x: A): Decoder[A] =
        instance(_ => Right(x))
    }

  implicit val intDecoder: Decoder[Int] =
    instance(s =>
      Try(s.toInt) match {
        case Success(i) => Right(i)
        case Failure(t) => Left(t.getMessage)
      }
    )
}

class RefTypeMonadErrorSpec extends Properties("MonadError") {

  property("Decoder[Int]") = secure {
    Decoder[Int].decode("1") ?= Right(1)
  }

  property("derive Decoder[PosInt] via MonadError[Decoder, String]") = {
    // This import is needed because of https://github.com/scala/bug/issues/10753
    import Decoder.decoderMonadError
    import eu.timepit.refined.cats.derivation._

    val decoder = Decoder[PosInt]
    (decoder.decode("1") ?= Right(PosInt.unsafeFrom(1))) &&
    (decoder.decode("-1") ?= Left("Predicate failed: (-1 > 0)."))
  }
}
