package eu.timepit.refined

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

  property("SubtypeOf.isValid") = secure {
    Predicate[SubtypeOf[AnyVal], Int].isValid(0)
  }

  property("SubtypeOf.notValid") = secure {
    illTyped("Predicate[SubtypeOf[Int], AnyVal]", ".*could not find implicit value.*")
    true
  }

  property("SupertypeOf.isValid") = secure {
    Predicate[SupertypeOf[List[Int]], Seq[Int]].isValid(Seq(0))
  }

  property("SupertypeOf.notValid") = secure {
    illTyped("Predicate[SupertypeOf[Seq[Int]], List[Int]]", ".*could not find implicit value.*")
    true
  }
}
