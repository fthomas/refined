Examples from [accord][accord]:

```scala
scala> import eu.timepit.refined._
import eu.timepit.refined._

scala> case class Person(firstName: String, lastName: String)
defined class Person

scala> implicit val personPredicate = new Predicate[Nothing, Person] {
     |   def isValid(p: Person) = p.firstName.nonEmpty && p.lastName.nonEmpty
     |   def show(p: Person) = s"first and last name of $p must not be empty"
     | }
personPredicate: eu.timepit.refined.Predicate[Nothing,Person] = $anon$1@1d539b46

scala> val invalidPerson = Person("", "No First Name")
invalidPerson: Person = Person(,No First Name)

scala> refine[Nothing](invalidPerson)
res0: Either[String,shapeless.tag.@@[Person,Nothing]] = Left(Predicate failed: first and last name of Person(,No First Name) must not be empty.)
```

[accord]: https://github.com/wix/accord
