package eu.timepit.refined.predicates

import eu.timepit.refined

object boolean extends BooleanPredicates

trait BooleanPredicates {
  final type True = refined.boolean.True
  final val True = refined.boolean.True

  final type False = refined.boolean.False
  final val False = refined.boolean.False

  final type Not[P] = refined.boolean.Not[P]
  final val Not = refined.boolean.Not

  final type And[A, B] = refined.boolean.And[A, B]
  final val And = refined.boolean.And

  final type Or[A, B] = refined.boolean.Or[A, B]
  final val Or = refined.boolean.Or

  final type Xor[A, B] = refined.boolean.Xor[A, B]
  final val Xor = refined.boolean.Xor

  final type AllOf[PS] = refined.boolean.AllOf[PS]
  final val AllOf = refined.boolean.AllOf

  final type AnyOf[PS] = refined.boolean.AnyOf[PS]
  final val AnyOf = refined.boolean.AnyOf

  final type OneOf[PS] = refined.boolean.OneOf[PS]
  final val OneOf = refined.boolean.OneOf

  final type Nand[A, B] = refined.boolean.Nand[A, B]

  final type Nor[A, B] = refined.boolean.Nor[A, B]
}
