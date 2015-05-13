package eu.timepit

import shapeless.tag
import shapeless.tag.@@

import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

package object refined {
  def refine[P, T](t: T)(implicit p: Predicate[P, T]): Either[String, T @@ P] =
    p.validated(t) match {
      case Some(s) => Left(s)
      case None => Right(tag[P](t))
    }

  def refineUnsafe[P, T](t: T)(implicit p: Predicate[P, T]): T @@ P =
    p.validated(t).fold(tag[P](t))(s => throw new IllegalArgumentException(s))

  def refineLit[P, T](t: T)(implicit p: Predicate[P, T]): T @@ P = macro refineLitImpl[P, T]

  def refineLitImpl[P: c.WeakTypeTag, T: c.WeakTypeTag](c: Context)(t: c.Expr[T])(p: c.Expr[Predicate[P, T]]): c.Expr[T @@ P] = {
    import c.universe._

    val resetTree = c.untypecheck(p.tree)
    val p3 = c.eval(c.Expr[Predicate[P, T]](resetTree))

    //val pp = c.eval(c.Expr(p.tree))
    //val p2 = c.eval(p)
    //pp
    //p2

    val pType = weakTypeOf[P]
    val tType = weakTypeOf[T]

    println(showRaw(p))
    println(showRaw(t))

    val q"$p1" = p.tree
    //println(showRaw(p1))

    //val xxx = Predicate[UpperCase, T]

    println("in macro")
    t.tree match {
      case Literal(Constant(l)) => {
        val ll = l.asInstanceOf[T]
        println(l.asInstanceOf[T])
        println(p3.validated(ll))
        reify { println(refine(l.asInstanceOf[T])(p.splice)) }

        c.Expr(q"refine[$pType, $tType]($t).fold(???, identity)")
      }
      case _ => c.abort(c.enclosingPosition, "only supports literals")
    }
  }

}
