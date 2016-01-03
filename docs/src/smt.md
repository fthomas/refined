# SMT-based refinement types

The `refined-smt` add-on provides a `Smt` predicate that uses a
[SMT solver][SMT]


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

val a: EvenInt = 2
```

```tut:fail
val b: EvenInt = 3
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

[SMT]: https://en.wikipedia.org/wiki/Satisfiability_modulo_theories
[SMT-LIB]: http://smtlib.cs.uiowa.edu/language.shtml
[Z3]: https://github.com/Z3Prover/z3
