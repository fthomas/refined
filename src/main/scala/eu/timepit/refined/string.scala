package eu.timepit.refined

import eu.timepit.refined.collection._

object string {
  implicit val emptyStringPredicate: Predicate[Empty, String] =
    new Predicate[Empty, String] {
      def isValid(t: String): Boolean = t.isEmpty
      def show(t: String): String = s"isEmpty($t)"
    }

  implicit def lengthStringPredicate[P](implicit p: Predicate[P, Int]): Predicate[Length[P], String] =
    new Predicate[Length[P], String] {
      def isValid(t: String): Boolean = p.isValid(t.length)
      def show(t: String): String = s"${p.show(t.length)}"

      override def validated(t: String): Option[String] = {
        val l = t.length
        p.validated(l).map(s => s"Predicate taking length($t) = $l failed: $s")
      }
    }
}
