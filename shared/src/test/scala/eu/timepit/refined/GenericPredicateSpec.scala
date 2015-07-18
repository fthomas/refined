package eu.timepit.refined

import eu.timepit.refined.collection.Contains
import eu.timepit.refined.generic._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.test.illTyped

class GenericPredicateSpec extends Properties("GenericPredicate") {

  property("Equal[_].isValid") = secure {
    Predicate[Equal[W.`1.4`.T], Double].isValid(1.4)
  }

  property("Equal[_].notValid") = secure {
    Predicate[Equal[W.`1.4`.T], Double].notValid(2.4)
  }

  property("Equal[_].show") = secure {
    Predicate[Equal[W.`1.4`.T], Double].show(0.4) ?= "(0.4 == 1.4)"
  }

  property("Equal[object.type].isValid") = secure {
    object Foo
    Predicate[Equal[Foo.type], Any].isValid(Foo)
  }

  property("Equal[Symbol].isValid") = secure {
    Predicate[Equal[W.`'foo`.T], Symbol].isValid('foo)
  }

  property("Equal[Symbol].notValid") = secure {
    Predicate[Equal[W.`'foo`.T], Symbol].notValid('bar)
  }

  property("ConstructorNames.isValid") = secure {
    Predicate[ConstructorNames[Contains[W.`"Some"`.T]], Option[Int]].isValid(Option(0))
  }

  property("ConstructorNames.notValid") = secure {
    Predicate[ConstructorNames[Contains[W.`"Just"`.T]], Option[Int]].notValid(Option(0))
  }

  property("ConstructorNames.show") = secure {
    Predicate[ConstructorNames[Contains[W.`"Just"`.T]], Option[Int]].show(Option(0)) ?=
      "!(!(None == Just) && !(Some == Just))"
  }

  property("FieldNames.isValid") = secure {
    case class A(fst: Any, snd: Any)
    Predicate[FieldNames[Contains[W.`"snd"`.T]], A].isValid(A((), ()))
  }

  property("FieldNames.notValid") = secure {
    case class A(fst: Any, snd: Any)
    Predicate[FieldNames[Contains[W.`"first"`.T]], A].notValid(A((), ()))
  }

  property("FieldNames.show") = secure {
    case class A(fst: Any, snd: Any)
    Predicate[FieldNames[Contains[W.`"third"`.T]], A].show(A((), ())) ?=
      "!(!(fst == third) && !(snd == third))"
  }

  property("Subtype.isValid") = secure {
    Predicate[Subtype[AnyVal], Int].isValid(0)
  }

  property("Subtype.notValid") = secure {
    illTyped("Predicate[Subtype[Int], AnyVal]", ".*could not find implicit value.*")
    true
  }

  property("Supertype.isValid") = secure {
    Predicate[Supertype[List[Int]], Seq[Int]].isValid(Seq(0))
  }

  property("Supertype.notValid") = secure {
    illTyped("Predicate[Supertype[Seq[Int]], List[Int]]", ".*could not find implicit value.*")
    true
  }
}
