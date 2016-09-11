# refined: simple refinement types for Scala
[![Build Status](https://img.shields.io/travis/fthomas/refined/master.svg)](https://travis-ci.org/fthomas/refined)
[![codecov.io](https://img.shields.io/codecov/c/github/fthomas/refined.svg)](http://codecov.io/github/fthomas/refined)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/fthomas/refined?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Scaladex](https://index.scala-lang.org/fthomas/refined/refined/latest.svg?color=blue)](https://index.scala-lang.org/fthomas/refined/refined)
[![Scaladoc](https://www.javadoc.io/badge/eu.timepit/refined_2.11.svg?color=blue&label=Scaladoc)](https://www.javadoc.io/doc/eu.timepit/refined_2.11)

**refined** is a Scala library for refining types with type-level predicates
which constrain the set of values described by the refined type. It started
as a port of the [refined][refined.hs] Haskell library (which also provides
an excellent motivation why this kind of library is useful).

A quick example:

```scala
import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._

// This refines Int with the Positive predicate and checks via an
// implicit macro that the assigned value satisfies it:
scala> val i1: Int Refined Positive = 5
i1: Int Refined Positive = 5

// If the value does not satisfy the predicate, we get a meaningful
// compile error:
scala> val i2: Int Refined Positive = -5
<console>:22: error: Predicate failed: (-5 > 0).
       val i2: Int Refined Positive = -5
                                       ^

// There is also the explicit refineMV macro that can infer the base
// type from its parameter:
scala> refineMV[Positive](5)
res0: Int Refined Positive = 5

// Macros can only validate literals because their values are known at
// compile-time. To validate arbitrary (runtime) values we can use the
// refineV function:

scala> val x = 42 // suppose the value of x is not known at compile-time

scala> refineV[Positive](x)
res1: Either[String, Int Refined Positive] = Right(42)

scala> refineV[Positive](-x)
res2: Either[String, Int Refined Positive] = Left(Predicate failed: (-42 > 0).)
```

**refined** also contains inference rules for converting between different
refined types. For example, ``Int Refined Greater[W.`10`.T]`` can be safely
converted to `Int Refined Positive` because all integers greater than ten
are also positive. The type conversion of refined types is a compile-time
operation that is provided by the library:

```scala
scala> val a: Int Refined Greater[W.`5`.T] = 10
a: Int Refined Greater[Int(5)] = 10

// Since every value greater than 5 is also greater than 4, `a` can be
// ascribed the type Int Refined Greater[W.`4`.T]:
scala> val b: Int Refined Greater[W.`4`.T] = a
b: Int Refined Greater[Int(4)] = 10

// An unsound ascription leads to a compile error:
scala> val c: Int Refined Greater[W.`6`.T] = a
<console>:23: error: type mismatch (invalid inference):
 Greater[Int(5)] does not imply
 Greater[Int(6)]
       val c: Int Refined Greater[W.`6`.T] = a
                                             ^
```

This mechanism allows to pass values of more specific types (e.g.
``Int Refined Greater[W.`10`.T]``) to functions that take a more general
type (e.g. `Int Refined Positive`) without manual intervention.

Note that [`W`](http://fthomas.github.io/refined/latest/api/index.html#eu.timepit.refined.package@W:shapeless.Witness.type)
is a shortcut for [`shapeless.Witness`][singleton-types] which provides
syntax for [literal-based singleton types][sip-23].

## Table of contents

1. [More examples](#more-examples)
2. [Using refined](#using-refined)
3. [Documentation](#documentation)
4. [Provided predicates](#provided-predicates)
5. [Contributors and participation](#contributors-and-participation)
6. [Projects using refined](#projects-using-refined)
7. [Performance concerns](#performance-concerns)
8. [Related projects](#related-projects)
9. [License](#license)

## More examples

```scala
import eu.timepit.refined.boolean._
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.generic._
import eu.timepit.refined.string._
import shapeless.{ ::, HNil }

scala> refineMV[NonEmpty]("Hello")
res2: String Refined NonEmpty = Hello

scala> refineMV[NonEmpty]("")
<console>:39: error: Predicate isEmpty() did not fail.
            refineMV[NonEmpty]("")
                              ^

scala> type ZeroToOne = Not[Less[W.`0.0`.T]] And Not[Greater[W.`1.0`.T]]
defined type alias ZeroToOne

scala> refineMV[ZeroToOne](1.8)
<console>:40: error: Right predicate of (!(1.8 < 0.0) && !(1.8 > 1.0)) failed: Predicate (1.8 > 1.0) did not fail.
       refineMV[ZeroToOne](1.8)
                          ^

scala> refineMV[AnyOf[Digit :: Letter :: Whitespace :: HNil]]('F')
res3: Char Refined AnyOf[Digit :: Letter :: Whitespace :: HNil] = F

scala> refineMV[MatchesRegex[W.`"[0-9]+"`.T]]("123.")
<console>:39: error: Predicate failed: "123.".matches("[0-9]+").
              refineMV[MatchesRegex[W.`"[0-9]+"`.T]]("123.")
                                                    ^

scala> val d1: Char Refined Equal[W.`'3'`.T] = '3'
d1: Char Refined Equal[Char('3')] = 3

scala> val d2: Char Refined Digit = d1
d2: Char Refined Digit = 3

scala> val d3: Char Refined Letter = d1
<console>:39: error: type mismatch (invalid inference):
 Equal[Char('3')] does not imply
 Letter
       val d3: Char Refined Letter = d1
                                     ^

scala> val r1: String Refined Regex = "(a|b)"
r1: String Refined Regex = (a|b)

scala> val r2: String Refined Regex = "(a|b"
<console>:38: error: Regex predicate failed: Unclosed group near index 4
(a|b
    ^
       val r2: String Refined Regex = "(a|b"
                                      ^

scala> val u1: String Refined Url = "htp://example.com"
<console>:38: error: Url predicate failed: unknown protocol: htp
       val u1: String Refined Url = "htp://example.com"
                                    ^
```

## Using refined

The latest version of the library is 0.5.0, which is available for Scala and
[Scala.js][scala.js] version 2.10, 2.11, and 2.12.0-M5.

If you're using sbt, add the following to your build:

```sbt
libraryDependencies ++= Seq(
  "eu.timepit" %% "refined"            % "0.5.0",
  "eu.timepit" %% "refined-scalaz"     % "0.5.0",         // optional
  "eu.timepit" %% "refined-scodec"     % "0.5.0",         // optional
  "eu.timepit" %% "refined-scalacheck" % "0.5.0" % "test" // optional
)
```

For Scala.js just replace `%%` with `%%%` above.

Instructions for Maven and other build tools are available at [search.maven.org][search.maven].

Release notes for the latest version are available in
[0.5.0.markdown](https://github.com/fthomas/refined/blob/master/notes/0.5.0.markdown).

The optional dependencies are add-on libraries that provide support for
other tag types or integration of refined types in other libraries:

* `refined-scalaz` for support of [Scalaz](https://github.com/scalaz/scalaz)'
  tag type (`scalaz.@@`)
* `refined-scodec` for integration with [scodec](http://scodec.org/)
* `refined-scalacheck` for [ScalaCheck](http://scalacheck.org/) type
  class instances of refined types

See also the list of [projects that use refined][built-with-refined]
for libraries that directly provide support for **refined**.

## Documentation

API documentation of the latest release is available at:
[http://fthomas.github.io/refined/latest/api/](http://fthomas.github.io/refined/latest/api/#eu.timepit.refined.package)

There are further (type-checked) examples in the [`docs`][docs]
directory including ones for defining [custom predicates][custom-pred]
and working with [type aliases][type-aliases]. It also contains a
[description][design] of **refined's** design and internals.

Talks and other external resources are listed on the [Resources][resources]
page in the wiki.

[custom-pred]: https://github.com/fthomas/refined/blob/master/docs/custom_predicates.md
[design]: https://github.com/fthomas/refined/blob/master/docs/design.md
[docs]: https://github.com/fthomas/refined/tree/master/docs
[resources]: https://github.com/fthomas/refined/wiki/Resources
[type-aliases]: https://github.com/fthomas/refined/blob/master/docs/type_aliases.md

## Provided predicates

The library comes with these predefined predicates:

[`boolean`](https://github.com/fthomas/refined/blob/master/core/shared/src/main/scala/eu/timepit/refined/boolean.scala)

* `True`: constant predicate that is always `true`
* `False`: constant predicate that is always `false`
* `Not[P]`: negation of the predicate `P`
* `And[A, B]`: conjunction of the predicates `A` and `B`
* `Or[A, B]`: disjunction of the predicates `A` and `B`
* `Xor[A, B]`: exclusive disjunction of the predicates `A` and `B`
* `Nand[A, B]`: negated conjunction of the predicates `A` and `B`
* `Nor[A, B]`: negated disjunction of the predicates `A` and `B`
* `AllOf[PS]`: conjunction of all predicates in `PS`
* `AnyOf[PS]`: disjunction of all predicates in `PS`
* `OneOf[PS]`: exclusive disjunction of all predicates in `PS`

[`char`](https://github.com/fthomas/refined/blob/master/core/shared/src/main/scala/eu/timepit/refined/char.scala)

* `Digit`: checks if a `Char` is a digit
* `Letter`: checks if a `Char` is a letter
* `LetterOrDigit`: checks if a `Char` is a letter or digit
* `LowerCase`: checks if a `Char` is a lower case character
* `UpperCase`: checks if a `Char` is an upper case character
* `Whitespace`: checks if a `Char` is white space

[`collection`](https://github.com/fthomas/refined/blob/master/core/shared/src/main/scala/eu/timepit/refined/collection.scala)

* `Contains[U]`: checks if a `Traversable` contains a value equal to `U`
* `Count[PA, PC]`: counts the number of elements in a `Traversable` which satisfy the
  predicate `PA` and passes the result to the predicate `PC`
* `Empty`: checks if a `Traversable` is empty
* `NonEmpty`: checks if a `Traversable` is not empty
* `Forall[P]`: checks if the predicate `P` holds for all elements of a `Traversable`
* `Exists[P]`: checks if the predicate `P` holds for some elements of a `Traversable`
* `Head[P]`: checks if the predicate `P` holds for the first element of a `Traversable`
* `Index[N, P]`: checks if the predicate `P` holds for the element at index `N` of a sequence
* `Init[P]`: checks if the predicate `P` holds for all but the last element of a `Traversable`
* `Last[P]`: checks if the predicate `P` holds for the last element of a `Traversable`
* `Tail[P]`: checks if the predicate `P` holds for all but the first element of a `Traversable`
* `Size[P]`: checks if the size of a `Traversable` satisfies the predicate `P`
* `MinSize[N]`: checks if the size of a `Traversable` is greater than or equal to `N`
* `MaxSize[N]`: checks if the size of a `Traversable` is less than or equal to `N`

[`generic`](https://github.com/fthomas/refined/blob/master/core/shared/src/main/scala/eu/timepit/refined/generic.scala)

* `Equal[U]`: checks if a value is equal to `U`
* `Eval[S]`: checks if a value applied to the predicate `S` yields `true`
* `ConstructorNames[P]`: checks if the constructor names of a sum type satisfy `P`
* `FieldNames[P]`: checks if the field names of a product type satisfy `P`
* `Subtype[U]`: witnesses that the type of a value is a subtype of `U`
* `Supertype[U]`: witnesses that the type of a value is a supertype of `U`

[`numeric`](https://github.com/fthomas/refined/blob/master/core/shared/src/main/scala/eu/timepit/refined/numeric.scala)

* `Less[N]`: checks if a numeric value is less than `N`
* `LessEqual[N]`: checks if a numeric value is less than or equal to `N`
* `Greater[N]`: checks if a numeric value is greater than `N`
* `GreaterEqual[N]`: checks if a numeric value is greater than or equal to `N`
* `Positive`: checks if a numeric value is greater than zero
* `NonPositive`: checks if a numeric value is zero or negative
* `Negative`: checks if a numeric value is less than zero
* `NonNegative`: checks if a numeric value is zero or positive
* `Interval.Open[L, H]`: checks if a numeric value is in the interval (`L`, `H`)
* `Interval.OpenClosed[L, H]`: checks if a numeric value is in the interval (`L`, `H`]
* `Interval.ClosedOpen[L, H]`: checks if a numeric value is in the interval [`L`, `H`)
* `Interval.Closed[L, H]`: checks if a numeric value is in the interval [`L`, `H`]

[`string`](https://github.com/fthomas/refined/blob/master/core/shared/src/main/scala/eu/timepit/refined/string.scala)

* `EndsWith[S]`: checks if a `String` ends with the suffix `S`
* `MatchesRegex[S]`: checks if a `String` matches the regular expression `S`
* `Regex`: checks if a `String` is a valid regular expression
* `StartsWith[S]`: checks if a `String` starts with the prefix `S`
* `Uri`: checks if a `String` is a valid URI
* `Url`: checks if a `String` is a valid URL
* `Uuid`: checks if a `String` is a valid UUID
* `Xml`: checks if a `String` is well-formed XML
* `XPath`: checks if a `String` is a valid XPath expression

## Contributors and participation

* [Alexandre Archambault](https://github.com/alexarchambault) ([@alxarchambault](https://twitter.com/alxarchambault))
* [Dale Wijnand](https://github.com/dwijnand) ([@dwijnand](https://twitter.com/dwijnand))
* [Frank S. Thomas](https://github.com/fthomas) ([@fst9000](https://twitter.com/fst9000))
* [Jean-Rémi Desjardins](https://github.com/jedesah) ([@jrdesjardins](https://twitter.com/jrdesjardins))
* [John-Michael Reed](https://github.com/JohnReedLOL)
* [Shohei Shimomura](https://github.com/sh0hei) ([@sm0kym0nkey](https://twitter.com/sm0kym0nkey))
* [Vladimir Koshelev](https://github.com/koshelev) ([@vlad_koshelev](https://twitter.com/vlad_koshelev))
* Your name here :-)

**refined** is a [Typelevel][typelevel] project. This means we embrace pure,
typeful, functional programming, and provide a safe and friendly environment
for teaching, learning, and contributing as described in the Typelevel
[code of conduct][code-of-conduct].

## Projects using refined

Please see the wiki for an [incomplete list of projects and companies][built-with-refined]
which use **refined**. If you are using the library and your project isn't
listed yet, please add it.

## Performance concerns

Using **refined's** macros for compile-time refinement has zero runtime
overhead for reference types and only causes boxing for value types.
Refer to [RefineJavapSpec][RefineJavapSpec] and [InferJavapSpec][InferJavapSpec]
for a detailed analysis of the runtime component of refinement types on the JVM.

[InferJavapSpec]: https://github.com/fthomas/refined/blob/master/contrib/scalaz/jvm/src/test/scala-2.11/eu/timepit/refined/scalaz/InferJavapSpec.scala
[RefineJavapSpec]: https://github.com/fthomas/refined/blob/master/contrib/scalaz/jvm/src/test/scala-2.11/eu/timepit/refined/scalaz/RefineJavapSpec.scala

## Related projects

* [bond](https://github.com/fwbrasil/bond): Type-level validation for Scala
* [F7](http://research.microsoft.com/en-us/projects/f7/): Refinement Types for F#
* [LiquidHaskell](http://goto.ucsd.edu/~rjhala/liquid/haskell/blog/about/):
  Refinement Types via SMT and Predicate Abstraction
* [refined][refined.hs]: Refinement types with static and runtime checking for
  Haskell. **refined** was inspired this library and even stole its name!
* [refscript](https://github.com/UCSD-PL/refscript): Refinement Types for TypeScript
* [Subtypes in Perl 6](https://design.perl6.org/S12.html#Types_and_Subtypes)

## License

**refined** is licensed under the MIT license, available at http://opensource.org/licenses/MIT
and also in the [LICENSE](https://github.com/fthomas/refined/blob/master/LICENSE) file.

[built-with-refined]: https://github.com/fthomas/refined/wiki/Built-with-refined
[code-of-conduct]: http://typelevel.org/conduct.html
[refined.hs]: http://nikita-volkov.github.io/refined
[scala.js]: http://www.scala-js.org
[search.maven]: http://search.maven.org/#search|ga|1|eu.timepit.refined
[singleton-types]: https://github.com/milessabin/shapeless/wiki/Feature-overview:-shapeless-2.0.0#singleton-typed-literals
[sip-23]: http://docs.scala-lang.org/sips/pending/42.type.html
[typelevel]: http://typelevel.org
