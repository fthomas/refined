```tut:silent
import eu.timepit.refined._
import eu.timepit.refined.api.Validate

object Test1 {
  case class Foo()

  implicit def fooValidate[T]: Validate[T, Foo] =
    Validate.fromPredicate(_ => true, x => s"$x = Foo", Foo())

  //val x = refineMT[Foo](0)
  //
  // The above will raise an exception:
  // <console>:21: error: exception during macro expansion:
  // java.lang.AssertionError: assertion failed: fooValidate
  // 
  // The problem here is that refineMT is used in the same compilation
  // unit where the Validate instance for Foo is defined.
}

object Test2 {
  import Test1.Foo

  // Calling refineMT in another compilation unit is fine:
  val x = refineMT[Foo](0)
}
```
