```tut:silent
import eu.timepit.refined.api.Refined
import shapeless.Witness
```

```tut
case class Dim[R <: Int, C <: Int](rows: R, columns: C)
```

```tut
type Matrix[R <: Int, C <: Int] = List[List[Int]] Refined Dim[R, C]

class MatrixAux[R <: Int, C <: Int](val wr: Witness.Aux[R], val wc: Witness.Aux[C]) {
  def apply(f: (Int, Int) => Int): Matrix[wr.T, wc.T] =
    Refined.unsafeApply(List.tabulate(wr.value, wc.value)(f))
}

def matrix(rows: Int, columns: Int)(f: (Int, Int) => Int) =
  new MatrixAux(Witness(rows), Witness(columns))(f)
```

```tut
val m1 = matrix(2, 3)((r, c) => r * 10 + c)
```

```tut
def transpose[R <: Int, C <: Int](m: Matrix[R, C]): Matrix[C, R] =
  Refined.unsafeApply(m.value.transpose)

transpose(m1)
```
