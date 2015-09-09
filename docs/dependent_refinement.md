```scala
scala> import eu.timepit.refined._
import eu.timepit.refined._

scala> import eu.timepit.refined.implicits._
import eu.timepit.refined.implicits._

scala> import eu.timepit.refined.string._
import eu.timepit.refined.string._

scala> import shapeless.Witness
import shapeless.Witness

scala> import shapeless.tag.@@
import shapeless.tag.$at$at

scala> class FooAux[A](a: String, w: Witness.Aux[A]) {
     |   def apply(b: String @@ StartsWith[w.T]) = a + b
     | }
defined class FooAux

scala> def foo(a: String) = new FooAux(a, W(a))
foo: (a: String)FooAux[a.type]
```

```scala
scala> foo("ab")("abcd")
res0: String = ababcd
```

```scala
scala> foo("cd")("abcd")
<console>:25: error: Predicate failed: "abcd".startsWith("cd").
       foo("cd")("abcd")
                 ^
```
