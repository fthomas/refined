package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.RefType
import eu.timepit.refined.collection.MaxSize
import org.scalacheck.{Arbitrary, Gen}
import shapeless.Nat
import shapeless.ops.nat.ToInt

/** Module that provides `Arbitrary` instances for collection predicates. */
object collection extends CollectionInstances

trait CollectionInstances {

  implicit def listMaxSizeArbitraryNat[T: Arbitrary, F[_, _]: RefType, N <: Nat](
      implicit tn: ToInt[N]
  ): Arbitrary[F[List[T], MaxSize[N]]] =
    arbitraryRefType[F, List[T], MaxSize[N]](
      for {
        numElems <- Gen.chooseNum(0, tn())
        elems <- Gen.listOfN(numElems, Arbitrary.arbitrary[T])
      } yield elems
    )
}
