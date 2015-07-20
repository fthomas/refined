Examples from [accord][accord]:

```scala
import eu.timepit.refined._

case class Person(firstName: String, lastName: String)

implicit val personPredicate = new Predicate[Nothing, Person] {
  def isValid(p: Person) = p.firstName.nonEmpty && p.lastName.nonEmpty
  def show(p: Person) = s"first and last name of $p must not be empty"
}
```

```scala
scala> val invalidPerson = Person("", "No First Name")
invalidPerson: Person = Person(,No First Name)

scala> refineT[Nothing](invalidPerson)
res2: Either[String,shapeless.tag.@@[Person,Nothing]] = Left(Predicate failed: first and last name of Person(,No First Name) must not be empty.)
```

[accord]: https://github.com/wix/accord
