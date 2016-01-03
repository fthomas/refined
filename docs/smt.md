# refined-smt


satisfiability modulo theories (SMT)

https://en.wikipedia.org/wiki/Satisfiability_modulo_theories

SMT solver

[Z3 theorem prover](https://github.com/Z3Prover/z3)

SMT-LIB language http://smtlib.cs.uiowa.edu/language.shtml


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

scala> val a: EvenInt = 2
a: EvenInt = Refined(2)
```

```scala
scala> val b: EvenInt = 3
<console>:21: error: Predicate failed: (= (mod x 2) 0).
       val b: EvenInt = 3
                        ^
```

```scala
scala> type Percentage = Int Refined Smt[W.`"(and (>= x 0) (<= x 100))"`.T]
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
<console>:24: error: invalid inference: eu.timepit.refined.smt.Smt[String("(>= x 0)")] ==> eu.timepit.refined.smt.Smt[String("(and (>= x 0) (<= x 100))")]
       val p2: Percentage = n
                            ^
```
