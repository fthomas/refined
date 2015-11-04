```scala
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Greater
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
<console>:19: error: Predicate failed: "abcd".startsWith("cd").
       foo("cd")("abcd")
                 ^
```

Unfortunately Scala does not allow singleton types of `AnyVal`s, see
[section 3.2.1][spec-3.2.1] of the Scala Language Specification:


```scala
scala> def bar[I <: Int](i: I)(j: Int @@ Greater[i.type]) = j - i
<console>:17: error: type mismatch;
 found   : i.type (with underlying type I)
 required: AnyRef
Note that I is bounded only by AnyVal, which means AnyRef is not a known parent.
Such types can participate in value classes, but instances
cannot appear in singleton types or in reference comparisons.
       def bar[I <: Int](i: I)(j: Int @@ Greater[i.type]) = j - i
                                                 ^
```

[spec-3.2.1]: http://www.scala-lang.org/files/archive/spec/2.11/03-types.html#singleton-types
