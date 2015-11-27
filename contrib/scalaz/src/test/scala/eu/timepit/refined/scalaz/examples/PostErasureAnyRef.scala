package eu.timepit.refined.scalaz
package examples

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.scalaz.auto._
import eu.timepit.refined.string._

object PostErasureAnyRef {

  val a1: String Refined Regex = "(a|b)"

  val a2: String = a1

  val b1: shapeless.tag.@@[String, Regex] = "(a|b)"

  val b2: String = b1

  val c1: scalaz.@@[String, Regex] = "(a|b)"

  val c2: String = c1

  val d: String = "(a|b)"
}

/*
This is what scalac 2.11.7 outputs with -Xprint:posterasure:

package eu.timepit.refined.scalaz {
  package examples {
    object PostErasureAnyRef extends Object {
      def <init>(): eu.timepit.refined.scalaz.examples.PostErasureAnyRef.type = {
        PostErasureAnyRef.super.<init>();
        ()
      };
      private[this] val a1: eu.timepit.refined.api.Refined = Refined.unsafeApply("(a|b)");
      <stable> <accessor> def a1(): eu.timepit.refined.api.Refined = PostErasureAnyRef.this.a1;

      private[this] val a2: String = eu.timepit.refined.auto.autoUnwrap(PostErasureAnyRef.this.a1(), api.this.RefType.refinedRefType()).$asInstanceOf[String]();
      <stable> <accessor> def a2(): String = PostErasureAnyRef.this.a2;

      private[this] val b1: String = "(a|b)";
      <stable> <accessor> def b1(): String = PostErasureAnyRef.this.b1;

      private[this] val b2: String = PostErasureAnyRef.this.b1();
      <stable> <accessor> def b2(): String = PostErasureAnyRef.this.b2;

      private[this] val c1: Object = "(a|b)";
      <stable> <accessor> def c1(): Object = PostErasureAnyRef.this.c1;

      private[this] val c2: String = eu.timepit.refined.auto.autoUnwrap(PostErasureAnyRef.this.c1(), scalaz.this.`package`.scalazTagRefType()).$asInstanceOf[String]();
      <stable> <accessor> def c2(): String = PostErasureAnyRef.this.c2;

      private[this] val d: String = "(a|b)";
      <stable> <accessor> def d(): String = PostErasureAnyRef.this.d
    }
  }
}
*/
