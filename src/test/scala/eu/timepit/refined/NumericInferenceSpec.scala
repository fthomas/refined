package eu.timepit.refined

import eu.timepit.refined.numeric._
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.tag.@@
import shapeless.test.illTyped

class NumericInferenceSpec extends Properties("NumericInference") {

  property("Less success") = secure {
    val a: Double @@ Less[W.`7.2`.T] = refineLit(1.0)
    val b: Double @@ Less[W.`7.5`.T] = a
    a == b
  }

  property("Less failure") = secure {
    illTyped("""
      val a: Double @@ Less[W.`7.5`.T] = refineLit(1.0)
      val b: Double @@ Less[W.`7.2`.T] = a
      """)
    true
  }

  property("LessEqual success") = secure {
    val a: Double @@ LessEqual[W.`7.2`.T] = refineLit(1.0)
    val b: Double @@ LessEqual[W.`7.5`.T] = a
    a == b
  }

  property("LessEqual failure") = secure {
    illTyped("""
      val a: Double @@ LessEqual[W.`7.5`.T] = refineLit(1.0)
      val b: Double @@ LessEqual[W.`7.2`.T] = a
      """)
    true
  }

  property("Greater success") = secure {
    val a: Double @@ Greater[W.`7.5`.T] = refineLit(10.0)
    val b: Double @@ Greater[W.`7.2`.T] = a
    a == b
  }

  property("Greater failure") = secure {
    illTyped("""
      val a: Double @@ Greater[W.`7.2`.T] = refineLit(10.0)
      val b: Double @@ Greater[W.`7.5`.T] = a
      """)
    true
  }

  property("GreaterEqual success") = secure {
    val a: Double @@ GreaterEqual[W.`7.5`.T] = refineLit(10.0)
    val b: Double @@ GreaterEqual[W.`7.2`.T] = a
    a == b
  }

  property("GreaterEqual failure") = secure {
    illTyped("""
      val a: Double @@ GreaterEqual[W.`7.2`.T] = refineLit(10.0)
      val b: Double @@ GreaterEqual[W.`7.5`.T] = a
      """)
    true
  }

  property("Less.Nat success") = secure {
    val a: Int @@ Less[_5] = refineLit(1)
    val b: Int @@ Less[_10] = a
    a == b
  }

  property("Less.Nat failure") = secure {
    illTyped("""
      val a: Int @@ Less[_10] = refineLit(1)
      val b: Int @@ Less[_5] = a
      """)
    true
  }

  property("Greater.Nat success") = secure {
    val a: Int @@ Greater[_10] = refineLit(15)
    val b: Int @@ Greater[_5] = a
    a == b
  }

  property("Greater.Nat failure") = secure {
    illTyped("""
      val a: Int @@ Greater[_5] = refineLit(15)
      val b: Int @@ Greater[_10] = a
      """)
    true
  }
}
