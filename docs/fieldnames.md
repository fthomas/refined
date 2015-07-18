```scala
scala> import eu.timepit.refined._
import eu.timepit.refined._

scala> import eu.timepit.refined.collection._
import eu.timepit.refined.collection._

scala> import eu.timepit.refined.generic._
import eu.timepit.refined.generic._

scala> import eu.timepit.refined.implicits._
import eu.timepit.refined.implicits._

scala> import eu.timepit.refined.numeric._
import eu.timepit.refined.numeric._

scala> import eu.timepit.refined.string._
import eu.timepit.refined.string._

scala> import shapeless.nat._
import shapeless.nat._

scala> import shapeless.tag.@@
import shapeless.tag.$at$at

scala> case class Person(firstName: String, lastName: String, age: Int)
defined class Person

scala> // all fields of `Person` are longer than three chars:
     | val x: Person @@ FieldNames[Forall[Size[GreaterEqual[_3]]]] = Person("John", "Doe", 32)
x: shapeless.tag.@@[Person,eu.timepit.refined.generic.FieldNames[eu.timepit.refined.collection.Forall[eu.timepit.refined.collection.Size[eu.timepit.refined.numeric.GreaterEqual[shapeless.nat._3]]]]] = Person(John,Doe,32)

scala> // `Person` has a field that starts with "last":
     | val y: Person @@ FieldNames[Exists[StartsWith[W.`"last"`.T]]] = Person("John", "Doe", 32)
y: shapeless.tag.@@[Person,eu.timepit.refined.generic.FieldNames[eu.timepit.refined.collection.Exists[eu.timepit.refined.string.StartsWith[String("last")]]]] = Person(John,Doe,32)
```

```scala
scala> // there is no field in `Person` which is called "name":
     | val a: Person @@ FieldNames[Contains[W.`"name"`.T]] = Person("John", "Doe", 32)
<console>:36: error: Predicate failed: !(!(firstName == name) && !(lastName == name) && !(age == name)).
       val a: Person @@ FieldNames[Contains[W.`"name"`.T]] = Person("John", "Doe", 32)
                                                                   ^

scala> // `Person` has not four fields:
     | val z: Person @@ FieldNames[Size[Equal[_4]]] = Person("", "", 0)
<console>:36: error: Predicate failed: (3 == 4).
       val z: Person @@ FieldNames[Size[Equal[_4]]] = Person("", "", 0)
                                                            ^
```
