package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.{Refined, RefType}
import eu.timepit.refined.collection.Size
import org.scalacheck.{Arbitrary, Gen}
import shapeless.Nat
import shapeless.ops.nat.ToInt

/** Module that provides `Arbitrary` instances for collection predicates. */
object collection extends CollectionInstances

trait CollectionInstances {

  implicit def listSizeArbitrary[T: Arbitrary, F[_, _]: RefType, P[_], N <: Nat](
      implicit arb: Arbitrary[Int Refined P[N]],
      tn: ToInt[N]): Arbitrary[F[List[T], Size[P[N]]]] =
    arbitraryRefType[F, List[T], Size[P[N]]](
      for {
        numElems <- arb.arbitrary
        elems <- Gen.listOfN(Math.min(numElems.value, tn() + 999), Arbitrary.arbitrary[T])
      } yield elems
    )
}
