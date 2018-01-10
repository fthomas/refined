package eu.timepit.refined.predicates

import eu.timepit.refined

object collection extends CollectionPredicates

trait CollectionPredicates {
  final type Count[PA, PC] = refined.collection.Count[PA, PC]
  final val Count = refined.collection.Count

  final type Empty = refined.collection.Empty
  final val Empty = refined.collection.Empty

  final type Forall[P] = refined.collection.Forall[P]
  final val Forall = refined.collection.Forall

  final type Head[P] = refined.collection.Head[P]
  final val Head = refined.collection.Head

  final type Index[N, P] = refined.collection.Index[N, P]
  final val Index = refined.collection.Index

  final type Init[P] = refined.collection.Init[P]
  final val Init = refined.collection.Init

  final type Last[P] = refined.collection.Last[P]
  final val Last = refined.collection.Last

  final type Size[P] = refined.collection.Size[P]
  final val Size = refined.collection.Size

  final type Tail[P] = refined.collection.Tail[P]
  final val Tail = refined.collection.Tail

  final type Contains[U] = refined.collection.Contains[U]

  final type Exists[P] = refined.collection.Exists[P]

  final type MinSize[N] = refined.collection.MinSize[N]

  final type MaxSize[N] = refined.collection.MaxSize[N]

  final type NonEmpty = refined.collection.NonEmpty
}
