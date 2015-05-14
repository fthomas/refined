# refined
[![Build Status](https://img.shields.io/travis/fthomas/refined.svg)](https://travis-ci.org/fthomas/refined)
[![Download](https://api.bintray.com/packages/fthomas/maven/refined/images/download.svg)](https://bintray.com/fthomas/maven/refined/_latestVersion)
[![Gitter](https://img.shields.io/badge/GITTER-join%20chat-brightgreen.svg)](https://gitter.im/fthomas/refined?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Coverage Status](https://img.shields.io/coveralls/fthomas/refined/master.svg)](https://coveralls.io/r/fthomas/refined)
[![Codacy Badge](https://img.shields.io/codacy/e4f25ef2656e463e8fed3f4f9314abdb.svg)](https://www.codacy.com/app/fthomas/refined)

## Overview

This is a port of the [`refined`][refined.hs] Haskell library to Scala.
The linked websites provides an excellent motivation why this kind of library
is useful.

This library consists of:

 * Type-level predicates for refining other types, like `UpperCase`, `Positive`,
   `Greater[_0] And LessEqual[_2]`, or `Length[Greater[_5]]`. There are also higher
   order predicates for combining proper predicates like `And[_, _]`, `Or[_, _]`,
   `Not[_]`, or `Length[_]`.

 * A `Predicate` type class that is able to validate a concrete data type (like `Double`)
   against a type-level predicate (like `Positive`).

 * Two functions `refine` and `refineLit` that take a predicate `P` and some value
   of type `T`, validates this value with a `Predicate[P, T]` and returns the value
   with type `T @@ P` if validation was successful or an error otherwise.
   (`@@` is [shapeless'](https://github.com/milessabin/shapeless) type for tagging types :-))

## Examples

```scala
scala> refine[Positive, Int](5)
res0: Either[String, Int @@ Positive] = Right(5)

scala> refine[Positive, Int](-5)
res1: Either[String, Int @@ Positive] = Left(Predicate failed: (-5 > 0).)

scala> refineLit[NonEmpty, String]("Hello")
res2: String @@ NonEmpty = Hello

scala> refineLit[NonEmpty, String]("")
<console>:27: error: Predicate isEmpty() did not fail.
            refineLit[NonEmpty, String]("")
                                       ^

scala> type ZeroToOne = Not[Less[_0]] And Not[Greater[_1]]
defined type alias ZeroToOne

scala> refineLit[ZeroToOne, Double](1.8)
<console>:27: error: Right predicate of (!(1.8 < 0) && !(1.8 > 1)) failed: Predicate (1.8 > 1) did not fail.
              refineLit[ZeroToOne, Double](1.8)
                                          ^
```

Note that `refineLit` (which only supports literals) is implemented as macro
and checks at compile time if the given literal conforms to the predicate.

## Installation

To get the latest version of the library, add the following to your SBT build:

    resolvers += "Frank's Bintray" at "https://dl.bintray.com/fthomas/maven"
    
    libraryDependencies += "eu.timepit" %% "refined" % "<version>"

where `<version>` is a combination of a version number and a Git hash. See
https://bintray.com/fthomas/maven/refined for the latest version.

## Documentation

The latest API documentation is available at: http://fthomas.github.io/refined/latest/api/

## Related projects

This library is heavily inspired by the [`refined`][refined.hs] library for
Haskell. It even stole its name! [`bond`][bond] is another Scala library that
provides type-level validations.

## License

`refined` is licensed under the MIT license, available at http://opensource.org/licenses/MIT
and also in the [LICENSE](https://github.com/fthomas/refined/blob/master/LICENSE) file.

[bond]: https://github.com/fwbrasil/bond
[refined.hs]: http://nikita-volkov.github.io/refined/
