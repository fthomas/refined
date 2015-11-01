# Macro pitfalls

```tut:silent
import eu.timepit.refined._
import eu.timepit.refined.api.Validate
```

When using the `refineMT` or `refineMV` macros with custom predicates
make sure to define the predicate and its `Validate` instance in a
different compilation unit. Otherwise macro expansion will fail because
it tries to evaluate an uninitialized object as you can see below:

```tut:fail
object Test1 {
  case class Foo()

  implicit def fooValidate[T]: Validate[T, Foo] =
    Validate.fromPredicate(_ => true, x => s"$x = Foo", Foo())

  val x = refineMT[Foo](0)
}
```

Using the macro in a different compilation unit is fine:

```tut
object Test1 {
  case class Foo()

  implicit def fooValidate[T]: Validate[T, Foo] =
    Validate.fromPredicate(_ => true, x => s"$x = Foo", Foo())
}

object Test2 {
  val x = refineMT[Test1.Foo](0)
}
```
