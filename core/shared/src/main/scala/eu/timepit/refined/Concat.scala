package eu.timepit.refined

import scala.reflect.macros.whitebox

trait Concat[A, B] {
  type Out <: String
}

object Concat {

  type Aux[A, B, Out0] = Concat[A, B] { type Out = Out0 }

  def apply[A, B](implicit c: Concat[A, B]): Aux[A, B, c.Out] = c

  implicit def stringConcat[A <: String, B <: String]: Concat[A, B] = macro impl[A, B]

  def impl[A: c.WeakTypeTag, B: c.WeakTypeTag](c: whitebox.Context): c.Expr[Concat[A, B]] = {
    import c.universe._

    def stringOf(tpe: Type): String =
      tpe match {
        case ConstantType(Constant(a)) => a.asInstanceOf[String]
        case _ => c.abort(c.enclosingPosition, s"Cannot extract String from $tpe")
      }

    val aTpe = weakTypeOf[A]
    val bTpe = weakTypeOf[B]

    println(showRaw(aTpe))
    println(showRaw(bTpe))

    val ab = stringOf(aTpe) + stringOf(bTpe)
    val abTpe = c.internal.constantType(Constant(ab))

    c.Expr(q"new Concat[$aTpe, $bTpe] { type Out = $abTpe }")
  }
}
