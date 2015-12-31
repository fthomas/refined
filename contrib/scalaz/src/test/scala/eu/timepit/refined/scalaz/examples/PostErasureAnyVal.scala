package eu.timepit.refined.scalaz
package examples

import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import eu.timepit.refined.scalaz.auto._

object PostErasureAnyVal {

  val a1: Int Refined Positive = 2

  val a2: Int = a1

  val b1: shapeless.tag.@@[Int, Positive] = 1

  val b2: Int = b1

  val c1: scalaz.@@[Int, Positive] = 1

  val c2: Int = c1

  val d: Int = 1
}

/*
This is what scalac 2.11.7 outputs with -Xprint:posterasure:

package eu.timepit.refined.scalaz {
  package examples {
    object PostErasureAnyVal extends Object {
      def <init>(): eu.timepit.refined.scalaz.examples.PostErasureAnyVal.type = {
        PostErasureAnyVal.super.<init>();
        ()
      };
      private[this] val a1: eu.timepit.refined.api.Refined = Refined.unsafeApply(scala.Int.box(2));
      <stable> <accessor> def a1(): eu.timepit.refined.api.Refined = PostErasureAnyVal.this.a1;

      private[this] val a2: Int = unbox(eu.timepit.refined.auto.autoUnwrap(PostErasureAnyVal.this.a1(), api.this.RefType.refinedRefType()));
      <stable> <accessor> def a2(): Int = PostErasureAnyVal.this.a2;

      private[this] val b1: Int = unbox(scala.Int.box(1));
      <stable> <accessor> def b1(): Int = PostErasureAnyVal.this.b1;

      private[this] val b2: Int = PostErasureAnyVal.this.b1();
      <stable> <accessor> def b2(): Int = PostErasureAnyVal.this.b2;

      private[this] val c1: Object = scala.Int.box(1);
      <stable> <accessor> def c1(): Object = PostErasureAnyVal.this.c1;

      private[this] val c2: Int = unbox(eu.timepit.refined.auto.autoUnwrap(PostErasureAnyVal.this.c1(), scalaz.this.`package`.scalazTagRefType()));
      <stable> <accessor> def c2(): Int = PostErasureAnyVal.this.c2;

      private[this] val d: Int = 1;
      <stable> <accessor> def d(): Int = PostErasureAnyVal.this.d
    }
  }
}
*/
