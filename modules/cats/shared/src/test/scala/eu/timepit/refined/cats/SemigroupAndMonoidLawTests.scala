package eu.timepit.refined.cats

import cats.implicits._
import cats.kernel.{Monoid, Semigroup}
import cats.kernel.laws.discipline.{MonoidTests, SemigroupTests, SerializableTests}
import eu.timepit.refined.scalacheck.numeric._
import eu.timepit.refined.types.numeric._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import org.typelevel.discipline.scalatest.FunSuiteDiscipline

class SemigroupAndMonoidLawTests extends AnyFunSuite with FunSuiteDiscipline with Checkers {
  // Positive semigroups
  checkAll("Semigroup[PosByte]", SemigroupTests[PosByte].semigroup)
  checkAll("Semigroup[PosByte]", SerializableTests.serializable(Semigroup[PosByte]))
  checkAll("Semigroup[PosShort]", SemigroupTests[PosShort].semigroup)
  checkAll("Semigroup[PosShort]", SerializableTests.serializable(Semigroup[PosShort]))
  checkAll("Semigroup[PosInt]", SemigroupTests[PosInt].semigroup)
  checkAll("Semigroup[PosInt]", SerializableTests.serializable(Semigroup[PosInt]))
  checkAll("Semigroup[PosLong]", SemigroupTests[PosLong].semigroup)
  checkAll("Semigroup[PosLong]", SerializableTests.serializable(Semigroup[PosLong]))
  // checkAll("Semigroup[PosFloat]", SemigroupTests[PosFloat].semigroup) // approximately associative
  checkAll("Semigroup[PosFloat]", SerializableTests.serializable(Semigroup[PosFloat]))
  // checkAll("Semigroup[PosDouble]", SemigroupTests[PosDouble].semigroup) // approximately associative
  checkAll("Semigroup[PosDouble]", SerializableTests.serializable(Semigroup[PosDouble]))

  // Negative semigroups
  checkAll("Semigroup[NegByte]", SemigroupTests[NegByte].semigroup)
  checkAll("Semigroup[NegByte]", SerializableTests.serializable(Semigroup[NegByte]))
  checkAll("Semigroup[NegShort]", SemigroupTests[NegShort].semigroup)
  checkAll("Semigroup[NegShort]", SerializableTests.serializable(Semigroup[NegShort]))
  checkAll("Semigroup[NegInt]", SemigroupTests[NegInt].semigroup)
  checkAll("Semigroup[NegInt]", SerializableTests.serializable(Semigroup[NegInt]))
  checkAll("Semigroup[NegLong]", SemigroupTests[NegLong].semigroup)
  checkAll("Semigroup[NegLong]", SerializableTests.serializable(Semigroup[NegLong]))
  // checkAll("Semigroup[NegFloat]", SemigroupTests[NegFloat].semigroup) // approximately associative
  checkAll("Semigroup[NegFloat]", SerializableTests.serializable(Semigroup[NegFloat]))
  // checkAll("Semigroup[NegDouble]", SemigroupTests[NegDouble].semigroup) // approximately associative
  checkAll("Semigroup[NegDouble]", SerializableTests.serializable(Semigroup[NegDouble]))

  // NonNegative monoids
  checkAll("Monoid[NonNegByte]", MonoidTests[NonNegByte].monoid)
  checkAll("Monoid[NonNegByte]", SerializableTests.serializable(Monoid[NonNegByte]))
  checkAll("Monoid[NonNegShort]", MonoidTests[NonNegShort].monoid)
  checkAll("Monoid[NonNegShort]", SerializableTests.serializable(Monoid[NonNegShort]))
  checkAll("Monoid[NonNegInt]", MonoidTests[NonNegInt].monoid)
  checkAll("Monoid[NonNegInt]", SerializableTests.serializable(Monoid[NonNegInt]))
  checkAll("Monoid[NonNegLong]", MonoidTests[NonNegLong].monoid)
  checkAll("Monoid[NonNegLong]", SerializableTests.serializable(Monoid[NonNegLong]))
  // checkAll("Monoid[NonNegFloat]", MonoidTests[NonNegFloat].monoid) // approximately associative
  checkAll("Monoid[NonNegFloat]", SerializableTests.serializable(Monoid[NonNegFloat]))
  // checkAll("Monoid[NonNegDouble]", MonoidTests[NonNegDouble].monoid) // approximately associative
  checkAll("Monoid[NonNegDouble]", SerializableTests.serializable(Monoid[NonNegDouble]))

  // NonPositive monoids
  // checkAll("Monoid[NonPosFloat]", MonoidTests[NonPosFloat].monoid) // approximately associative
  checkAll("Monoid[NonPosFloat]", SerializableTests.serializable(Monoid[NonPosFloat]))
  // checkAll("Monoid[NonPosDouble]", MonoidTests[NonPosDouble].monoid) // approximately associative
  checkAll("Monoid[NonPosDouble]", SerializableTests.serializable(Monoid[NonPosDouble]))

}
