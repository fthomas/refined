package eu.timepit.refined.test

import scala.compiletime.testing.{Error, typeCheckErrors}

object ScalaVersionSpecific {

  /**
   * Scala 3 stand-in for `shapeless.test.illTyped`, used by the ported test
   * suites. It asserts that `code` does not compile and, when a pattern is
   * given, that at least one reported error message matches it.
   *
   * The pattern is matched against the Scala 3 compiler's diagnostics, which
   * differ from Scala 2's — so the ported call sites use Scala-3-accurate
   * patterns rather than the originals.
   */
  object illTyped {

    inline def apply(inline code: String): Unit =
      check(typeCheckErrors(code), code, None)

    inline def apply(inline code: String, inline expected: String): Unit =
      check(typeCheckErrors(code), code, Some(expected))

    // Deliberately a plain method with an explicit `throw` (not `assert`, which is elidable) so the
    // check can never be silently compiled out.
    private def check(errors: List[Error], code: String, expected: Option[String]): Unit = {
      if (errors.isEmpty)
        throw new AssertionError(s"Expected a compile error, but the code type-checked:\n$code")
      expected.foreach { pattern =>
        val message = errors.iterator.map(_.message).mkString("\n")
        if (pattern.r.findFirstMatchIn(message).isEmpty)
          throw new AssertionError(
            s"Expected a compile error matching /$pattern/, but got:\n$message"
          )
      }
    }
  }
}
