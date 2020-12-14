package eu.timepit.refined.scallop

import eu.timepit.refined.types.numeric.PosInt
import org.scalacheck.Prop._
import org.scalacheck.Properties
import org.rogach.scallop._
import org.rogach.scallop.exceptions._
import scala.util.{Try, Success, Failure}

class RefTypeReadSpec extends Properties("RefTypeRead") {
  org.rogach.scallop.throwError.value = true

  class Config(args: Seq[String]) extends ScallopConf(args) {
    val foo = trailArg[PosInt](descr = "foo is a positive integer property")
    verify()
  }

  property("load success") = secure {
    loadConfigWithValue("10").map(_.foo()) =?
      Success(PosInt.unsafeFrom(10))
  }

  property("load failure (predicate)") = secure {
    loadConfigWithValue("0") =?
      Failure(
        WrongOptionFormat(
          "foo",
          "0",
          "java.lang.IllegalArgumentException: Predicate failed: (0 > 0)."
        )
      )
  }

  property("load failure (wrong type)") = secure {
    loadConfigWithValue("abc") =?
      Failure(WrongOptionFormat("foo", "abc", "bad Int value"))
  }

  def loadConfigWithValue(value: String): Try[Config] =
    Try(new Config(Seq(value)))

}
