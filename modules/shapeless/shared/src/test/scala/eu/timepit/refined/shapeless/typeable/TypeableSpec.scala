package eu.timepit.refined.shapeless.typeable

import eu.timepit.refined.W
import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.string.MatchesRegex
import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.Typeable

class TypeableSpec extends Properties("shapeless") {

  property("Typeable cast success") = secure {
    val value: PosInt = PosInt.unsafeFrom(5)
    typeableCast[PosInt](5) ?= Some(value)
  }

  property("Typeable cast fail") = secure {
    typeableCast[PosInt](0) ?= None
  }

  property("Typeable describe") = secure {
    typeableDescribe[PosInt] ?= "Refined[Int, Greater[_0]]"
  }

  property("Typeable cast success string regex") = secure {
    type Word = String Refined MatchesRegex[W.`"[a-zA-Z]*"`.T]
    object Word extends RefinedTypeOps[Word, String]
    val value: Word = Word.unsafeFrom("AlloweD")
    typeableCast[Word]("AlloweD") ?= Some(value)
  }

  property("Typeable cast fail string regex") = secure {
    type Word = String Refined MatchesRegex[W.`"[a-zA-Z]*"`.T]
    typeableCast[Word]("Not Allowed") ?= None
  }

  property("Typeable string regex describe") = secure {
    type Word = String Refined MatchesRegex[W.`"[a-zA-Z]*"`.T]
    typeableDescribe[Word] ?= """Refined[String, MatchesRegex[String([a-zA-Z]*)]]"""
  }

  private def typeableDescribe[T](implicit T: Typeable[T]): String = T.describe

  private def typeableCast[T](value: Any)(implicit T: Typeable[T]): Option[T] = T.cast(value)

}
