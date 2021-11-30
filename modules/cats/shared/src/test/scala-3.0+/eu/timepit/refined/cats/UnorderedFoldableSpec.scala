package eu.timepit.refined.cats

import cats.data.NonEmptyList
import eu.timepit.refined.api.Validate
import eu.timepit.refined.collection.MinSize
import eu.timepit.refined.refineV
import org.scalacheck.Prop.{AnyOperators, secure}
import org.scalacheck.Properties

class UnorderedFoldableSpec extends Properties("unorderedFoldable") {

  property("Size validate for NonEmptyList") = secure {
    import eu.timepit.refined.cats.unorderedFoldable.sizeValidateInstance
    val nel = NonEmptyList.of(1, 2)
    refineV[MinSize[2]](nel) ?= Right(refineV[MinSize[2]].unsafeFrom(nel))
  }

  property("Failing Size validate for NonEmptyList") = secure {
    import eu.timepit.refined.cats.unorderedFoldable.sizeValidateInstance
    val nel = NonEmptyList.of(1, 2)
    refineV[MinSize[3]](nel) ?= Left(
      "Predicate taking size(NonEmptyList(1, 2)) = 2 failed: Predicate (2 < 3) did not fail."
    )
  }

  property("showExpr for NonEmptyList") = secure {
    import eu.timepit.refined.cats.unorderedFoldable.sizeValidateInstance
    val nel = NonEmptyList.of(1, 2)
    Validate[NonEmptyList[Int], MinSize[3]].showExpr(nel) ?= "!(2 < 3)"
  }

}
