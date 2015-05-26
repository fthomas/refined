```scala
scala> import shapeless.tag.@@
import shapeless.tag.$at$at

scala> import eu.timepit.refined.collection.Forall
import eu.timepit.refined.collection.Forall

scala> import eu.timepit.refined.char.Digit
import eu.timepit.refined.char.Digit

scala> import eu.timepit.refined._
import eu.timepit.refined._

scala> def safeBigInt(x: String @@ Forall[Digit]): BigInt = BigInt(x)
safeBigInt: (x: shapeless.tag.@@[String,eu.timepit.refined.collection.Forall[eu.timepit.refined.char.Digit]])BigInt
```

```scala
scala> safeBigInt(refineLit("9000"))
res0: BigInt = 9000
```

```scala
scala> safeBigInt(refineLit("90.00"))
<console>:16: error: could not find implicit value for parameter p: eu.timepit.refined.Predicate[P,String]
              safeBigInt(refineLit("90.00"))
                                  ^
```
