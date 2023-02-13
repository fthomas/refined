package eu.timepit.refined.cats
import cats.kernel.{CommutativeMonoid, CommutativeSemigroup}
import cats.kernel.laws.discipline.{
  CommutativeMonoidTests,
  CommutativeSemigroupTests,
  SerializableTests
}
import eu.timepit.refined.scalacheck.numeric._
import eu.timepit.refined.types.numeric._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.scalacheck.Checkers
import org.typelevel.discipline.scalatest.FunSuiteDiscipline

class CommutativeSemigroupAndMonoidLawTests
    extends AnyFunSuite
    with FunSuiteDiscipline
    with Checkers {
  // Positive commutativeSemigroups
  checkAll("CommutativeSemigroup[PosByte]", CommutativeSemigroupTests[PosByte].commutativeSemigroup)
  checkAll(
    "CommutativeSemigroup[PosByte]",
    SerializableTests.serializable(CommutativeSemigroup[PosByte])
  )
  checkAll(
    "CommutativeSemigroup[PosShort]",
    CommutativeSemigroupTests[PosShort].commutativeSemigroup
  )
  checkAll(
    "CommutativeSemigroup[PosShort]",
    SerializableTests.serializable(CommutativeSemigroup[PosShort])
  )
  checkAll("CommutativeSemigroup[PosInt]", CommutativeSemigroupTests[PosInt].commutativeSemigroup)
  checkAll(
    "CommutativeSemigroup[PosInt]",
    SerializableTests.serializable(CommutativeSemigroup[PosInt])
  )
  checkAll("CommutativeSemigroup[PosLong]", CommutativeSemigroupTests[PosLong].commutativeSemigroup)
  checkAll(
    "CommutativeSemigroup[PosLong]",
    SerializableTests.serializable(CommutativeSemigroup[PosLong])
  )
  // checkAll("CommutativeSemigroup[PosFloat]", CommutativeSemigroupTests[PosFloat].commutativeSemigroup) // approximately associative
  checkAll(
    "CommutativeSemigroup[PosFloat]",
    SerializableTests.serializable(CommutativeSemigroup[PosFloat])
  )
  // checkAll("CommutativeSemigroup[PosDouble]", CommutativeSemigroupTests[PosDouble].commutativeSemigroup) // approximately associative
  checkAll(
    "CommutativeSemigroup[PosDouble]",
    SerializableTests.serializable(CommutativeSemigroup[PosDouble])
  )
  checkAll(
    "CommutativeSemigroup[PosBigDecimal]",
    SerializableTests.serializable(CommutativeSemigroup[PosBigDecimal])
  )

  // Negative commutativeSemigroups
  checkAll("CommutativeSemigroup[NegByte]", CommutativeSemigroupTests[NegByte].commutativeSemigroup)
  checkAll(
    "CommutativeSemigroup[NegByte]",
    SerializableTests.serializable(CommutativeSemigroup[NegByte])
  )
  checkAll(
    "CommutativeSemigroup[NegShort]",
    CommutativeSemigroupTests[NegShort].commutativeSemigroup
  )
  checkAll(
    "CommutativeSemigroup[NegShort]",
    SerializableTests.serializable(CommutativeSemigroup[NegShort])
  )
  checkAll("CommutativeSemigroup[NegInt]", CommutativeSemigroupTests[NegInt].commutativeSemigroup)
  checkAll(
    "CommutativeSemigroup[NegInt]",
    SerializableTests.serializable(CommutativeSemigroup[NegInt])
  )
  checkAll("CommutativeSemigroup[NegLong]", CommutativeSemigroupTests[NegLong].commutativeSemigroup)
  checkAll(
    "CommutativeSemigroup[NegLong]",
    SerializableTests.serializable(CommutativeSemigroup[NegLong])
  )
  // checkAll("CommutativeSemigroup[NegFloat]", CommutativeSemigroupTests[NegFloat].commutativeSemigroup) // approximately associative
  checkAll(
    "CommutativeSemigroup[NegFloat]",
    SerializableTests.serializable(CommutativeSemigroup[NegFloat])
  )
  // checkAll("CommutativeSemigroup[NegDouble]", CommutativeSemigroupTests[NegDouble].commutativeSemigroup) // approximately associative
  checkAll(
    "CommutativeSemigroup[NegDouble]",
    SerializableTests.serializable(CommutativeSemigroup[NegDouble])
  )
  checkAll(
    "CommutativeSemigroup[NegBigDecimal]",
    SerializableTests.serializable(CommutativeSemigroup[NegBigDecimal])
  )

  // NonNegative monoids
  checkAll("CommutativeMonoid[NonNegByte]", CommutativeMonoidTests[NonNegByte].commutativeMonoid)
  checkAll(
    "CommutativeMonoid[NonNegByte]",
    SerializableTests.serializable(CommutativeMonoid[NonNegByte])
  )
  checkAll("CommutativeMonoid[NonNegShort]", CommutativeMonoidTests[NonNegShort].commutativeMonoid)
  checkAll(
    "CommutativeMonoid[NonNegShort]",
    SerializableTests.serializable(CommutativeMonoid[NonNegShort])
  )
  checkAll("CommutativeMonoid[NonNegInt]", CommutativeMonoidTests[NonNegInt].commutativeMonoid)
  checkAll(
    "CommutativeMonoid[NonNegInt]",
    SerializableTests.serializable(CommutativeMonoid[NonNegInt])
  )
  checkAll("CommutativeMonoid[NonNegLong]", CommutativeMonoidTests[NonNegLong].commutativeMonoid)
  checkAll(
    "CommutativeMonoid[NonNegLong]",
    SerializableTests.serializable(CommutativeMonoid[NonNegLong])
  )
  // checkAll("Monoid[NonNegFloat]", CommutativeMonoidTests[NonNegFloat].commutativeMonoid) // approximately associative
  checkAll(
    "CommutativeMonoid[NonNegFloat]",
    SerializableTests.serializable(CommutativeMonoid[NonNegFloat])
  )
  // checkAll("Monoid[NonNegDouble]", CommutativeMonoidTests[NonNegDouble].commutativeMonoid) // approximately associative
  checkAll(
    "CommutativeMonoid[NonNegDouble]",
    SerializableTests.serializable(CommutativeMonoid[NonNegDouble])
  )
  checkAll(
    "CommutativeMonoid[NonNegBigDecimal]",
    SerializableTests.serializable(CommutativeMonoid[NonNegBigDecimal])
  )

  // NonPositive monoids
  // checkAll("Monoid[NonPosFloat]", CommutativeMonoidTests[NonPosFloat].commutativeMonoid) // approximately associative
  checkAll(
    "CommutativeMonoid[NonPosFloat]",
    SerializableTests.serializable(CommutativeMonoid[NonPosFloat])
  )
  // checkAll("Monoid[NonPosDouble]", CommutativeMonoidTests[NonPosDouble].commutativeMonoid) // approximately associative
  checkAll(
    "CommutativeMonoid[NonPosDouble]",
    SerializableTests.serializable(CommutativeMonoid[NonPosDouble])
  )

}
