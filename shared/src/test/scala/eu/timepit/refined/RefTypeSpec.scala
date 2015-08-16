package eu.timepit.refined

import eu.timepit.refined.RefType.ops._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.tag.@@

abstract class RefTypeProperties[F[_, _]](name: String)(implicit rt: RefType[F]) extends Properties(s"RefType[$name]") {

  property("unsafeWrap.unwrap ~= id") = forAll { (s: String) =>
    rt.unsafeWrap(s).unwrap == s
  }

  property("mapRefine.success") = secure {
    rt.refine[Positive](5).right.flatMap(_.mapRefine(_.toDouble)).isRight
  }

  property("mapRefine.failure") = secure {
    rt.refine[Positive](5).right.flatMap(_.mapRefine(_ - 10)).isLeft
  }
}

class RefTypeSpecRefined extends RefTypeProperties[Refined]("Refined")

class RefTypeSpecTag extends RefTypeProperties[@@]("@@")
