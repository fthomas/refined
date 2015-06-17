package eu.timepit.refined

import eu.timepit.refined.boolean.{ False, True }
import eu.timepit.refined.char._
import eu.timepit.refined.collection._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.MatchesRegex
import org.scalacheck.Prop._
import org.scalacheck.Properties
import shapeless.nat._
import shapeless.tag.@@
import shapeless.test.illTyped

class RefineLitSpec extends Properties("refineLit") {

  property("RefineLit instance") = secure {
    val r = refineLit[Digit]
    r('0') == '0'
  }

  property("refineLit with Forall") = secure {
    def ignore: String @@ Forall[LowerCase] = refineLit[Forall[LowerCase]]("hello")
    illTyped("""refineLit[Forall[UpperCase]]("hello")""")
    true
  }

  property("refineLit with Greater") = secure {
    def ignore: Int @@ Greater[_10] = refineLit[Greater[_10]](15)
    illTyped("""refineLit[Greater[_10]](5)""")
    true
  }

  property("refineLit with Size") = secure {
    type ShortString = Size[LessEqual[_10]]
    def ignore: String @@ ShortString = refineLit[ShortString]("abc")
    illTyped("""refineLit[ShortString]("abcdefghijklmnopqrstuvwxyz")""")
    true
  }

  property("refineLit with LowerCase") = secure {
    def ignore: Char @@ LowerCase = refineLit[LowerCase]('c')
    illTyped("refineLit[LowerCase]('C')")
    true
  }

  property("refineLit with MatchesRegex") = secure {
    def ignore: String @@ MatchesRegex[W.`"[0-9]+"`.T] = refineLit("123")
    illTyped("""refineLit[MatchesRegex[W.`"[0-9]+"`.T]]("abc")""")
    true
  }

  property("refineLit with Contains") = secure {
    def ignore: String @@ Contains[W.`'c'`.T] = refineLit("abcd")
    illTyped("""refineLit[Contains[W.`'c'`.T]]("abde")""")
    true
  }

  property("refineLit with Double Witness") = secure {
    def ignore: Double @@ Greater[W.`2.3`.T] = refineLit(2.4)
    illTyped("refineLit[Greater[W.`2.3`.T]](2.2)")
    true
  }
}
