```scala
import eu.timepit.refined.api.Refined
import shapeless.Witness
```

```scala
scala> case class Dim[R <: Int, C <: Int](rows: R, columns: C)
defined class Dim
```

```scala
scala> type Matrix[R <: Int, C <: Int] = List[List[Int]] Refined Dim[R, C]
defined type alias Matrix

scala> class MatrixAux[R <: Int, C <: Int](val wr: Witness.Aux[R], val wc: Witness.Aux[C]) {
     |   def apply(f: (Int, Int) => Int): Matrix[wr.T, wc.T] =
     |     Refined.unsafeApply(List.tabulate(wr.value, wc.value)(f))
     | }
defined class MatrixAux

scala> def matrix(rows: Int, columns: Int)(f: (Int, Int) => Int) =
     |   new MatrixAux(Witness(rows), Witness(columns))(f)
matrix: (rows: Int, columns: Int)(f: (Int, Int) => Int)eu.timepit.refined.api.Refined[List[List[Int]],Dim[rows.type,columns.type]]
```

```scala
scala> val m1 = matrix(2, 3)((r, c) => r * 10 + c)
m1: eu.timepit.refined.api.Refined[List[List[Int]],Dim[Int(2),Int(3)]] = List(List(0, 1, 2), List(10, 11, 12))
```

```scala
scala> def transpose[R <: Int, C <: Int](m: Matrix[R, C]): Matrix[C, R] =
     |   Refined.unsafeApply(m.value.transpose)
transpose: [R <: Int, C <: Int](m: Matrix[R,C])Matrix[C,R]

scala> transpose(m1)
res0: Matrix[Int(3),Int(2)] = List(List(0, 10), List(1, 11), List(2, 12))
```
