# SMT-based refinement types

The `refined-smt` add-on is an experimental module that provides a
`Sat` predicate which uses a [SMT solver][SMT] to check the validity
of refinements and to convert between refined types (refinement
subtyping). We currently require the [Z3 theorem prover][Z3] to be
available in the PATH but the code can be easily changed to work with
any other [SMT-LIB][SMT-LIB] compliant solver.

Let's see the `Sat` predicate in action. The following examples use
this Z3 version:
```scala
scala> sys.process.Process("z3 -version").!!.trim
res0: String = Z3 version 4.4.1
```
And require the these imports:
```scala
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.smt.Sat
```

The `Sat` predicate takes a `String` singleton type as parameter whose
value needs to be a SMT-LIB expression that returns a boolean. Let's
define a type for all even integers:
```scala
scala> type EvenInt = Int Refined Sat[W.`"(= (mod x 2) 0)"`.T]
defined type alias EvenInt
```
And try it out:
```scala
scala> val a: EvenInt = 4
a: EvenInt = Refined(4)
```
In this example Z3 was called at compile-time to check whether 4 is even.
If we try to assign an odd integer to an `EvenInt`, we get a compile error:
```scala
scala> val b: EvenInt = 5
<console>:19: error: Predicate failed: (= (mod x 2) 0) where x = 5.
       val b: EvenInt = 5
                        ^
```
This usage of `Sat` is similar to the already existing [`Eval`][Eval]
predicate which also allows to define arbitrary predicates but written in
Scala. It is even more limited than `Eval` because it can only make use
of functions and data types defined by the SMT solver.

Let's define more refined types to demonstrate refinement subtyping
using Z3:
```scala
scala> type Natural       = Int Refined Sat[W.`"(>= x 0)"`.T]
defined type alias Natural

scala> type SmallPositive = Int Refined Sat[W.`"(and (>= x 1) (<= x 99))"`.T]
defined type alias SmallPositive

scala> val s: SmallPositive = 63
s: SmallPositive = Refined(63)

scala> val n: Natural = s
n: Natural = Refined(63)
```
In the last line Z3 deduced at compile-time that the refinement
`"(and (>= x 1) (<= x 99))"` always implies the refinement `"(>= x 0)"`.
If we try to assign a `Natural` to a `SmallPositive` we get a compile error:
```scala
scala> val s2: SmallPositive = n
<console>:22: error: type mismatch (invalid inference):
 eu.timepit.refined.smt.Sat[String("(>= x 0)")] does not imply
 eu.timepit.refined.smt.Sat[String("(and (>= x 1) (<= x 99))")]
       val s2: SmallPositive = n
                               ^
```

[Eval]: https://github.com/fthomas/refined/pull/82
[SMT]: https://en.wikipedia.org/wiki/Satisfiability_modulo_theories
[SMT-LIB]: http://smtlib.cs.uiowa.edu/language.shtml
[Z3]: https://github.com/Z3Prover/z3
