package eu.timepit.refined.scopt

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import scopt._

class RefTypeReadSpec extends Properties("RefTypeRead") {

  type PosInt = Int Refined Positive
  case class Config(foo: PosInt)

  val parser = new OptionParser[Config]("tests") {
    opt[PosInt]('f', "foo")
      .action((x, c) => c.copy(foo = x))
      .text("foo is a positive integer property")
  }

  property("load success") = secure {
    loadConfigWithValue("10") =?
      Some(Config(10))
  }

  property("load failure (predicate)") = secure {
    loadConfigWithValue("0") =?
      None
  }

  property("load failure (wrong type)") = secure {
    loadConfigWithValue("abc") =?
      None
  }

  def loadConfigWithValue(value: String): Option[Config] =
    parser.parse(Array("-f", value), Config(1))
}
