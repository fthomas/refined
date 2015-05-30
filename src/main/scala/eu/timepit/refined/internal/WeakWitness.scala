package eu.timepit.refined.internal

import scala.reflect.macros.whitebox

// Weaker version of shapeless.Witness, where the value field of the
// singleton type has type `Any`. This is a short-term workaround for
// https://github.com/fthomas/refined/issues/2
trait WeakWitness {
  type T
  val anyValue: Any {}
  def value: T = anyValue.asInstanceOf[T]
}

object WeakWitness {
  type Aux[T0] = WeakWitness { type T = T0 }

  implicit def apply[T]: WeakWitness.Aux[T] = macro OurSingletonTypeMacros.materializeWeakWitnessImpl[T]
}

class OurSingletonTypeMacros(override val c: whitebox.Context) extends shapeless.SingletonTypeMacros(c) {
  import c.universe._

  def materializeWeakWitnessImpl[T: WeakTypeTag]: Tree = {
    val tpe = weakTypeOf[T].dealias
    val value =
      tpe match {
        case ConstantType(c: Constant) => Literal(c)

        case SingleType(p, v) if !v.isParameter => mkAttributedRef(p, v)

        case SingletonSymbolType(c) => mkSingletonSymbol(c)

        case _ =>
          c.abort(c.enclosingPosition, s"Type argument $tpe is not a singleton type")
      }
    mkWeakWitness(tpe, value)
  }

  def mkWeakWitness(sTpe: Type, s: Tree): Tree = {
    val name = TypeName(c.freshName())

    q"""
      {
        final class $name extends _root_.eu.timepit.refined.internal.WeakWitness {
          type T = $sTpe
          val anyValue = $s
        }
        new $name
      }
    """
  }
}
