```scala
scala> import eu.timepit.refined._
import eu.timepit.refined._

scala> import eu.timepit.refined.numeric._
import eu.timepit.refined.numeric._

scala> refineV[Greater[W.`5`.T]].withErrors(6)
res0: scala.util.Either[eu.timepit.refined.api.Result[eu.timepit.refined.numeric.Greater[Int(5)]],eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Greater[Int(5)]]] = Right(6)

scala> refineV[Greater[W.`5`.T]].withErrors(4)
res1: scala.util.Either[eu.timepit.refined.api.Result[eu.timepit.refined.numeric.Greater[Int(5)]],eu.timepit.refined.api.Refined[Int,eu.timepit.refined.numeric.Greater[Int(5)]]] = Left(Failed(Greater(5)))

scala> refineV[GreaterEqual[W.`5`.T]].withErrors(4)
res2: scala.util.Either[eu.timepit.refined.api.Result[eu.timepit.refined.boolean.Not[eu.timepit.refined.api.Result[eu.timepit.refined.numeric.Less[Int(5)]]]],eu.timepit.refined.api.Refined[Int,eu.timepit.refined.boolean.Not[eu.timepit.refined.numeric.Less[Int(5)]]]] = Left(Failed(Not(Passed(Less(5)))))

scala> refineV[Interval.Closed[W.`5`.T, W.`10`.T]].withErrors(11)
res3: scala.util.Either[eu.timepit.refined.api.Result[eu.timepit.refined.boolean.And[eu.timepit.refined.api.Result[eu.timepit.refined.boolean.Not[eu.timepit.refined.api.Result[eu.timepit.refined.numeric.Less[Int(5)]]]],eu.timepit.refined.api.Result[eu.timepit.refined.boolean.Not[eu.timepit.refined.api.Result[eu.timepit.refined.numeric.Greater[Int(10)]]]]]],eu.timepit.refined.api.Refined[Int,eu.timepit.refined.boolean.And[eu.timepit.refined.boolean.Not[eu.timepit.refined.numeric.Less[Int(5)]],eu.timepit.refined.boolean.Not[eu.timepit.refined.numeric.Greater[Int(10)]]]]] = Left(Failed(And(Passed(Not(Failed(Less(5)))),Failed(Not(Passed(Greater(10)))))))
```
