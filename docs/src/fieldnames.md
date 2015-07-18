```tut
import eu.timepit.refined._
import eu.timepit.refined.collection._
import eu.timepit.refined.generic._
import eu.timepit.refined.implicits._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string._
import shapeless.nat._
import shapeless.tag.@@

case class Person(firstName: String, lastName: String, age: Int)

// all fields of `Person` are longer than three chars:
val x: Person @@ FieldNames[Forall[Size[GreaterEqual[_3]]]] = Person("John", "Doe", 32)

// `Person` has a field that starts with "last":
val y: Person @@ FieldNames[Exists[StartsWith[W.`"last"`.T]]] = Person("John", "Doe", 32)
```

```tut:nofail
// there is no field in `Person` which is called "name":
val a: Person @@ FieldNames[Contains[W.`"name"`.T]] = Person("John", "Doe", 32)

// `Person` has not four fields:
val z: Person @@ FieldNames[Size[Equal[_4]]] = Person("", "", 0)
```
