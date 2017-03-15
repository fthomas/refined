package eu.timepit.refined.issues

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.api.Validate
import eu.timepit.refined.auto._
import org.scalacheck.Properties
import shapeless.tag.@@
import shapeless.test.illTyped

final case class CrashOnTypeTag[A]()

object CrashOnTypeTag {
  implicit def crashValidate[A: reflect.runtime.universe.TypeTag]
    : Validate.Plain[String, CrashOnTypeTag[A]] =
    Validate.fromPredicate(_ => true, _ => "check failed", CrashOnTypeTag[A]())
}

class Issue231 extends Properties("issue/231") {

  property("CrashOnTypeTag[String]") = wellTyped {
    illTyped(""" val crash: String @@ CrashOnTypeTag[String] = "function" """,
             ".*java.lang.ClassNotFoundException.*")
  }

  property("CrashOnTypeTag[Int => String]") = wellTyped {
    illTyped(""" val crash: String @@ CrashOnTypeTag[Int => String] = "function" """,
             ".*java.lang.ClassNotFoundException.*")
  }
}
