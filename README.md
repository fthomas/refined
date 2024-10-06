# refined: simple refinement types for Scala
[![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/fthomas/refined/ci.yml)](https://github.com/fthomas/refined/actions/workflows/ci.yml)
[![codecov.io](https://img.shields.io/codecov/c/github/fthomas/refined.svg)](http://codecov.io/github/fthomas/refined)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/fthomas/refined?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![refined Scala version support](https://index.scala-lang.org/fthomas/refined/refined/latest-by-scala-version.svg?color=blue)](https://index.scala-lang.org/fthomas/refined/refined)
[![Scaladoc](https://www.javadoc.io/badge/eu.timepit/refined_2.12.svg?color=blue&label=Scaladoc)](https://javadoc.io/doc/eu.timepit/refined_2.12/0.11.2)

**refined** is a Scala library for refining types with type-level predicates
which constrain the set of values described by the refined type.
It started as a port of the [refined][refined.hs] Haskell library by
[Nikita Volkov](https://github.com/nikita-volkov) (which also provides an excellent
motivation why this kind of library is useful).
The idea of expressing constraints at the type-level as Scala library was first
explored by [Flavio W. Brasil](https://github.com/fwbrasil) in [bond][bond].

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
refined types. For example, ``Int Refined Greater[10]`` can be safely
converted to `Int Refined Positive` because all integers greater than ten
are also positive. The type conversion of refined types is a compile-time
operation that is provided by the library:

```scala
scala> val a: Int Refined Greater[5] = 10
a: Int Refined Greater[Int(5)] = 10

// Since every value greater than 5 is also greater than 4, `a` can be
// ascribed the type Int Refined Greater[4]:
scala> val b: Int Refined Greater[4] = a
b: Int Refined Greater[Int(4)] = 10

// An unsound ascription leads to a compile error:
scala> val c: Int Refined Greater[6] = a
                                       ^
       error: type mismatch (invalid inference):
               eu.timepit.refined.numeric.Greater[5] does not imply
               eu.timepit.refined.numeric.Greater[6]
```

This mechanism allows to pass values of more specific types (e.g.
``Int Refined Greater[10]``) to functions that take a more general
type (e.g. `Int Refined Positive`) without manual intervention.

### prior Scala 2.13 without literal types

Since there are no literal types prior to Scala 2.13 the literals must be created with shapeless:

```scala
scala> val a: Int Refined Greater[W.`5`.T] = 10
a: Int Refined Greater[Int(5)] = 10
scala> val b: Int Refined Greater[W.`4`.T] = a
b: Int Refined Greater[Int(4)] = 10
```

**Note** that [`W`](https://static.javadoc.io/eu.timepit/refined_2.12/0.11.2/eu/timepit/refined/index.html#W:shapeless.Witness.type)
is a shortcut for [`shapeless.Witness`][singleton-types] which provides
syntax for [literal-based singleton types][sip-23].

## Table of contents

1. [More examples](#more-examples)
2. [Using refined](#using-refined)
3. [Community](#community)
4. [Documentation](#documentation)
5. [Provided predicates](#provided-predicates)
6. [Contributors and participation](#contributors-and-participation)
7. [Related projects](#related-projects)
8. [License](#license)

## More examples

```scala
import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import eu.timepit.refined.api.{RefType, Refined}
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

scala> type ZeroToOne = Not[Less[0.0]] And Not[Greater[1.0]]
defined type alias ZeroToOne

scala> refineMV[ZeroToOne](1.8)
<console>:40: error: Right predicate of (!(1.8 < 0.0) && !(1.8 > 1.0)) failed: Predicate (1.8 > 1.0) did not fail.
       refineMV[ZeroToOne](1.8)
                          ^

scala> refineMV[AnyOf[Digit :: Letter :: Whitespace :: HNil]]('F')
res3: Char Refined AnyOf[Digit :: Letter :: Whitespace :: HNil] = F

scala> refineMV[MatchesRegex["[0-9]+"]]("123.")
<console>:39: error: Predicate failed: "123.".matches("[0-9]+").
              refineMV[MatchesRegex[W.`"[0-9]+"`.T]]("123.")
                                                    ^

scala> val d1: Char Refined Equal['3'] = '3'
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

// Here we define a refined type "Int with the predicate (7 <= value < 77)".
scala> type Age = Int Refined Interval.ClosedOpen[7, 77]

scala> val userInput = 55

// We can refine values with this refined type by either using `refineV`
// with an explicit return type
scala> val ageEither1: Either[String, Age] = refineV(userInput)
ageEither1: Either[String,Age] = Right(55)

// or by using `RefType.applyRef` with the refined type as type parameter.
scala> val ageEither2 = RefType.applyRef[Age](userInput)
ageEither2: Either[String,Age] = Right(55)
```

## Using refined

The latest version of the library is 0.11.2, which is available for Scala and
[Scala.js][scala.js] version 2.12 and 2.13.

If you're using sbt, add the following to your build:

```sbt
libraryDependencies ++= Seq(
  "eu.timepit" %% "refined"                 % "0.11.2",
  "eu.timepit" %% "refined-cats"            % "0.11.2", // optional
  "eu.timepit" %% "refined-eval"            % "0.11.2", // optional, JVM-only
  "eu.timepit" %% "refined-jsonpath"        % "0.11.2", // optional, JVM-only
  "eu.timepit" %% "refined-pureconfig"      % "0.11.2", // optional, JVM-only
  "eu.timepit" %% "refined-scalacheck"      % "0.11.2", // optional
  "eu.timepit" %% "refined-scalaz"          % "0.11.2", // optional
  "eu.timepit" %% "refined-scodec"          % "0.11.2", // optional
  "eu.timepit" %% "refined-scopt"           % "0.11.2", // optional
  "eu.timepit" %% "refined-shapeless"       % "0.11.2"  // optional
)
```

For Scala.js just replace `%%` with `%%%` above.

Instructions for Maven and other build tools are available at [search.maven.org][search.maven].

Release notes for the latest version are [here](https://github.com/fthomas/refined/releases/tag/v0.11.2).

## Community

### Internal modules

The project provides these optional extensions and library integrations:

* `refined-cats` provides [Cats](https://github.com/typelevel/cats)
  type class instances for refined types
* `refined-eval` provides the `Eval[S]` predicate that checks if a value
  applied to the predicate `S` yields `true`
* `refined-jsonpath` provides the `JSONPath` predicate that checks if a
  `String` is a valid [JSONPath](http://goessner.net/articles/JsonPath/)
* `refined-pureconfig` allows to read configuration with refined types
  using [PureConfig](https://github.com/pureconfig/pureconfig)
* `refined-scalacheck` allows to generate arbitrary values of refined types
  with [ScalaCheck](http://scalacheck.org/).
  Use `refined-scalacheck_1.13` instead if your other dependencies
  use scalacheck version 1.13
* `refined-scalaz` provides [Scalaz](https://github.com/scalaz/scalaz)
  type class instances for refined types and support for `scalaz.@@`
* `refined-scodec` allows binary decoding and encoding of refined types with
  [scodec](http://scodec.org/) and allows refining `scodec.bits.ByteVector`
* `refined-scopt` allows to read command line options with refined types
  using [scopt](https://github.com/scopt/scopt)
* `refined-shapeless`

### External modules

Below is an incomplete list of third-party extensions and library integrations
for **refined**. If your library is missing, please open a pull request to
list it here:

* [api-refiner](https://github.com/dgouyette/play-api-refiner)
* [argonaut-refined](https://github.com/alexarchambault/argonaut-shapeless)
* [atto-refined](https://github.com/tpolecat/atto)
* [case-app-refined](https://github.com/alexarchambault/case-app)
* [circe-refined](https://github.com/circe/circe)
* [ciris-refined](https://github.com/vlovgr/ciris)
* [cormorant-refined](https://github.com/ChristopherDavenport/cormorant)
* [coulomb-refined](https://github.com/erikerlandson/coulomb/tree/develop/coulomb-refined)
* [decline-refined](http://ben.kirw.in/decline/arguments.html#refined-support)
* [doobie-refined](https://github.com/tpolecat/doobie)
* [exercises-refined](https://github.com/ysusuk/exercises-refined)
* [extruder-refined](https://github.com/janstenpickle/extruder)
* [finch-refined](https://github.com/finagle/finch)
* [formulation-refined](https://github.com/vectos/formulation)
* [kantan.csv-refined](https://nrinaudo.github.io/kantan.csv/refined.html)
* [kantan.regex-refined](https://nrinaudo.github.io/kantan.regex/refined.html)
* [kantan.xpath-refined](https://nrinaudo.github.io/kantan.xpath/refined.html)
* [monocle-refined](https://github.com/julien-truffaut/Monocle)
* [neotypes-refined](https://github.com/neotypes/neotypes)
* [play-refined](https://github.com/kwark/play-refined)
* [play-json-refined](https://github.com/lunaryorn/play-json-refined)
* [play-json-refined](https://github.com/btlines/play-json-refined)
* [refined-anorm](https://github.com/derekmorr/refined-anorm)
* [refined-guava](https://github.com/derekmorr/refined-guava)
* [scanamo-refined](https://github.com/scanamo/scanamo)
* [seals-refined](https://github.com/durban/seals)
* [slick-refined](https://github.com/kwark/slick-refined)
* [spray-json-refined](https://github.com/typeness/spray-json-refined)
* [strictify-refined](https://github.com/cakesolutions/strictify)
* [validated-config](https://github.com/carlpulley/validated-config)
* [vulcan-refined](https://github.com/ovotech/vulcan)
* [xml-names-core](https://github.com/slamdata/scala-xml-names)

### Projects using refined

If your open source project is using **refined**, please consider opening
a pull request to list it here:

* [calypso](https://github.com/m2-oss/calypso): BSON library for Scala
* [Quasar](https://github.com/quasar-analytics/quasar): An open source
  NoSQL analytics engine which uses refined for natural and positive
  integer types
* [rvi_sota_server](https://github.com/GENIVI/rvi_sota_server): The SOTA
  Server Reference Implementation uses refined for domain specific types.
  like the [vehicle identification number (VIN)](https://en.wikipedia.org/wiki/Vehicle_identification_number).

### Adopters

Are you using **refined** in your organization or company? Please consider
opening a pull request to list it here:

* [PITS Global Data Recovery Services](https://www.pitsdatarecovery.net/)
* [AutoScout24](http://techblog.scout24.com/)
* [Besedo](https://besedo.com/)
* [Chatroulette](https://chatroulette.com/)
* [Colisweb](https://www.colisweb.com/fr/)
* [FOLIO](https://folio-sec.com/)
* [HolidayCheck](http://techblog.holidaycheck.com/)
* [Zalando](https://tech.zalando.de/)
* [ContentSquare](https://contentsquare.com/)
* [Dassault Systèmes](https://www.3ds.com/)
* [Hypefactors](https://www.hypefactors.com/)

## Documentation

API documentation of the latest release is available at:
<https://static.javadoc.io/eu.timepit/refined_2.12/0.11.2/eu/timepit/refined/index.html>

There are further (type-checked) examples in the [`docs`][docs]
directory including ones for defining [custom predicates][custom-pred]
and working with [type aliases][type-aliases]. It also contains a
[description][design] of **refined's** design and internals.

Talks and other external resources are listed on the [Resources][resources]
page in the wiki.

[custom-pred]: https://github.com/fthomas/refined/blob/master/modules/docs/custom_predicates.md
[design]: https://github.com/fthomas/refined/blob/master/modules/docs/design.md
[docs]: https://github.com/fthomas/refined/tree/master/modules/docs
[resources]: https://github.com/fthomas/refined/wiki/Resources
[type-aliases]: https://github.com/fthomas/refined/blob/master/modules/docs/type_aliases.md

## Provided predicates

The library comes with these predefined predicates:

[`boolean`](https://github.com/fthomas/refined/blob/master/modules/core/shared/src/main/scala-3.0+/eu/timepit/refined/boolean.scala)

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

[`char`](https://github.com/fthomas/refined/blob/master/modules/core/shared/src/main/scala/eu/timepit/refined/char.scala)

* `Digit`: checks if a `Char` is a digit
* `Letter`: checks if a `Char` is a letter
* `LetterOrDigit`: checks if a `Char` is a letter or digit
* `LowerCase`: checks if a `Char` is a lower case character
* `UpperCase`: checks if a `Char` is an upper case character
* `Whitespace`: checks if a `Char` is white space

[`collection`](https://github.com/fthomas/refined/blob/master/modules/core/shared/src/main/scala-3.0+/eu/timepit/refined/collection.scala)

* `Contains[U]`: checks if an `Iterable` contains a value equal to `U`
* `Count[PA, PC]`: counts the number of elements in an `Iterable` which satisfy the
  predicate `PA` and passes the result to the predicate `PC`
* `Empty`: checks if an `Iterable` is empty
* `NonEmpty`: checks if an `Iterable` is not empty
* `Forall[P]`: checks if the predicate `P` holds for all elements of an `Iterable`
* `Exists[P]`: checks if the predicate `P` holds for some elements of an `Iterable`
* `Head[P]`: checks if the predicate `P` holds for the first element of an `Iterable`
* `Index[N, P]`: checks if the predicate `P` holds for the element at index `N` of a sequence
* `Init[P]`: checks if the predicate `P` holds for all but the last element of an `Iterable`
* `Last[P]`: checks if the predicate `P` holds for the last element of an `Iterable`
* `Tail[P]`: checks if the predicate `P` holds for all but the first element of an `Iterable`
* `Size[P]`: checks if the size of an `Iterable` satisfies the predicate `P`
* `MinSize[N]`: checks if the size of an `Iterable` is greater than or equal to `N`
* `MaxSize[N]`: checks if the size of an `Iterable` is less than or equal to `N`

[`generic`](https://github.com/fthomas/refined/blob/master/modules/core/shared/src/main/scala-3.0+/eu/timepit/refined/generic.scala)

* `Equal[U]`: checks if a value is equal to `U`

[`numeric`](https://github.com/fthomas/refined/blob/master/modules/core/shared/src/main/scala-3.0+/eu/timepit/refined/numeric.scala)

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
* `Modulo[N, O]`: checks if an integral value modulo `N` is `O`
* `Divisible[N]`: checks if an integral value is evenly divisible by `N`
* `NonDivisible[N]`: checks if an integral value is not evenly divisible by `N`
* `Even`: checks if an integral value is evenly divisible by 2
* `Odd`: checks if an integral value is not evenly divisible by 2
* `NonNaN`: checks if a floating-point number is not NaN

[`string`](https://github.com/fthomas/refined/blob/master/modules/core/shared/src/main/scala-3.0+/eu/timepit/refined/string.scala)

* `EndsWith[S]`: checks if a `String` ends with the suffix `S`
* `IPv4`: checks if a `String` is a valid IPv4
* `IPv6`: checks if a `String` is a valid IPv6
* `MatchesRegex[S]`: checks if a `String` matches the regular expression `S`
* `Regex`: checks if a `String` is a valid regular expression
* `StartsWith[S]`: checks if a `String` starts with the prefix `S`
* `Uri`: checks if a `String` is a valid URI
* `Url`: checks if a `String` is a valid URL
* `Uuid`: checks if a `String` is a valid UUID
* `ValidByte`: checks if a `String` is a parsable `Byte`
* `ValidShort`: checks if a `String` is a parsable `Short`
* `ValidInt`: checks if a `String` is a parsable `Int`
* `ValidLong`: checks if a `String` is a parsable `Long`
* `ValidFloat`: checks if a `String` is a parsable `Float`
* `ValidDouble`: checks if a `String` is a parsable `Double`
* `ValidBigInt`: checks if a `String` is a parsable `BigInt`
* `ValidBigDecimal`: checks if a `String` is a parsable `BigDecimal`
* `Xml`: checks if a `String` is well-formed XML
* `XPath`: checks if a `String` is a valid XPath expression
* `Trimmed`: checks if a `String` has no leading or trailing whitespace
* `HexStringSpec`: checks if a `String` represents a hexadecimal number

## Contributors and participation

The following people have helped making **refined** great:

* [Alex](https://github.com/jhnsmth)
* [Alexandre Archambault](https://github.com/alexarchambault)
* [Arman Bilge](https://github.com/armanbilge)
* [Brian P. Holt](https://github.com/bpholt)
* [Chris Birchall](https://github.com/cb372)
* [Chris Hodapp](https://github.com/clhodapp)
* [Cody Allen](https://github.com/ceedubs)
* [Dale Wijnand](https://github.com/dwijnand)
* [Denys Shabalin](https://github.com/densh)
* [Derek Morr](https://github.com/derekmorr)
* [Didac](https://github.com/umbreak)
* [Diogo Castro](https://github.com/dcastro)
* [dm-tran](https://github.com/dm-tran)
* [Ender Tunç](https://github.com/endertunc)
* [Frank S. Thomas](https://github.com/fthomas)
* [Frederick Roth](https://github.com/froth)
* [Howard Perrin](https://github.com/howyp)
* [Iurii Susuk](https://github.com/ysusuk)
* [Ivan Klass](https://github.com/ivan-klass)
* [Jean-Rémi Desjardins](https://github.com/jedesah)
* [Jente Hidskes](https://github.com/Hjdskes)
* [Joe Greene](https://github.com/ClydeMachine)
* [John-Michael Reed](https://github.com/JohnReedLOL)
* [Julien BENOIT](https://github.com/jbenoit2011)
* [kalejami](https://github.com/kalejami)
* [kenji yoshida](https://github.com/xuwei-k)
* [kusamakura](https://github.com/kusamakura)
* [急須](https://github.com/kyusu)
* [Leif Wickland](https://github.com/leifwickland)
* [Luis Miguel Mejía Suárez](https://github.com/BalmungSan)
* [Mateusz Wójcik](https://github.com/matwojcik)
* [Matt Pickering](https://github.com/matthedude)
* [Matthieu Jacquot](https://github.com/err0r500)
* [Michael Thomas](https://github.com/Michaelt293)
* [Michal Sitko](https://github.com/note)
* [Naoki Aoyama](https://github.com/aoiroaoino)
* [Nicolas Rinaudo](https://github.com/nrinaudo)
* [Olli Helenius](https://github.com/liff)
* [Richard Gomes](https://github.com/frgomes)
* [ronanM](https://github.com/ronanM)
* [Sam Guymer](https://github.com/guymers)
* [Sam Halliday](https://github.com/fommil)
* [Shawn Garner](https://github.com/BusyByte)
* [Shohei Shimomura](https://github.com/sh0hei)
* [Shunsuke Otani](https://github.com/zaneli)
* [Tim Steinbach](https://github.com/NeQuissimus)
* [Torsten Scholak](https://github.com/tscholak)
* [Viktor Lövgren](https://github.com/vlovgr)
* [Vladimir Koshelev](https://github.com/koshelev)
* [Yuki Ishikawa](https://github.com/rider-yi)
* [Zainab Ali](https://github.com/zainab-ali)
* Your name here :-)

**refined** is a [Typelevel][typelevel] project. This means we embrace pure,
typeful, functional programming, and provide a safe and friendly environment
for teaching, learning, and contributing as described in the
[Scala Code of Conduct](http://scala-lang.org/conduct.html).

## Related projects

* [SMT-Based Checking of Predicate-Qualified Types for Scala](http://lara.epfl.ch/~kuncak/papers/SchmidKuncak16CheckingPredicate.pdf)
* [bond][bond]: Type-level validation for Scala
* [F7](http://research.microsoft.com/en-us/projects/f7/): Refinement Types for F#
* [LiquidHaskell](https://ucsd-progsys.github.io/liquidhaskell-blog/):
  Refinement Types via SMT and Predicate Abstraction
* [Refinement Types in Typed Racket](http://blog.racket-lang.org/2017/11/adding-refinement-types.html)
* [refined][refined.hs]: Refinement types with static and runtime checking for
  Haskell. **refined** was inspired by this library and even stole its name!
* [refscript](https://github.com/UCSD-PL/refscript): Refinement Types for TypeScript
* [Subtypes in Perl 6](https://design.perl6.org/S12.html#Types_and_Subtypes)

## License

**refined** is licensed under the MIT license, available at http://opensource.org/licenses/MIT
and also in the [LICENSE](https://github.com/fthomas/refined/blob/master/LICENSE) file.

[bond]: https://github.com/fwbrasil/bond
[refined.hs]: http://nikita-volkov.github.io/refined
[scala.js]: http://www.scala-js.org
[search.maven]: http://search.maven.org/#search|ga|1|eu.timepit.refined
[singleton-types]: https://github.com/milessabin/shapeless/wiki/Feature-overview:-shapeless-2.0.0#singleton-typed-literals
[sip-23]: http://docs.scala-lang.org/sips/42.type.html
[typelevel]: http://typelevel.org
