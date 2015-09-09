```tut:silent
import eu.timepit.refined._
import eu.timepit.refined.implicits._
import eu.timepit.refined.string._
import shapeless.Witness
import shapeless.tag.@@
```

```tut
class FooAux[A](a: String, w: Witness.Aux[A]) {
  def apply(b: String @@ StartsWith[w.T]) = a + b
}

def foo(a: String) = new FooAux(a, W(a))
```

```tut
foo("ab")("abcd")
```

```tut:fail
foo("cd")("abcd")
```
