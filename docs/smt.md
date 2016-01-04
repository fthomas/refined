# SMT-based refinement types

**refined's** refinement types are based on type-level predicates and
associated type class instances for checking the validity of refinements
and for conversion between refined types (refinement subtyping). The
library comes with a lot of [predefined predicates][provided-predicates]
and many of those take type parameters to allow for some customisation
of their behavior. But using refinements that are not already covered
by the predefined predicates either requires a clever combination of
the existing predicates or introducing a new predicate and associated
type class instances.




power of 2
is that even

The refined-smt add-on provides a `Smt` predicate that uses a
[SMT solver][SMT] to validate refinements and
 
 
 for subtyping queries of
refined types.


SMT-LIB language


SMT solver

[Z3 theorem prover][Z3]




```scala
scala> import scala.sys.process._
import scala.sys.process._

scala> "z3 -version".!!.trim
res0: String = Z3 version 4.4.0
```

```scala
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.smt.Smt
```

```scala
scala> type EvenInt = Int Refined Smt[W.`"(= (mod x 2) 0)"`.T]
defined type alias EvenInt

scala> type Pow2 = Int Refined Smt[W.`"(exists ((n Int)) (= x (^ 2 n)))"`.T]
defined type alias Pow2

scala> //val a: EvenInt = 2
     | 
     | //val c: Pow2 = 32
```

```scala
     | val b: EvenInt = 3
<console>:24: error: Predicate failed: (= (mod x 2) 0).
       val b: EvenInt = 3
                        ^

scala> //val c: Pow2 = 34
```

```scala
     | type Percentage = Int Refined Smt[W.`"(and (>= x 0) (<= x 100))"`.T]
defined type alias Percentage

scala> type Natural = Int Refined Smt[W.`"(>= x 0)"`.T]
defined type alias Natural

scala> val p: Percentage = 63
p: Percentage = Refined(63)

scala> val n: Natural = p
n: Natural = Refined(63)
```

```scala
scala> val p2: Percentage = n
<console>:24: error: invalid inference:
  eu.timepit.refined.smt.Smt[String("(>= x 0)")] ==>
  eu.timepit.refined.smt.Smt[String("(and (>= x 0) (<= x 100))")]
       val p2: Percentage = n
                            ^
```

[provided-predicates]: https://github.com/fthomas/refined#provided-predicates
[SMT]: https://en.wikipedia.org/wiki/Satisfiability_modulo_theories
[SMT-LIB]: http://smtlib.cs.uiowa.edu/language.shtml
[Z3]: https://github.com/Z3Prover/z3
