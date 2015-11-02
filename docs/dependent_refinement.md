```scala
import eu.timepit.refined.auto._
import eu.timepit.refined.string.StartsWith
import shapeless.tag.@@
```

```scala
scala> def foo[S <: String](a: S)(b: String @@ StartsWith[a.type]) = a + b
foo: [S <: String](a: S)(b: shapeless.tag.@@[String,eu.timepit.refined.string.StartsWith[a.type]])String
```

```scala
scala> foo("ab")("abcd")
res0: String = ababcd
```

```scala
scala> foo("cd")("abcd")
<console>:18: error: Predicate failed: "abcd".startsWith("cd").
       foo("cd")("abcd")
                 ^
```
