package eu.timepit.refined.scalacheck

import eu.timepit.refined.api.{Refined, RefType}
import eu.timepit.refined.collection.{NonEmpty, Size}
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.util.Buildable

/** Module that provides `Arbitrary` instances for collection predicates. */
object collection extends CollectionInstances with CollectionInstancesBinCompat1

trait CollectionInstances {

  implicit def listSizeArbitrary[F[_, _]: RefType, T: Arbitrary, P](implicit
      arbSize: Arbitrary[Int Refined P]
  ): Arbitrary[F[List[T], Size[P]]] =
    buildableSizeArbitrary[F, List[T], T, P]

  implicit def vectorSizeArbitrary[F[_, _]: RefType, T: Arbitrary, P](implicit
      arbSize: Arbitrary[Int Refined P]
  ): Arbitrary[F[Vector[T], Size[P]]] =
    buildableSizeArbitrary[F, Vector[T], T, P]

  // This is private and not implicit because it could produce invalid
  // values for some collections:
  //
  // scala> buildableSizeArbitrary[Refined, Set[Boolean], Boolean, Equal[3]].arbitrary.sample
  // res0: Option[Refined[Set[Boolean], Size[Equal[3]]]] = Some(Set(false, true))
  private[scalacheck] def buildableSizeArbitrary[F[_, _]: RefType, C, T, P](implicit
      arbT: Arbitrary[T],
      arbSize: Arbitrary[Int Refined P],
      ev1: Buildable[T, C],
      ev2: C => Iterable[T]
  ): Arbitrary[F[C, Size[P]]] =
    arbitraryRefType(arbSize.arbitrary.flatMap { n =>
      Gen.buildableOfN[C, T](n.value, arbT.arbitrary)
    })
}

trait CollectionInstancesBinCompat1 {

  implicit def listNonEmptyArbitrary[F[_, _]: RefType, T: Arbitrary]
      : Arbitrary[F[List[T], NonEmpty]] =
    buildableNonEmptyArbitrary[F, List[T], T]

  implicit def vectorNonEmptyArbitrary[F[_, _]: RefType, T: Arbitrary]
      : Arbitrary[F[Vector[T], NonEmpty]] =
    buildableNonEmptyArbitrary[F, Vector[T], T]

  private[scalacheck] def buildableNonEmptyArbitrary[F[_, _]: RefType, C, T](implicit
      arbT: Arbitrary[T],
      ev1: Buildable[T, C],
      ev2: C => Iterable[T]
  ): Arbitrary[F[C, NonEmpty]] =
    arbitraryRefType(Gen.nonEmptyBuildableOf(arbT.arbitrary))
}
