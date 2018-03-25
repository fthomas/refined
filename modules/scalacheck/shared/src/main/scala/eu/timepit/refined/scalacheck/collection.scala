package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.{Refined, RefType}
import eu.timepit.refined.collection.Size
import org.scalacheck.{Arbitrary, Gen}

/** Module that provides `Arbitrary` instances for collection predicates. */
object collection extends CollectionInstances

trait CollectionInstances {

  implicit def listSizeArbitrary[T: Arbitrary, F[_, _]: RefType, P](
      implicit arb: Arbitrary[Int Refined P]
  ): Arbitrary[F[List[T], Size[P]]] =
    arbitraryRefType[F, List[T], Size[P]](
      for {
        numElems <- arb.arbitrary
        elems <- Gen.listOfN(numElems.value, Arbitrary.arbitrary[T])
      } yield elems
    )
}
