# Dependent refinement

```tut:silent
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.string.StartsWith
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

Unfortunately Scala currently does not allow to use singleton types of
`AnyVal`s, see [section 3.2.1][spec-3.2.1] of the Scala Language
Specification:

```tut:fail
def bar(i: Int)(j: Int Refined Greater[i.type]) = j - i
```

But fortunately this restriction will be lifted with the implementation
of [SIP-23][SIP-23] in a future Scala version.

[spec-3.2.1]: http://www.scala-lang.org/files/archive/spec/2.11/03-types.html#singleton-types
[SIP-23]: http://docs.scala-lang.org/sips/42.type.html
