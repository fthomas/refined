This file shows some examples with the `FieldNames` and `ConstructorNames`
predicates that are implemented with [shapeless'][shapeless]
`LabelledGeneric`.

```scala
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
```scala
scala> val a: Person @@ FieldNames[Forall[Size[GreaterEqual[_3]]]] = p
a: shapeless.tag.@@[Person,eu.timepit.refined.generic.FieldNames[eu.timepit.refined.collection.Forall[eu.timepit.refined.collection.Size[eu.timepit.refined.numeric.GreaterEqual[shapeless.nat._3]]]]] = Person(John,Doe,32)

scala> val b: Person @@ FieldNames[Exists[StartsWith[W.`"last"`.T]]] = p
b: shapeless.tag.@@[Person,eu.timepit.refined.generic.FieldNames[eu.timepit.refined.collection.Exists[eu.timepit.refined.string.StartsWith[String("last")]]]] = Person(John,Doe,32)
```

These examples fail since `Person` has no field that is called "name" and it
has three fields instead of four:
```scala
scala> val c: Person @@ FieldNames[Contains[W.`"name"`.T]] = p
<console>:36: error: Predicate failed: !(!(firstName == name) && !(lastName == name) && !(age == name)).
       val c: Person @@ FieldNames[Contains[W.`"name"`.T]] = p
                                                             ^

scala> val d: Person @@ FieldNames[Size[Equal[_4]]] = p
<console>:36: error: Predicate failed: (3 == 4).
       val d: Person @@ FieldNames[Size[Equal[_4]]] = p
                                                      ^
```

The `ConstructorNames` predicate validates the names of a sealed family of
classes:
```scala
scala> val opt: Option[Unit] = Some(())
opt: Option[Unit] = Some(())

scala> val e: Option[Unit] @@ ConstructorNames[Size[Equal[_2]]] = opt
e: shapeless.tag.@@[Option[Unit],eu.timepit.refined.generic.ConstructorNames[eu.timepit.refined.collection.Size[eu.timepit.refined.generic.Equal[shapeless.nat._2]]]] = Some(())

scala> val f: Option[Unit] @@ ConstructorNames[Contains[W.`"None"`.T]] = opt
f: shapeless.tag.@@[Option[Unit],eu.timepit.refined.generic.ConstructorNames[eu.timepit.refined.collection.Contains[String("None")]]] = Some(())
```

Let's see what happens if we use predicates that should fail:
```scala
scala> val g: Option[Unit] @@ ConstructorNames[Size[Equal[_3]]] = opt
<console>:34: error: Predicate failed: (2 == 3).
       val g: Option[Unit] @@ ConstructorNames[Size[Equal[_3]]] = opt
                                                                  ^

scala> val h: Option[Unit] @@ ConstructorNames[Contains[W.`"Just"`.T]] = opt
<console>:34: error: Predicate failed: !(!(None == Just) && !(Some == Just)).
       val h: Option[Unit] @@ ConstructorNames[Contains[W.`"Just"`.T]] = opt
                                                                         ^
```

[shapeless]: https://github.com/milessabin/shapeless
