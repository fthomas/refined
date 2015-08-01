package eu.timepit.refined

import eu.timepit.refined.internal.Wrapper
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties
import shapeless.tag.@@

class WrapperSpec extends Properties("Wrapper") {

  def wrapperLaw[F[_, _]](implicit w: Wrapper[F]) = forAll { (s: String) =>
    w.unwrap(w.wrap(s)) == s
  }

  property("Refined") = wrapperLaw[Refined]
  property("@@") = wrapperLaw[@@]
}
