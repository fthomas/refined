package eu.timepit.refined
package examples

import eu.timepit.refined.auto._
import eu.timepit.refined.string._
import shapeless.tag.@@

object PostErasureAnyRef {

  val x: String Refined Regex = "(a|b)"

  val y: String @@ Regex = "(a|b)"

  val z: String = "(a|b)"
}

/*
This is what scalac 2.11.7 outputs with -Xprint:posterasure:

package eu.timepit.refined {
  package examples {
    object PostErasureAnyRef extends Object {
      def <init>(): eu.timepit.refined.examples.PostErasureAnyRef.type = {
        PostErasureAnyRef.super.<init>();
        ()
      };

      private[this] val x: String = ("(a|b)": String);
      <stable> <accessor> def x(): String = PostErasureAnyRef.this.x;

      private[this] val y: String = ("(a|b)": String);
      <stable> <accessor> def y(): String = PostErasureAnyRef.this.y;

      private[this] val z: String = "(a|b)";
      <stable> <accessor> def z(): String = PostErasureAnyRef.this.z
    }
  }
}
*/
