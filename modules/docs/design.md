# Design description

TODO

The text below was copied from the README but is outdated:

## Internals

*refined* basically consists of two parts, one for [refining types with
type-level predicates](#predicates) and the other for [converting between
different refined types](#inference-rules).

### Predicates

The refinement machinery is built of:

* Type-level predicates for refining other types, like `UpperCase`, `Positive`, or
  `LessEqual[_2]`. There are also higher order predicates for combining proper
  predicates like `And[_, _]`, `Or[_, _]`, `Not[_]`, `Forall[_]`, or `Size[_]`.

* A `Predicate` type class for validating a value of an unrefined type
  (like `Double`) against a type-level predicate (like `Positive`).

* A function `refineT` and a macro `refineMT` that take a predicate `P`
  and some value of type `T`, validate this value with a `Predicate[P, T]`
  and return the value with type `T @@ P` if validation was successful or
  an error otherwise. The return type of `refineT` is `Either[String, T @@ P]`
  while the `refineMT` returns a `T @@ P` or compilation fails. Since
  `refineMT` is a macro it only works with literal values or constant
  predicates.

### Inference rules

The type-conversions are built of:

* An `InferenceRule` type class that is indexed by two type-level predicates
  which states whether the second predicate can be logically derived from the
  first. `InferenceRule[Greater[_5], Positive]` would be an instance of a
  valid inference rule while `InferenceRule[Greater[_5], Negative]` would be
  an invalid inference rule.

* An implicit conversion defined as macro that casts a value of type `T @@ A`
  to type `T @@ B` if a valid `InferenceRule[A, B]` is in scope.

### Intro from early smt doc

**refined's** refinement types are based on type-level predicates and
type classes for checking the validity of refinements and for converting
between refined types (refinement subtyping). The library comes with a
lot of [predefined predicates][provided-predicates] of which many take
type parameters to allow for some customisation of their behavior.
These predicates mirror ordinary value-level predicates but lifted into
the type-level (`Greater[N]` for example roughly mirrors `(x: Int) =>
x > n` or `StartsWith[S]` mirrors `(x: String) => x.startsWith(s)`).
This lifting of predicates from the value to the type-level is necessary
because we can't just use value-level predicates in a position where a
type is expected (which is typically only possible in languages with
fully [dependent types][dependent-type]).

Using distinct types for predicates has the advantage that we can
easily compose predicates via type aliases and that we retain information
about the individual parts of composite predicates. That means if we
check if a value satisfies a composite predicate, we not only know if
the whole predicate passed or failed but also for each individual
predicate if it passed or failed.

Here are two examples how predicates can be composed via type aliases:
```scala
// checks if a numeric value is in the closed interval [L, H]
type Closed[L, H]
  = GreaterEqual[L] And LessEqual[H] // definition of Closed
  = Not[Less[L]] And Not[Greater[H]] // fully dealiased definition
                                     // in terms primitive predicates

// checks if some element of a collection satisfies the predicate P
type Exists[P] = Not[Forall[Not[P]]]
```
