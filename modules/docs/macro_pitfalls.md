# Macro pitfalls

```scala
import eu.timepit.refined._
import eu.timepit.refined.api.Validate
```

When using the `refineMT`, `refineMV`, implicit conversion with `eu.timepit.refined.auto._` or `RefType.applyRef` macros with custom predicates
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
     |   val x = refineMV[Foo](0)
     | }
<console>:22: error: exception during macro expansion:
java.lang.ClassNotFoundException: Test1$
	at scala.reflect.internal.util.AbstractFileClassLoader.findClass(AbstractFileClassLoader.scala:64)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
	at __wrapper$3$750921a965fa40ed8c2fb1b84a33a405.__wrapper$3$750921a965fa40ed8c2fb1b84a33a405$.wrapper(<no source file>:22)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl$ToolBoxGlobal.$anonfun$compile$11(ToolBoxFactory.scala:279)
	at scala.tools.reflect.ToolBoxFactory$ToolBoxImpl.eval(ToolBoxFactory.scala:448)
	at scala.reflect.macros.contexts.Evals.eval(Evals.scala:20)
	at scala.reflect.macros.contexts.Evals.eval$(Evals.scala:14)
	at scala.reflect.macros.contexts.Context.eval(Context.scala:6)
	at eu.timepit.refined.macros.MacroUtils.$anonfun$eval$1(MacroUtils.scala:25)
	at scala.Option.getOrElse(Option.scala:121)
	at eu.timepit.refined.macros.MacroUtils.tryN(MacroUtils.scala:29)
	at eu.timepit.refined.macros.MacroUtils.tryN$(MacroUtils.scala:28)
	at eu.timepit.refined.macros.RefineMacro.tryN(RefineMacro.scala:12)
	at eu.timepit.refined.macros.MacroUtils.eval(MacroUtils.scala:25)
	at eu.timepit.refined.macros.MacroUtils.eval$(MacroUtils.scala:18)
	at eu.timepit.refined.macros.RefineMacro.eval(RefineMacro.scala:12)
	at eu.timepit.refined.macros.RefineMacro.$anonfun$validateInstance$1(RefineMacro.scala:58)
	at scala.Option.getOrElse(Option.scala:121)
	at eu.timepit.refined.macros.RefineMacro.validateInstance(RefineMacro.scala:58)
	at eu.timepit.refined.macros.RefineMacro.impl(RefineMacro.scala:28)

         val x = refineMV[Foo](0)
                              ^
```
## Solution 1
Using the macro in a different compilation unit is fine (through REPL):

```scala
scala> object Test1 {
     |   case class Foo()
     | 
     |   implicit def fooValidate[T]: Validate[T, Foo] =
     |     Validate.fromPredicate(_ => true, x => s"$x = Foo", Foo())
     | }
defined object Test1

scala> object Test2 {
     |   val x = refineMV[Test1.Foo](0)
     | }
defined object Test2
```
## Solution 2

When used inside a regular project, this approach might not be enough.

The other solution is to create a [multi-project](https://www.scala-sbt.org/1.x/docs/Multi-Project.html). The subproject `a` contains the previously defined object `Test1` while the subproject `b` contains the actual call to `refinedMV`, `refinedMT`, implicit conversion with `eu.timepit.refined.auto._` or the call to `RefType.applyRef`. 

Project `b` will have to depend on project `a` (on it's build.sbt file) using the method call [dependsOn](https://www.scala-sbt.org/1.x/docs/Multi-Project.html#Classpath+dependencies).
