# Dependent refinement

```tut:silent
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.string.StartsWith
import shapeless.Witness
```

Scala's path dependent types makes it possible to express refinements
that depend other statically known values:

```tut
def foo(a: String)(b: String Refined StartsWith[a.type]) = a + b
```

```tut
foo("ab")("abcd")
```

```tut:fail
foo("cd")("abcd")
```

Unfortunately Scala does not allow to use singleton types of `AnyVal`s,
see [section 3.2.1][spec-3.2.1] of the Scala Language Specification:

```tut:fail
def bar(i: Int)(j: Int Refined Greater[i.type]) = j - i
```

### shapeless to the rescue

We can however use `shapeless.Witness` to workaround this limitation in
the specification. `Witness` captures the singleton type of an `AnyVal`
and makes it available via the type member `T`:

```tut
def baz[I <: Int](i: Witness.Aux[I])(j: Int Refined Greater[i.T]) = j - i.value
```

```tut:nofail
baz(Witness(2))(4)
```

```tut:fail
baz(Witness(6))(4)
```

With a little more involved definition of our function we can even hide
`Witness` at the call site:

```tut
class BarAux[I <: Int](i: Witness.Aux[I]) {
  def apply(j: Int Refined Greater[i.T]) = j - i.value
}

def bar(i: Int) = new BarAux(Witness(i))
```

```tut:nofail
bar(2)(4)
```

```tut:fail
bar(6)(4)
```

[spec-3.2.1]: http://www.scala-lang.org/files/archive/spec/2.11/03-types.html#singleton-types
