package eu.example

object Example {
  import eu.timepit.refined.numeric._
  import eu.timepit.refined.api.Refined
  import eu.timepit.refined.auto._
  import eu.timepit.refined._
  import eu.timepit.refined.api._
  import eu.timepit.refined.auto._

  @main def main(): Unit =
    val x: Int Refined Greater[5] = autoRefineV[Greater[5]](6)
    // does not compile
    // val x2: Int Refined Greater[5] = autoRefineV[Greater[5]](5)
    println("hello")
}

