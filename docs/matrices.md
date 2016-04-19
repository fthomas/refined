```scala
import eu.timepit.refined.api.Refined
import shapeless.Witness
```

```scala
scala> case class Dim[R <: Int, C <: Int](rows: R, columns: C)
defined class Dim
```

```scala
scala> class MatrixAux[R <: Int, C <: Int](val wr: Witness.Aux[R], val wc: Witness.Aux[C]) {
     |   def apply[A](f: (Int, Int) => A): List[List[A]] Refined Dim[wr.T, wc.T] =
     |     Refined.unsafeApply(List.tabulate(wr.value, wc.value)(f))
     | }
defined class MatrixAux

scala> def matrix[A](rows: Int, columns: Int)(f: (Int, Int) => A) =
     |   new MatrixAux(Witness(rows), Witness(columns))(f)
matrix: [A](rows: Int, columns: Int)(f: (Int, Int) => A)eu.timepit.refined.api.Refined[List[List[A]],Dim[rows.type,columns.type]]
```

```scala
scala> matrix(2, 3)((r, c) => s"$r$c")
res0: eu.timepit.refined.api.Refined[List[List[String]],Dim[Int(2),Int(3)]] = List(List(00, 01, 02), List(10, 11, 12))
```
