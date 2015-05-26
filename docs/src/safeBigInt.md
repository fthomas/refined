```tut
import shapeless.tag.@@
import eu.timepit.refined.collection.Forall
import eu.timepit.refined.char.Digit
import eu.timepit.refined._

def safeBigInt(x: String @@ Forall[Digit]): BigInt = BigInt(x)
```

```tut
safeBigInt(refineLit("9000"))
```

```tut:nofail
safeBigInt(refineLit("90.00"))
```
