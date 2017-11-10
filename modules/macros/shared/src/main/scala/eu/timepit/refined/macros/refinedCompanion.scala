package eu.timepit.refined.macros

import macrocompat.bundle
import scala.annotation.{compileTimeOnly, StaticAnnotation}
import scala.reflect.macros.blackbox

@compileTimeOnly("enable macro paradise to expand macro annotations")
class refinedCompanion extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro RefinedCompanionMacro.impl
}

@bundle
class RefinedCompanionMacro(val c: blackbox.Context) {
  def impl(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    val typeDef = annottees.head
    val typeDefAndCompanion = typeDef.tree match {
      case q"type $alias = $refType[$baseType, $predicate]" =>
        val companionName = alias.asInstanceOf[TypeName].toTermName
        val fromBaseType = TermName(s"from$baseType")

        // Using $predicate instead of Any in RefineMacro.impl raises the error
        // "erroneous or inaccessible type" when the predicate includes a literal
        // type written with shapeless.Witness. Fortunately using Any works just fine.

        q"""
          $typeDef

          object $companionName {
            def apply(t: $baseType)(
              implicit
              rt: _root_.eu.timepit.refined.api.RefType[$refType],
              v:  _root_.eu.timepit.refined.api.Validate[$baseType, $predicate]
            ): $alias = macro _root_.eu.timepit.refined.macros.RefineMacro.impl[$refType, $baseType, _root_.scala.Any]

            def $fromBaseType(t: $baseType): _root_.scala.Either[_root_.scala.Predef.String, $alias] =
              _root_.eu.timepit.refined.api.RefType[$refType].refine[$predicate](t)
          }
        """
    }

    c.Expr(typeDefAndCompanion)
  }
}
