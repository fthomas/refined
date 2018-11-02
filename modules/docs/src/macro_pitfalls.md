# Macro pitfalls

```tut:silent
import eu.timepit.refined._
import eu.timepit.refined.api.Validate
```

When using the `refineMT`, `refineMV`, implicit conversion with `eu.timepit.refined.auto._` or `RefType.applyRef` macros with custom predicates
make sure to define the predicate and its `Validate` instance in a
different compilation unit. Otherwise macro expansion will fail because
it tries to evaluate an uninitialized object as you can see below:

```tut:fail
object Test1 {
  case class Foo()

  implicit def fooValidate[T]: Validate[T, Foo] =
    Validate.fromPredicate(_ => true, x => s"$x = Foo", Foo())

  val x = refineMV[Foo](0)
}
```
## Solution 1
Using the macro in a different compilation unit is fine (through REPL):

```tut
object Test1 {
  case class Foo()

  implicit def fooValidate[T]: Validate[T, Foo] =
    Validate.fromPredicate(_ => true, x => s"$x = Foo", Foo())
}

object Test2 {
  val x = refineMV[Test1.Foo](0)
}
```
## Solution 2

When used inside a regular project, this approach might not be enough.

The other solution is to create a [multi-project](https://www.scala-sbt.org/1.x/docs/Multi-Project.html). The subproject `a` contains the previously defined object `Test1` while the subproject `b` contains the actual call to `refinedMV`, `refinedMT`, implicit conversion with `eu.timepit.refined.auto._` or the call to `RefType.applyRef`.

Project `b` will have to depend on project `a` (on it's build.sbt file) using the method call [dependsOn](https://www.scala-sbt.org/1.x/docs/Multi-Project.html#Classpath+dependencies).
