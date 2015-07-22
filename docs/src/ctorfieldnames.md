This file shows some examples with the `FieldNames` and `ConstructorNames`
predicates that are implemented with [shapeless'][shapeless]
`LabelledGeneric`.

```tut:silent
import eu.timepit.refined._
import eu.timepit.refined.collection._
import eu.timepit.refined.generic._
import eu.timepit.refined.implicits._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string._
import shapeless.nat._
import shapeless.tag.@@

case class Person(firstName: String, lastName: String, age: Int)
val p = Person("John", "Doe", 32)
```

The `FieldNames` predicate takes as argument a predicate that validates a
`List[String]` which are the field names of a product type (`Person` in these
examples):
```tut
val a: Person @@ FieldNames[Forall[Size[GreaterEqual[_3]]]] = p

val b: Person @@ FieldNames[Exists[StartsWith[W.`"last"`.T]]] = p
```

These examples fail since `Person` has no field that is called "name" and it
has three fields instead of four:
```tut:nofail
val c: Person @@ FieldNames[Contains[W.`"name"`.T]] = p

val d: Person @@ FieldNames[Size[Equal[_4]]] = p
```

The `ConstructorNames` predicate validates the names of a sealed family of
classes:
```tut
val opt: Option[Unit] = Some(())

val e: Option[Unit] @@ ConstructorNames[Size[Equal[_2]]] = opt

val f: Option[Unit] @@ ConstructorNames[Contains[W.`"None"`.T]] = opt
```

Let's see what happens if we use predicates that should fail:
```tut:nofail
val g: Option[Unit] @@ ConstructorNames[Size[Equal[_3]]] = opt

val h: Option[Unit] @@ ConstructorNames[Contains[W.`"Just"`.T]] = opt
```

[shapeless]: https://github.com/milessabin/shapeless
