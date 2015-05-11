package eu.timepit.refined

import eu.timepit.refined.boolean._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class BooleanSpec extends Properties("boolean") {
  type FF[Op[_, _]] = False Op False
  type FT[Op[_, _]] = False Op True
  type TF[Op[_, _]] = True Op False
  type TT[Op[_, _]] = True Op True

  property("True.isValid") = secure {
    Predicate[True, Unit].isValid(())
  }

  property("True.show") = secure {
    Predicate[True, Unit].show(()) ?= "true"
  }

  property("Not[True].isValid") = secure {
    Predicate[Not[True], Unit].notValid(())
  }

  property("Not[True].show") = secure {
    Predicate[Not[True], Unit].show(()) ?= "!true"
  }

  property("False.isValid") = secure {
    Predicate[False, Unit].notValid(())
  }

  property("False.show") = secure {
    Predicate[False, Unit].show(()) ?= "false"
  }

  property("Not[False].isValid") = secure {
    Predicate[Not[False], Unit].isValid(())
  }

  property("And.isValid") = secure {
    Predicate[FF[And], Unit].notValid(()) &&
      Predicate[FT[And], Unit].notValid(()) &&
      Predicate[TF[And], Unit].notValid(()) &&
      Predicate[TT[And], Unit].isValid(())
  }

  property("And.show") = secure {
    Predicate[TF[And], Unit].show(()) ?= "(true && false)"
  }

  property("Or.isValid") = secure {
    Predicate[FF[Or], Unit].notValid(()) &&
      Predicate[FT[Or], Unit].isValid(()) &&
      Predicate[TF[Or], Unit].isValid(()) &&
      Predicate[TT[Or], Unit].isValid(())
  }

  property("Or.show") = secure {
    Predicate[TF[Or], Unit].show(()) ?= "(true || false)"
  }
}
