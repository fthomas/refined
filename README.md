# refined
[![Build Status](https://img.shields.io/travis/fthomas/refined.svg)](https://travis-ci.org/fthomas/refined)
[![Download](https://img.shields.io/maven-central/v/eu.timepit/refined_2.11.svg)][search.maven]
[![Gitter](https://img.shields.io/badge/GITTER-join%20chat-brightgreen.svg)](https://gitter.im/fthomas/refined?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Coverage Status](https://img.shields.io/coveralls/fthomas/refined/master.svg)](https://coveralls.io/r/fthomas/refined)
[![Codacy Badge](https://img.shields.io/codacy/e4f25ef2656e463e8fed3f4f9314abdb.svg)](https://www.codacy.com/app/fthomas/refined)

## Overview

This is a port of the [refined][refined.hs] Haskell library to Scala.
The linked websites provides an excellent motivation why this kind of library
is useful.

This library consists of:

* Type-level predicates for refining other types, like `UpperCase`, `Positive`,
  `Greater[_0] And LessEqual[_2]`, or `Length[Greater[_5]]`. There are also higher
  order predicates for combining proper predicates like `And[_, _]`, `Or[_, _]`,
  `Not[_]`, `Forall[_]`, or `Size[_]`.

* A `Predicate` type class that is able to validate a concrete data type (like `Double`)
  against a type-level predicate (like `Positive`).

* Two functions `refine` and `refineLit` that take a predicate `P` and some value
  of type `T`, validates this value with a `Predicate[P, T]` and returns the value
  with type `T @@ P` if validation was successful or an error otherwise
  (`@@` is [shapeless'][shapeless] type for tagging types :-)). `refine` validates
  values at runtime and returns an `Either[String, T @@ P]` while `refineLit` is a
  macro and validates literals at compile-time. So it either returns a `T @@ P` or
  compilation fails with an error.

## Examples

```scala
scala> refine[Positive](5)
res0: Either[String, Int @@ Positive] = Right(5)

scala> refine[Positive](-5)
res1: Either[String, Int @@ Positive] = Left(Predicate failed: (-5 > 0).)

scala> refineLit[NonEmpty]("Hello")
res2: String @@ NonEmpty = Hello

scala> refineLit[NonEmpty]("")
<console>:27: error: Predicate isEmpty() did not fail.
            refineLit[NonEmpty]("")
                               ^

scala> type ZeroToOne = Not[Less[_0]] And Not[Greater[_1]]
defined type alias ZeroToOne

scala> refineLit[ZeroToOne](1.8)
<console>:27: error: Right predicate of (!(1.8 < 0) && !(1.8 > 1)) failed: Predicate (1.8 > 1) did not fail.
              refineLit[ZeroToOne](1.8)
                                  ^

scala> refineLit[AnyOf[Digit :: Letter :: Whitespace :: HNil]]('F')
res3: Char @@ AnyOf[Digit :: Letter :: Whitespace :: HNil] = F

scala> refineLit[MatchesRegex[W.`"[0-9]+"`.T]]("123.")
<console>:34: error: Predicate failed: "123.".matches("[0-9]+").
              refineLit[MatchesRegex[W.`"[0-9]+"`.T]]("123.")
                                                     ^
```

Note that `W` is a shortcut for [`shapeless.Witness`][singleton-types]
which allows to express singleton types of literal values.

## Installation

The latest version of the library is 0.0.3, which is built against Scala 2.11.

If you're using SBT, add the following to your build file:

    libraryDependencies += "eu.timepit" %% "refined" % "0.0.3"

Instructions for Maven and other build tools is available at [search.maven.org][search.maven].

## Documentation

API documentation of the latest release is available at:
http://fthomas.github.io/refined/latest/api/

There are also further (typechecked) examples in the [`docs`][docs]
directory including one for defining [custom predicates][custom-pred].

[docs]: https://github.com/fthomas/refined/tree/master/docs
[custom-pred]: https://github.com/fthomas/refined/tree/master/docs/point.md

## Provided predicates

The library comes with these predefined predicates:

[`boolean`](https://github.com/fthomas/refined/blob/master/src/main/scala/eu/timepit/refined/boolean.scala)

* `True`: constant predicate that is always `true`
* `False`: constant predicate that is always `false`
* `Not[P]`: negation of the predicate `P`
* `And[A, B]`: conjunction of the predicates `A` and `B`
* `Or[A, B]`: disjunction of the predicates `A` and `B`
* `Xor[A, B]`: exclusive disjunction of the predicates `A` and `B`
* `AllOf[PS]`: conjunction of all predicates in `PS`
* `AnyOf[PS]`: disjunction of all predicates in `PS`
* `OneOf[PS]`: exclusive disjunction of all predicates in `PS`

[`char`](https://github.com/fthomas/refined/blob/master/src/main/scala/eu/timepit/refined/char.scala)

* `Digit`: checks if a `Char` is a digit
* `Letter`: checks if a `Char` is a letter
* `LetterOrDigit`: checks if a `Char` is a letter or digit
* `LowerCase`: checks if a `Char` is a lower case character
* `UpperCase`: checks if a `Char` is an upper case character
* `Whitespace`: checks if a `Char` is white space

[`collection`](https://github.com/fthomas/refined/blob/master/src/main/scala/eu/timepit/refined/collection.scala)

* `Contains[U]`: checks if a `TraversableOnce` contains a value equal to `U`
* `Count[PA, PC]`: counts the number of elements in a `TraversableOnce` which
  satisfy the predicate `PA` and passes the result to the predicate `PC`
* `Empty`: checks if a `TraversableOnce` is empty
* `NonEmpty`: checks if a `TraversableOnce` is not empty
* `Forall[P]`: checks if the predicate `P` holds for all elements of a
  `TraversableOnce`
* `Exists[P]`: checks if the predicate `P` holds for some elements of a
  `TraversableOnce`
* `Head[P]`: checks if the predicate `P` holds for the first element of
  a `Traversable`
* `Index[N, P]`: checks if the predicate `P` holds for the element at
  index `N` of a sequence
* `Last[P]`: checks if the predicate `P` holds for the last element of
  a `Traversable`
* `Size[P]`: checks if the size of a `TraversableOnce` satisfies the predicate `P`
* `MinSize[N]`: checks if the size of a `TraversableOnce` is greater than
  or equal to `N`
* `MaxSize[N]`: checks if the size of a `TraversableOnce` is less than
  or equal to `N`

[`generic`](https://github.com/fthomas/refined/blob/master/src/main/scala/eu/timepit/refined/generic.scala)

* `Equal[U]`: checks if a value is equal to `U`
* `IsNull`:  checks if a value is `null`
* `NonNull`: checks if a value is not `null`

[`numeric`](https://github.com/fthomas/refined/blob/master/src/main/scala/eu/timepit/refined/numeric.scala)

* `Less[N]`: checks if a numeric value is less than `N`
* `LessEqual[N]`: checks if a numeric value is less than or equal to `N`
* `Greater[N]`: checks if a numeric value is greater than `N`
* `GreaterEqual[N]`: checks if a numeric value is greater than or equal to `N`
* `Positive`: checks if a numeric value is greater than zero
* `Negative`: checks if a numeric value is less than zero
* `Interval[L, H]`: checks if a numeric value is in the interval [`L`, `H`]

[`string`](https://github.com/fthomas/refined/blob/master/src/main/scala/eu/timepit/refined/string.scala)

* `MatchesRegex[R]`: checks if a `String` matches the regular expression `R`

## Related projects

This library is heavily inspired by the [refined][refined.hs] library for
Haskell. It even stole its name! Another Scala library that provides type-level
validations is [bond][bond].

## License

refined is licensed under the MIT license, available at http://opensource.org/licenses/MIT
and also in the [LICENSE](https://github.com/fthomas/refined/blob/master/LICENSE) file.

[bond]: https://github.com/fwbrasil/bond
[refined.hs]: http://nikita-volkov.github.io/refined
[search.maven]: http://search.maven.org/#search|ga|1|eu.timepit.refined
[shapeless]: https://github.com/milessabin/shapeless
[singleton-types]: https://github.com/milessabin/shapeless/wiki/Feature-overview:-shapeless-2.0.0#singleton-typed-literals
