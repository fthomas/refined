package eu.timepit.refined.types

import eu.timepit.refined.types.all._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import eu.timepit.refined.scalacheck.numeric._

class NumericOrderingSpec extends Properties("NumericOrderingSpec") {

  property("PosByte") = forAll { (x: List[PosByte]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NonNegByte") = forAll { (x: List[NonNegByte]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NegByte") = forAll { (x: List[NegByte]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NonPosByte") = forAll { (x: List[NonPosByte]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("PosShort") = forAll { (x: List[PosShort]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NonNegShort") = forAll { (x: List[NonNegShort]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NegShort") = forAll { (x: List[NegShort]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NonPosShort") = forAll { (x: List[NonPosShort]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("PosInt") = forAll { (x: List[PosInt]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NonNegInt") = forAll { (x: List[NonNegInt]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NegInt") = forAll { (x: List[NegInt]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NonPosInt") = forAll { (x: List[NonPosInt]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("PosLong") = forAll { (x: List[PosLong]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NonNegLong") = forAll { (x: List[NonNegLong]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NegLong") = forAll { (x: List[NegLong]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NonPosLong") = forAll { (x: List[NonPosLong]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("PosFloat") = forAll { (x: List[PosFloat]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NonNegFloat") = forAll { (x: List[NonNegFloat]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NegFloat") = forAll { (x: List[NegFloat]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NonPosFloat") = forAll { (x: List[NonPosFloat]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("PosDouble") = forAll { (x: List[PosDouble]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NonNegDouble") = forAll { (x: List[NonNegDouble]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NegDouble") = forAll { (x: List[NegDouble]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

  property("NonPosDouble") = forAll { (x: List[NonPosDouble]) =>
    x.sorted.map(_.value) == x.map(_.value).sorted
  }

}
