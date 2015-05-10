import shapeless.tag
import shapeless.tag.@@

package object refined {
  def refine[P, X](x: X)(implicit ev: Predicate[P, X]): Either[String, X @@ P] =
    ev.validate(null.asInstanceOf[P], x) match {
      case Some(err) => Left(err)
      case None => Right(tag[P](x))
    }

  def refineL[P, X](x: X)(implicit ev: Predicate[P, X]): X @@ P =
    ev.validate(null.asInstanceOf[P], x) match {
      case Some(err) => ???
      case None => tag[P](x)
    }
}
