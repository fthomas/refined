package eu.timepit.refined

import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.MatchesRegex
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.tag.@@
import shapeless.test.illTyped

class RefinedSpec extends Properties("refined") {
  val W = shapeless.Witness

  property("refine success") = secure {
    refine[Greater[_5]](6).isRight
  }

  property("refine failure") = secure {
    refine[Forall[LowerCase]]("Hallo").isLeft
  }

  property("refine success with MatchesRegex") = secure {
    type DigitsOnly = MatchesRegex[W.`"[0-9]+"`.T]
    val res = refine[DigitsOnly]("123"): Either[String, String @@ DigitsOnly]
    res.isRight
  }

  property("refineLit success with String") = secure {
    def ignore: String @@ Forall[LowerCase] = refineLit[Forall[LowerCase]]("hello")
    true
  }

  property("refineLit failure with String") = secure {
    illTyped("""refineLit[Forall[UpperCase]]("hello")""")
    true
  }

  property("refineLit success with Int") = secure {
    def ignore: Int @@ Greater[_10] = refineLit[Greater[_10]](15)
    true
  }

  property("refineLit failure with Int") = secure {
    illTyped("""refineLit[Greater[_10]](5)""")
    true
  }

  property("refineLit success with custom Predicate") = secure {
    type ShortString = Size[LessEqual[_10]]
    def ignore: String @@ ShortString = refineLit[ShortString]("abc")
    true
  }

  property("refineLit failure with custom Predicate") = secure {
    type ShortString = Size[LessEqual[_10]]
    illTyped("""refineLit[ShortString]("abcdefghijklmnopqrstuvwxyz")""")
    true
  }

  property("refineLit success with Char") = secure {
    def ignore: Char @@ LowerCase = refineLit[LowerCase]('c')
    true
  }

  /*
  property("refineLit success with MatchesRegex") = secure {
    def ignore: String @@ MatchesRegex[W.`"[0-9]+"`.T] =
      refineLit[MatchesRegex[W.`"[0-9]+"`.T]]("123")
    true
  }

  This fails to compile with:
  [error] refined/src/test/scala/eu/timepit/refined/RefinedSpec.scala:66: exception during macro expansion:
  [error] scala.tools.reflect.ToolBoxError: reflective compilation has failed:
  [error]
  [error] overriding value value in trait Witness of type fresh$macro$5.this.T;
  [error]  value value has incompatible type
  [error]         at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$ToolBoxGlobal.throwIfErrors(ToolBoxFactory.scala:316)
  [error]         at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$ToolBoxGlobal.wrapInPackageAndCompile(ToolBoxFactory.scala:198)
  [error]         at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$ToolBoxGlobal.compile(ToolBoxFactory.scala:252)
  [error]         at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$$anonfun$compile$2.apply(ToolBoxFactory.scala:429)
  [error]         at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$$anonfun$compile$2.apply(ToolBoxFactory.scala:422)
  [error]         at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$withCompilerApi$.liftedTree2$1(ToolBoxFactory.scala:355)
  [error]         at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$withCompilerApi$.apply(ToolBoxFactory.scala:355)
  [error]         at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl.compile(ToolBoxFactory.scala:422)
  [error]         at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl.eval(ToolBoxFactory.scala:444)
  [error]         at scala.reflect.macros.contexts.Evals$class.eval(Evals.scala:20)
  [error]         at scala.reflect.macros.contexts.Context.eval(Context.scala:6)
  [error]         at eu.timepit.refined.internal.package$.refineLitImpl(package.scala:13)
  [error]       refineLit[MatchesRegex[W.`"[0-9]+"`.T], String]("123")
  */
}
