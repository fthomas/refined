```tut:silent
import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.string._
import shapeless.tag.@@
```

```tut
def foo[S <: String](a: S)(b: String @@ StartsWith[a.type]) = a + b
```

```tut
foo("ab")("abcd")
```

```tut:fail
foo("cd")("abcd")
```
