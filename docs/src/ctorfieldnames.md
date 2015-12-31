This file shows some examples with the `FieldNames` and `ConstructorNames`
predicates that are implemented with [shapeless'][shapeless]
`LabelledGeneric`.

```tut:silent
import eu.timepit.refined.W
import eu.timepit.refined.auto._
import eu.timepit.refined.collection._
import eu.timepit.refined.generic._
import eu.timepit.refined.numeric.GreaterEqual
import eu.timepit.refined.string.StartsWith
import shapeless.tag.@@

case class Person(firstName: String, lastName: String, age: Int)
val p = Person("John", "Doe", 32)
```

The `FieldNames` predicate takes as argument a predicate that validates a
`List[String]` which are the field names of a product type (`Person` in these
examples):
```tut:nofail
p: Person @@ FieldNames[Forall[Size[GreaterEqual[W.`3`.T]]]]

p: Person @@ FieldNames[Exists[StartsWith[W.`"last"`.T]]]
```

These examples fail since `Person` has no field that is called "name" and it
has three fields instead of four:
```tut:nofail
p: Person @@ FieldNames[Contains[W.`"name"`.T]]

p: Person @@ FieldNames[Size[Equal[W.`4`.T]]]
```

The `ConstructorNames` predicate validates the names of a sealed family of
classes:
```tut:nofail
val o: Option[Unit] = Some(())

o: Option[Unit] @@ ConstructorNames[Size[Equal[W.`2`.T]]]

o: Option[Unit] @@ ConstructorNames[Contains[W.`"None"`.T]]
```

Let's see what happens if we use predicates that should fail:
```tut:nofail
o: Option[Unit] @@ ConstructorNames[Size[Equal[W.`3`.T]]]

o: Option[Unit] @@ ConstructorNames[Contains[W.`"Just"`.T]]
```

[shapeless]: https://github.com/milessabin/shapeless
