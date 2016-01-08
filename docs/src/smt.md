# SMT-based refinement types

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




 But using refinements that are not already covered
by the predefined predicates either requires a clever combination of
the existing predicates or introducing a new predicate and associated
type class instances.


composable, "stringly typed" programming

power of 2
is that even

The refined-smt add-on provides a `Smt` predicate that uses a
[SMT solver][SMT] to validate refinements and
 
 
 for subtyping queries of
refined types.


SMT-LIB language


SMT solver

[Z3 theorem prover][Z3]




```tut
import scala.sys.process._
"z3 -version".!!.trim
```

```tut:silent
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.smt.Smt
```

```tut
type EvenInt = Int Refined Smt[W.`"(= (mod x 2) 0)"`.T]

type Pow2 = Int Refined Smt[W.`"(exists ((n Int)) (= x (^ 2 n)))"`.T]

//val a: EvenInt = 2

//val c: Pow2 = 32
```

```tut:fail
val b: EvenInt = 3

//val c: Pow2 = 34
```

```tut
type Percentage = Int Refined Smt[W.`"(and (>= x 0) (<= x 100))"`.T]
type Natural = Int Refined Smt[W.`"(>= x 0)"`.T]

val p: Percentage = 63

val n: Natural = p
```

```tut:fail
val p2: Percentage = n
```

[dependent-type]: https://en.wikipedia.org/wiki/Dependent_type
[provided-predicates]: https://github.com/fthomas/refined#provided-predicates
[SMT]: https://en.wikipedia.org/wiki/Satisfiability_modulo_theories
[SMT-LIB]: http://smtlib.cs.uiowa.edu/language.shtml
[Z3]: https://github.com/Z3Prover/z3
