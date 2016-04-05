This file shows some examples with the `FieldNames` and `ConstructorNames`
predicates that are implemented with [shapeless'][shapeless]
`LabelledGeneric`.

```scala
import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.collection._
import eu.timepit.refined.generic._
import eu.timepit.refined.numeric.GreaterEqual
import eu.timepit.refined.string.StartsWith

case class Person(firstName: String, lastName: String, age: Int)
val p = Person("John", "Doe", 32)
```

The `FieldNames` predicate takes as argument a predicate that validates a
`List[String]` which are the field names of a product type (`Person` in these
examples):
```scala
scala> refineV[FieldNames[Forall[Size[GreaterEqual[W.`3`.T]]]]](p)
res1: Either[String,eu.timepit.refined.api.Refined[Person,eu.timepit.refined.generic.FieldNames[eu.timepit.refined.collection.Forall[eu.timepit.refined.collection.Size[eu.timepit.refined.numeric.GreaterEqual[Int(3)]]]]]] = Right(Person(John,Doe,32))

scala> refineV[FieldNames[Exists[StartsWith[W.`"last"`.T]]]](p)
res2: Either[String,eu.timepit.refined.api.Refined[Person,eu.timepit.refined.generic.FieldNames[eu.timepit.refined.collection.Exists[eu.timepit.refined.string.StartsWith[String("last")]]]]] = Right(Person(John,Doe,32))
```

These examples fail since `Person` has no field that is called "name" and it
has three fields instead of four:
```scala
scala> refineV[FieldNames[Contains[W.`"name"`.T]]](p)
res3: Either[String,eu.timepit.refined.api.Refined[Person,eu.timepit.refined.generic.FieldNames[eu.timepit.refined.collection.Contains[String("name")]]]] = Left(Predicate failed: !(!(firstName == name) && !(lastName == name) && !(age == name)).)

scala> refineV[FieldNames[Size[Equal[W.`4`.T]]]](p)
res4: Either[String,eu.timepit.refined.api.Refined[Person,eu.timepit.refined.generic.FieldNames[eu.timepit.refined.collection.Size[eu.timepit.refined.generic.Equal[Int(4)]]]]] = Left(Predicate failed: (3 == 4).)
```

The `ConstructorNames` predicate validates the names of a sealed family of
classes:
```scala
scala> val o: Option[Unit] = Some(())
o: Option[Unit] = Some(())

scala> refineV[ConstructorNames[Size[Equal[W.`2`.T]]]](o)
res5: Either[String,eu.timepit.refined.api.Refined[Option[Unit],eu.timepit.refined.generic.ConstructorNames[eu.timepit.refined.collection.Size[eu.timepit.refined.generic.Equal[Int(2)]]]]] = Right(Some(()))

scala> refineV[ConstructorNames[Contains[W.`"None"`.T]]](o)
res6: Either[String,eu.timepit.refined.api.Refined[Option[Unit],eu.timepit.refined.generic.ConstructorNames[eu.timepit.refined.collection.Contains[String("None")]]]] = Right(Some(()))
```

Let's see what happens if we use predicates that should fail:
```scala
scala> refineV[ConstructorNames[Size[Equal[W.`3`.T]]]](o)
res7: Either[String,eu.timepit.refined.api.Refined[Option[Unit],eu.timepit.refined.generic.ConstructorNames[eu.timepit.refined.collection.Size[eu.timepit.refined.generic.Equal[Int(3)]]]]] = Left(Predicate failed: (2 == 3).)

scala> refineV[ConstructorNames[Contains[W.`"Just"`.T]]](o)
res8: Either[String,eu.timepit.refined.api.Refined[Option[Unit],eu.timepit.refined.generic.ConstructorNames[eu.timepit.refined.collection.Contains[String("Just")]]]] = Left(Predicate failed: !(!(None == Just) && !(Some == Just)).)
```

[shapeless]: https://github.com/milessabin/shapeless
