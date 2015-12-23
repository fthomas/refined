# Macro pitfalls

```scala
import eu.timepit.refined._
import eu.timepit.refined.api.Validate
```

When using the `refineMT` or `refineMV` macros with custom predicates
make sure to define the predicate and its `Validate` instance in a
different compilation unit. Otherwise macro expansion will fail because
it tries to evaluate an uninitialized object as you can see below:

```scala
scala> object Test1 {
     |   case class Foo()
     | 
     |   implicit def fooValidate[T]: Validate[T, Foo] =
     |     Validate.fromPredicate(_ => true, x => s"$x = Foo", Foo())
     | 
     |   val x = refineMT[Foo](0)
     | }
<console>:21: error: exception during macro expansion:
java.lang.AssertionError: assertion failed: fooValidate
	at scala.reflect.internal.Symbols$Symbol.info(Symbols.scala:1470)
	at scala.reflect.runtime.SynchronizedSymbols$SynchronizedSymbol$$anon$9.scala$reflect$runtime$SynchronizedSymbols$SynchronizedSymbol$$super$info(SynchronizedSymbols.scala:186)
	at scala.reflect.runtime.SynchronizedSymbols$SynchronizedSymbol$$anonfun$info$1.apply(SynchronizedSymbols.scala:127)
	at scala.reflect.runtime.SynchronizedSymbols$SynchronizedSymbol$$anonfun$info$1.apply(SynchronizedSymbols.scala:127)
	at scala.reflect.runtime.Gil$class.gilSynchronized(Gil.scala:15)
	at scala.tools.reflect.ReflectGlobal.gilSynchronized(ReflectGlobal.scala:11)
	at scala.reflect.runtime.SynchronizedSymbols$SynchronizedSymbol$class.gilSynchronizedIfNotThreadsafe(SynchronizedSymbols.scala:123)
	at scala.reflect.runtime.SynchronizedSymbols$SynchronizedSymbol$$anon$9.gilSynchronizedIfNotThreadsafe(SynchronizedSymbols.scala:186)
	at scala.reflect.runtime.SynchronizedSymbols$SynchronizedSymbol$class.info(SynchronizedSymbols.scala:127)
	at scala.reflect.runtime.SynchronizedSymbols$SynchronizedSymbol$$anon$9.info(SynchronizedSymbols.scala:186)
	at scala.reflect.internal.Symbols$Symbol.initialize(Symbols.scala:1634)
	at scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:5007)
	at scala.tools.nsc.typechecker.Typers$Typer.runTyper$1(Typers.scala:5395)
	at scala.tools.nsc.typechecker.Typers$Typer.scala$tools$nsc$typechecker$Typers$Typer$$typedInternal(Typers.scala:5422)
	at scala.tools.nsc.typechecker.Typers$Typer.body$2(Typers.scala:5369)
	at scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:5373)
	at scala.tools.nsc.typechecker.Typers$Typer.transformedOrTyped(Typers.scala:5604)
	at scala.tools.nsc.typechecker.Typers$Typer.typedDefDef(Typers.scala:2208)
	at scala.tools.nsc.typechecker.Typers$Typer.typedMemberDef$1(Typers.scala:5307)
	at scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:5358)
	at scala.tools.nsc.typechecker.Typers$Typer.runTyper$1(Typers.scala:5395)
	at scala.tools.nsc.typechecker.Typers$Typer.scala$tools$nsc$typechecker$Typers$Typer$$typedInternal(Typers.scala:5422)
	at scala.tools.nsc.typechecker.Typers$Typer.body$2(Typers.scala:5369)
	at scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:5373)
	at scala.tools.nsc.typechecker.Typers$Typer.typedByValueExpr(Typers.scala:5451)
	at scala.tools.nsc.typechecker.Typers$Typer.scala$tools$nsc$typechecker$Typers$Typer$$typedStat$1(Typers.scala:3047)
	at scala.tools.nsc.typechecker.Typers$Typer$$anonfun$65.apply(Typers.scala:3151)
	at scala.tools.nsc.typechecker.Typers$Typer$$anonfun$65.apply(Typers.scala:3151)
	at scala.collection.immutable.List.loop$1(List.scala:173)
	at scala.collection.immutable.List.mapConserve(List.scala:189)
	at scala.tools.nsc.typechecker.Typers$Typer.typedStats(Typers.scala:3151)
	at scala.tools.nsc.typechecker.Typers$Typer.typedTemplate(Typers.scala:1921)
	at scala.tools.nsc.typechecker.Typers$Typer.typedModuleDef(Typers.scala:1808)
	at scala.tools.nsc.typechecker.Typers$Typer.typedMemberDef$1(Typers.scala:5309)
	at scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:5358)
	at scala.tools.nsc.typechecker.Typers$Typer.runTyper$1(Typers.scala:5395)
	at scala.tools.nsc.typechecker.Typers$Typer.scala$tools$nsc$typechecker$Typers$Typer$$typedInternal(Typers.scala:5422)
	at scala.tools.nsc.typechecker.Typers$Typer.body$2(Typers.scala:5369)
	at scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:5373)
	at scala.tools.nsc.typechecker.Typers$Typer.typedByValueExpr(Typers.scala:5451)
	at scala.tools.nsc.typechecker.Typers$Typer.scala$tools$nsc$typechecker$Typers$Typer$$typedStat$1(Typers.scala:3047)
	at scala.tools.nsc.typechecker.Typers$Typer$$anonfun$65.apply(Typers.scala:3151)
	at scala.tools.nsc.typechecker.Typers$Typer$$anonfun$65.apply(Typers.scala:3151)
	at scala.collection.immutable.List.loop$1(List.scala:173)
	at scala.collection.immutable.List.mapConserve(List.scala:189)
	at scala.tools.nsc.typechecker.Typers$Typer.typedStats(Typers.scala:3151)
	at scala.tools.nsc.typechecker.Typers$Typer.typedPackageDef$1(Typers.scala:5014)
	at scala.tools.nsc.typechecker.Typers$Typer.typedMemberDef$1(Typers.scala:5311)
	at scala.tools.nsc.typechecker.Typers$Typer.typed1(Typers.scala:5358)
	at scala.tools.nsc.typechecker.Typers$Typer.runTyper$1(Typers.scala:5395)
	at scala.tools.nsc.typechecker.Typers$Typer.scala$tools$nsc$typechecker$Typers$Typer$$typedInternal(Typers.scala:5422)
	at scala.tools.nsc.typechecker.Typers$Typer.body$2(Typers.scala:5369)
	at scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:5373)
	at scala.tools.nsc.typechecker.Typers$Typer.typed(Typers.scala:5447)
	at scala.tools.nsc.typechecker.Analyzer$typerFactory$$anon$3.apply(Analyzer.scala:102)
	at scala.tools.nsc.Global$GlobalPhase$$anonfun$applyPhase$1.apply$mcV$sp(Global.scala:440)
	at scala.tools.nsc.Global$GlobalPhase.withCurrentUnit(Global.scala:431)
	at scala.tools.nsc.Global$GlobalPhase.applyPhase(Global.scala:440)
	at scala.tools.nsc.typechecker.Analyzer$typerFactory$$anon$3$$anonfun$run$1.apply(Analyzer.scala:94)
	at scala.tools.nsc.typechecker.Analyzer$typerFactory$$anon$3$$anonfun$run$1.apply(Analyzer.scala:93)
	at scala.collection.Iterator$class.foreach(Iterator.scala:742)
	at scala.collection.AbstractIterator.foreach(Iterator.scala:1194)
	at scala.tools.nsc.typechecker.Analyzer$typerFactory$$anon$3.run(Analyzer.scala:93)
	at scala.tools.nsc.Global$Run.compileUnitsInternal(Global.scala:1501)
	at scala.tools.nsc.Global$Run.compileUnits(Global.scala:1486)
	at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$ToolBoxGlobal.wrapInPackageAndCompile(ToolBoxFactory.scala:197)
	at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$ToolBoxGlobal.compile(ToolBoxFactory.scala:252)
	at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$$anonfun$compile$2.apply(ToolBoxFactory.scala:429)
	at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$$anonfun$compile$2.apply(ToolBoxFactory.scala:422)
	at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$withCompilerApi$.liftedTree2$1(ToolBoxFactory.scala:355)
	at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$withCompilerApi$.apply(ToolBoxFactory.scala:355)
	at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl.compile(ToolBoxFactory.scala:422)
	at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl.eval(ToolBoxFactory.scala:444)
	at scala.reflect.macros.contexts.Evals$class.eval(Evals.scala:20)
	at scala.reflect.macros.contexts.Context.eval(Context.scala:6)
	at eu.timepit.refined.macros.MacroUtils$$anonfun$eval$1.apply(MacroUtils.scala:22)
	at scala.Option.getOrElse(Option.scala:121)
	at eu.timepit.refined.macros.MacroUtils$class.tryN(MacroUtils.scala:26)
	at eu.timepit.refined.macros.RefineMacro.tryN(RefineMacro.scala:9)
	at eu.timepit.refined.macros.MacroUtils$class.eval(MacroUtils.scala:22)
	at eu.timepit.refined.macros.RefineMacro.eval(RefineMacro.scala:9)
	at eu.timepit.refined.macros.RefineMacro.impl(RefineMacro.scala:17)

         val x = refineMT[Foo](0)
                              ^
```

Using the macro in a different compilation unit is fine:

```scala
scala> object Test1 {
     |   case class Foo()
     | 
     |   implicit def fooValidate[T]: Validate[T, Foo] =
     |     Validate.fromPredicate(_ => true, x => s"$x = Foo", Foo())
     | }
defined object Test1

scala> object Test2 {
     |   val x = refineMT[Test1.Foo](0)
     | }
defined object Test2
```
