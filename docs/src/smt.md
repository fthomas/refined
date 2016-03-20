# SMT-based refinement types

The `refined-smt` add-on is an experimental module that provides a
`Sat` predicate which uses a [SMT solver][SMT] to check the validity
of refinements and to convert between refined types (refinement
subtyping). We currently require the [Z3 theorem prover][Z3] to be
available in the PATH but the code can be easily changed to work with
any other [SMT-LIB][SMT-LIB] compliant solver.

Let's see the `Sat` predicate in action. The following examples use
this Z3 version:
```tut
sys.process.Process("z3 -version").!!.trim
```
And require the these imports:
```tut:silent
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.smt.Sat
```

The `Sat` predicate takes a `String` singleton type as parameter whose
value needs to be a SMT-LIB expression that returns a boolean. Let's
define a type for all even integers:
```tut
type EvenInt = Int Refined Sat[W.`"(= (mod x 2) 0)"`.T]
```
And try it out:
```tut
val a: EvenInt = 4
```
In this example Z3 was called at compile-time to check whether 4 is even.
If we try to assign an odd integer to an `EvenInt`, we get a compile error:
```tut:fail
val b: EvenInt = 5
```
This usage of `Sat` is similar to the already existing [`Eval`][Eval]
predicate which also allows to define arbitrary predicates but written in
Scala. It is even more limited than `Eval` because it can only make use
of functions and data types defined by the SMT solver.

Let's define more refined types to demonstrate refinement subtyping
using Z3:
```tut
type Natural       = Int Refined Sat[W.`"(>= x 0)"`.T]
type SmallPositive = Int Refined Sat[W.`"(and (>= x 1) (<= x 99))"`.T]

val s: SmallPositive = 63
val n: Natural = s
```
In the last line Z3 deduced at compile-time that the refinement
`"(and (>= x 1) (<= x 99))"` always implies the refinement `"(>= x 0)"`.
If we try to assign a `Natural` to a `SmallPositive` we get a compile error:
```tut:fail
val s2: SmallPositive = n
```

[Eval]: https://github.com/fthomas/refined/pull/82
[SMT]: https://en.wikipedia.org/wiki/Satisfiability_modulo_theories
[SMT-LIB]: http://smtlib.cs.uiowa.edu/language.shtml
[Z3]: https://github.com/Z3Prover/z3
