package eu.timepit.refined.cats

import cats.data.NonEmptyList
import eu.timepit.refined.api.Validate
import eu.timepit.refined.collection.MinSize
import eu.timepit.refined.{W, refineV}
import org.scalacheck.Prop.{AnyOperators, secure}
import org.scalacheck.Properties

class UnorderedFoldableSpec extends Properties("unorderedFoldable") {

  property("Size validate for NonEmptyList") = secure {
    import eu.timepit.refined.cats.unorderedFoldable.sizeValidateInstance
    val nel = NonEmptyList.of(1, 2)
    refineV[MinSize[W.`2`.T]](nel) ?= Right(refineV[MinSize[W.`2`.T]].unsafeFrom(nel))
  }

  property("Failing Size validate for NonEmptyList") = secure {
    import eu.timepit.refined.cats.unorderedFoldable.sizeValidateInstance
    val nel = NonEmptyList.of(1, 2)
    refineV[MinSize[W.`3`.T]](nel) ?= Left(
      "Predicate taking size(NonEmptyList(1, 2)) = 2 failed: Predicate (2 < 3) did not fail."
    )
  }

  property("showExpr for NonEmptyList") = secure {
    import eu.timepit.refined.cats.unorderedFoldable.sizeValidateInstance
    val nel = NonEmptyList.of(1, 2)
    Validate[NonEmptyList[Int], MinSize[W.`3`.T]].showExpr(nel) ?= "!(2 < 3)"
  }

}
