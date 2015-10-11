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
