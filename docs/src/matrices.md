```tut:silent
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import shapeless.Witness
```

```tut
case class Dim[R <: Int, C <: Int](rows: R, columns: C)
```

```tut
class MatrixAux[R <: Int, C <: Int](val wr: Witness.Aux[R], val wc: Witness.Aux[C]) {
  def apply[A](f: (Int, Int) => A): List[List[A]] Refined Dim[wr.T, wc.T] =
    Refined.unsafeApply(List.tabulate(wr.value, wc.value)(f))
}

def matrix[A](rows: Int, columns: Int)(f: (Int, Int) => A) =
  new MatrixAux(Witness(rows), Witness(columns))(f)
```

```tut
matrix(2, 3)((r, c) => s"$r$c")
```
