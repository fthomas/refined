# Dependent refinement

```scala
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.string.StartsWith
import shapeless.Witness
```

Scala's path dependent types makes it possible to express refinements
that depend other statically known values:

```scala
scala> def foo(a: String)(b: String Refined StartsWith[a.type]) = a + b
foo: (a: String)(b: eu.timepit.refined.api.Refined[String,eu.timepit.refined.string.StartsWith[a.type]])String
```

```scala
scala> foo("ab")("abcd")
res0: String = ababcd
```

```scala
scala> foo("cd")("abcd")
<console>:21: error: Predicate failed: "abcd".startsWith("cd").
       foo("cd")("abcd")
                 ^
```

Unfortunately Scala does not allow to use singleton types of `AnyVal`s,
see [section 3.2.1][spec-3.2.1] of the Scala Language Specification:

```scala
scala> def bar(i: Int)(j: Int Refined Greater[i.type]) = j - i
<console>:19: error: type mismatch;
 found   : i.type (with underlying type Int)
 required: AnyRef
       def bar(i: Int)(j: Int Refined Greater[i.type]) = j - i
                                              ^
```

### shapeless to the rescue

We can however use `shapeless.Witness` to workaround this limitation in
the specification. `Witness` captures the singleton type of an `AnyVal`
and makes it available via the type member `T`:

```scala
scala> def baz[I <: Int](i: Witness.Aux[I])(j: Int Refined Greater[i.T]) = j - i.value
baz: [I <: Int](i: shapeless.Witness.Aux[I])(j: eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Greater[i.T]])Int
```

```scala
scala> baz(Witness(2))(4)
res2: Int = 2
```

```scala
scala> baz(Witness(6))(4)
<console>:21: error: Predicate failed: (4 > 6).
       baz(Witness(6))(4)
                       ^
```

With a little more involved definition of our function we can even hide
`Witness` at the call site:

```scala
scala> class BarAux[I <: Int](i: Witness.Aux[I]) {
     |   def apply(j: Int Refined Greater[i.T]) = j - i.value
     | }
defined class BarAux

scala> def bar(i: Int) = new BarAux(Witness(i))
bar: (i: Int)BarAux[i.type]
```

```scala
scala> bar(2)(4)
res4: Int = 2
```

```scala
scala> bar(6)(4)
<console>:22: error: Predicate failed: (4 > 6).
       bar(6)(4)
              ^
```

[spec-3.2.1]: http://www.scala-lang.org/files/archive/spec/2.11/03-types.html#singleton-types
