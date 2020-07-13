package eu.timepit.refined

import eu.timepit.refined.api.{Inference, Validate}
import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.generic._
import eu.timepit.refined.internal.WitnessAs
import shapeless._
import shapeless.ops.coproduct.ToHList
import shapeless.ops.hlist.ToList
import shapeless.ops.record.Keys

/** Module for generic predicates. */
object generic extends GenericInference {

  /** Predicate that checks if a value is equal to `U`. */
  final case class Equal[U](u: U)

  @deprecated(
    "Deprecated because ConstructorNames operates on types and not values and refined focuses on refining values.",
    "0.9.0"
  )
  final case class ConstructorNames[P](p: P)

  @deprecated(
    "Deprecated because FieldNames operates on types and not values and refined focuses on refining values.",
    "0.9.0"
  )
  final case class FieldNames[P](p: P)

  @deprecated(
    "The Subtype predicate is deprecated without replacement because it is lacking practical relevance.",
    "0.9.0"
  )
  final case class Subtype[U]()

  @deprecated(
    "The Supertype predicate is deprecated without replacement because it is lacking practical relevance.",
    "0.9.0"
  )
  final case class Supertype[U]()

  object Equal {
    implicit def equalValidate[T, U](implicit
        wu: WitnessAs[U, T]
    ): Validate.Plain[T, Equal[U]] =
      Validate.fromPredicate(_ == wu.snd, t => s"($t == ${wu.snd})", Equal(wu.fst))
  }

  object ConstructorNames {
    @deprecated(
      "Deprecated because ConstructorNames operates on types and not values and refined focuses on refining values.",
      "0.9.0"
    )
    implicit def ctorNamesValidate[T, R0 <: Coproduct, R1 <: HList, K <: HList, NP, NR](implicit
        lg: LabelledGeneric.Aux[T, R0],
        cthl: ToHList.Aux[R0, R1],
        keys: Keys.Aux[R1, K],
        ktl: ToList[K, Symbol],
        v: Validate.Aux[List[String], NP, NR]
    ): Validate.Aux[T, ConstructorNames[NP], ConstructorNames[v.Res]] = {

      val ctorNames = keys().toList.map(_.name)
      val rn = v.validate(ctorNames)
      Validate.constant(rn.as(ConstructorNames(rn)), v.showExpr(ctorNames))
    }
  }

  object FieldNames {
    @deprecated(
      "Deprecated because FieldNames operates on types and not values and refined focuses on refining values.",
      "0.9.0"
    )
    implicit def fieldNamesValidate[T, R <: HList, K <: HList, NP, NR](implicit
        lg: LabelledGeneric.Aux[T, R],
        keys: Keys.Aux[R, K],
        ktl: ToList[K, Symbol],
        v: Validate.Aux[List[String], NP, NR]
    ): Validate.Aux[T, FieldNames[NP], FieldNames[v.Res]] = {

      val fieldNames = keys().toList.map(_.name)
      val rn = v.validate(fieldNames)
      Validate.constant(rn.as(FieldNames(rn)), v.showExpr(fieldNames))
    }
  }

  object Subtype {
    @deprecated(
      "The Subtype predicate is deprecated without replacement because it is lacking practical relevance.",
      "0.9.0"
    )
    implicit def subtypeValidate[T, U >: T]: Validate.Plain[T, Subtype[U]] =
      Validate.alwaysPassed(Subtype())
  }

  object Supertype {
    @deprecated(
      "The Supertype predicate is deprecated without replacement because it is lacking practical relevance.",
      "0.9.0"
    )
    implicit def supertypeValidate[T, U <: T]: Validate.Plain[T, Supertype[U]] =
      Validate.alwaysPassed(Supertype())
  }
}

private[refined] trait GenericInference {

  implicit def equalValidateInference[T, U, P](implicit
      v: Validate[T, P],
      wu: WitnessAs[U, T]
  ): Equal[U] ==> P =
    Inference(v.isValid(wu.snd), s"equalValidateInference(${v.showExpr(wu.snd)})")
}
