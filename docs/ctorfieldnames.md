This file shows some examples with the `FieldNames` and `ConstructorNames`
predicates that are implemented with [shapeless'][shapeless]
`LabelledGeneric`.

```scala
import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.collection._
import eu.timepit.refined.generic._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string._
import shapeless.tag.@@

case class Person(firstName: String, lastName: String, age: Int)
val p = Person("John", "Doe", 32)
```

The `FieldNames` predicate takes as argument a predicate that validates a
`List[String]` which are the field names of a product type (`Person` in these
examples):
```scala
scala> p: Person @@ FieldNames[Forall[Size[GreaterEqual[W.`3`.T]]]]
res1: shapeless.tag.@@[Person,eu.timepit.refined.generic.FieldNames[eu.timepit.refined.collection.Forall[eu.timepit.refined.collection.Size[eu.timepit.refined.numeric.GreaterEqual[Int(3)]]]]] = Person(John,Doe,32)

scala> p: Person @@ FieldNames[Exists[StartsWith[W.`"last"`.T]]]
res2: shapeless.tag.@@[Person,eu.timepit.refined.generic.FieldNames[eu.timepit.refined.collection.Exists[eu.timepit.refined.string.StartsWith[String("last")]]]] = Person(John,Doe,32)
```

These examples fail since `Person` has no field that is called "name" and it
has three fields instead of four:
```scala
scala> p: Person @@ FieldNames[Contains[W.`"name"`.T]]
<console>:34: error: Predicate failed: !(!(firstName == name) && !(lastName == name) && !(age == name)).
       p: Person @@ FieldNames[Contains[W.`"name"`.T]]
       ^

scala> p: Person @@ FieldNames[Size[Equal[W.`4`.T]]]
<console>:34: error: Predicate failed: (3 == 4).
       p: Person @@ FieldNames[Size[Equal[W.`4`.T]]]
       ^
```

The `ConstructorNames` predicate validates the names of a sealed family of
classes:
```scala
scala> val o: Option[Unit] = Some(())
o: Option[Unit] = Some(())

scala> o: Option[Unit] @@ ConstructorNames[Size[Equal[W.`2`.T]]]
res5: shapeless.tag.@@[Option[Unit],eu.timepit.refined.generic.ConstructorNames[eu.timepit.refined.collection.Size[eu.timepit.refined.generic.Equal[Int(2)]]]] = Some(())

scala> o: Option[Unit] @@ ConstructorNames[Contains[W.`"None"`.T]]
res6: shapeless.tag.@@[Option[Unit],eu.timepit.refined.generic.ConstructorNames[eu.timepit.refined.collection.Contains[String("None")]]] = Some(())
```

Let's see what happens if we use predicates that should fail:
```scala
scala> o: Option[Unit] @@ ConstructorNames[Size[Equal[W.`3`.T]]]
<console>:32: error: Predicate failed: (2 == 3).
       o: Option[Unit] @@ ConstructorNames[Size[Equal[W.`3`.T]]]
       ^

scala> o: Option[Unit] @@ ConstructorNames[Contains[W.`"Just"`.T]]
<console>:32: error: Predicate failed: !(!(None == Just) && !(Some == Just)).
       o: Option[Unit] @@ ConstructorNames[Contains[W.`"Just"`.T]]
       ^
```

[shapeless]: https://github.com/milessabin/shapeless
