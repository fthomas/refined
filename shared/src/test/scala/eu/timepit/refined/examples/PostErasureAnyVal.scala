package eu.timepit.refined
package examples

import eu.timepit.refined.implicits._
import eu.timepit.refined.numeric._
import shapeless.tag.@@

object PostErasureAnyVal {

  val x: Int Refined Positive = 1

  val y: Int @@ Positive = 1

  val z: Int = 1
}

/*
This is what scalac 2.11.7 outputs with -Xprint:posterasure:

package eu.timepit.refined {
  package examples {
    object PostErasureAnyVal extends Object {
      def <init>(): eu.timepit.refined.examples.PostErasureAnyVal.type = {
        PostErasureAnyVal.super.<init>();
        ()
      };

      private[this] val x: Integer = (scala.Int.box(1).$asInstanceOf[Integer](): Integer);
      <stable> <accessor> def x(): Integer = PostErasureAnyVal.this.x;

      private[this] val y: Int = (unbox(scala.Int.box(1)): Int);
      <stable> <accessor> def y(): Int = PostErasureAnyVal.this.y;

      private[this] val z: Int = 1;
      <stable> <accessor> def z(): Int = PostErasureAnyVal.this.z
    }
  }
}
*/
