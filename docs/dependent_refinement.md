# Dependent refinement

```scala
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.string.StartsWith
import shapeless.tag.@@
import shapeless.Witness
```

Scala's path dependent types makes it possible to express refinements
that depend other statically known values:

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
<console>:20: error: Predicate failed: "abcd".startsWith("cd").
       foo("cd")("abcd")
                 ^
```

Unfortunately Scala does not allow to use singleton types of `AnyVal`s,
see [section 3.2.1][spec-3.2.1] of the Scala Language Specification:

```scala
scala> def bar[I <: Int](i: I)(j: Int @@ Greater[i.type]) = j - i
<console>:18: error: type mismatch;
 found   : i.type (with underlying type I)
 required: AnyRef
Note that I is bounded only by AnyVal, which means AnyRef is not a known parent.
Such types can participate in value classes, but instances
cannot appear in singleton types or in reference comparisons.
       def bar[I <: Int](i: I)(j: Int @@ Greater[i.type]) = j - i
                                                 ^
```

### shapeless to the rescue

We can however use `shapeless.Witness` to workaround this limitation in
the specification. `Witness` captures the singleton type of an `AnyVal`
and makes it available via the type member `T`:

```scala
scala> def baz[I <: Int](i: Witness.Aux[I])(j: Int @@ Greater[i.T]) = j - i.value
baz: [I <: Int](i: shapeless.Witness.Aux[I])(j: shapeless.tag.@@[Int,eu.timepit.refined.numeric.Greater[i.T]])Int
```

```scala
scala> baz(Witness(2))(4)
res2: Int = 2
```

```scala
scala> baz(Witness(6))(4)
<console>:20: error: Predicate failed: (4 > 6).
       baz(Witness(6))(4)
                       ^
```

[spec-3.2.1]: http://www.scala-lang.org/files/archive/spec/2.11/03-types.html#singleton-types
