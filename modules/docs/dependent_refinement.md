# Dependent refinement

```scala
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Greater
import eu.timepit.refined.string.StartsWith
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
<console>:20: error: Predicate failed: "abcd".startsWith("cd").
       foo("cd")("abcd")
                 ^
```

Unfortunately Scala currently does not allow to use singleton types of
`AnyVal`s, see [section 3.2.1][spec-3.2.1] of the Scala Language
Specification:

```scala
scala> def bar(i: Int)(j: Int Refined Greater[i.type]) = j - i
<console>:18: error: type mismatch;
 found   : i.type (with underlying type Int)
 required: AnyRef
       def bar(i: Int)(j: Int Refined Greater[i.type]) = j - i
                                              ^
```

But fortunately this restriction will be lifted with the implementation
of [SIP-23][SIP-23] in a future Scala version.

[spec-3.2.1]: http://www.scala-lang.org/files/archive/spec/2.11/03-types.html#singleton-types
[SIP-23]: http://docs.scala-lang.org/sips/42.type.html
