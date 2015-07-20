Examples from [accord][accord]:

```tut:silent
import eu.timepit.refined._

case class Person(firstName: String, lastName: String)

implicit val personPredicate = new Predicate[Nothing, Person] {
  def isValid(p: Person) = p.firstName.nonEmpty && p.lastName.nonEmpty
  def show(p: Person) = s"first and last name of $p must not be empty"
}
```

```tut
val invalidPerson = Person("", "No First Name")
refineT[Nothing](invalidPerson)
```

[accord]: https://github.com/wix/accord
