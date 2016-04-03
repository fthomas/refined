package eu.timepit.refined

import eu.timepit.refined.api.{ Inference, Validate }
import eu.timepit.refined.api.Inference.==>
import eu.timepit.refined.generic._
import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox
import shapeless._
import shapeless.ops.coproduct.ToHList
import shapeless.ops.hlist.ToList
import shapeless.ops.nat.ToInt
import shapeless.ops.record.Keys

/** Module for generic predicates. */
object generic extends GenericValidate with GenericInference {

  /** Predicate that checks if a value is equal to `U`. */
  case class Equal[U](u: U)

  /** Predicate that checks if a value applied to the predicate `S` yields `true`. */
  case class Eval[S](s: S)

  /** Predicate that checks if the constructor names of a sum type satisfy `P`. */
  case class ConstructorNames[P](p: P)

  /** Predicate that checks if the field names of a product type satisfy `P`. */
  case class FieldNames[P](p: P)

  /** Predicate that witnesses that the type of a value is a subtype of `U`. */
  case class Subtype[U]()

  /** Predicate that witnesses that the type of a value is a supertype of `U`. */
  case class Supertype[U]()
}

private[refined] trait GenericValidate {

  implicit def equalValidateWit[T, U <: T](
    implicit
    wu: Witness.Aux[U]
  ): Validate.Plain[T, Equal[U]] =
    Validate.fromPredicate(_ == wu.value, t => s"($t == ${wu.value})", Equal(wu.value))

  implicit def equalValidateNat[N <: Nat, T](
    implicit
    tn: ToInt[N], wn: Witness.Aux[N], nt: Numeric[T]
  ): Validate.Plain[T, Equal[N]] =
    Validate.fromPredicate(t => nt.toDouble(t) == tn(), t => s"($t == ${tn()})", Equal(wn.value))

  // Cache ToolBox for Eval Validate instances
  private lazy val toolBox = currentMirror.mkToolBox()

  implicit def evalValidate[T, S <: String](implicit ws: Witness.Aux[S]): Validate.Plain[T, Eval[S]] = {
    val tree = toolBox.parse(ws.value)
    val predicate = toolBox.eval(tree).asInstanceOf[T => Boolean]
    Validate.fromPredicate(predicate, _ => ws.value, Eval(ws.value))
  }

  implicit def ctorNamesValidate[T, R0 <: Coproduct, R1 <: HList, K <: HList, NP, NR](
    implicit
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

  implicit def fieldNamesValidate[T, R <: HList, K <: HList, NP, NR](
    implicit
    lg: LabelledGeneric.Aux[T, R],
    keys: Keys.Aux[R, K],
    ktl: ToList[K, Symbol],
    v: Validate.Aux[List[String], NP, NR]
  ): Validate.Aux[T, FieldNames[NP], FieldNames[v.Res]] = {

    val fieldNames = keys().toList.map(_.name)
    val rn = v.validate(fieldNames)
    Validate.constant(rn.as(FieldNames(rn)), v.showExpr(fieldNames))
  }

  implicit def subtypeValidate[T, U >: T]: Validate.Plain[T, Subtype[U]] =
    Validate.alwaysPassed(Subtype())

  implicit def supertypeValidate[T, U <: T]: Validate.Plain[T, Supertype[U]] =
    Validate.alwaysPassed(Supertype())
}

private[refined] trait GenericInference {

  implicit def equalValidateInferenceWit[T, U <: T, P](
    implicit
    v: Validate[T, P], wu: Witness.Aux[U]
  ): Equal[U] ==> P =
    Inference(v.isValid(wu.value), s"equalValidateInferenceWit(${v.showExpr(wu.value)})")

  implicit def equalValidateInferenceNat[T, N <: Nat, P](
    implicit
    v: Validate[T, P], nt: Numeric[T], tn: ToInt[N]
  ): Equal[N] ==> P =
    Inference(v.isValid(nt.fromInt(tn())), s"equalValidateInferenceNat(${v.showExpr(nt.fromInt(tn()))})")
}
