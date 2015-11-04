```tut:silent
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.string.StartsWith
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

Unfortunately Scala does not allow singleton types of `AnyVal`s, see
[section 3.2.1][spec-3.2.1] of the Scala Language Specification:


```tut:fail
def bar[I <: Int](i: I)(j: Int @@ Greater[i.type]) = j - i
```

[spec-3.2.1]: http://www.scala-lang.org/files/archive/spec/2.11/03-types.html#singleton-types
