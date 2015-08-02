package eu.timepit.refined
package examples

import eu.timepit.refined.implicits._
import eu.timepit.refined.string._

object ZeroOverhead extends App {

  val x: String Refined Regex = "(a|b)"

  val y: String = "(a|b)"

  println(x.get == y)
}

/*
This is what scalac 2.11.7 outputs with -Xprint:posterasure:

[[syntax trees at end of               posterasure]] // ZeroOverhead.scala
package eu.timepit.refined {
  package examples {
    object ZeroOverhead extends Object with App {
      def <init>(): eu.timepit.refined.examples.ZeroOverhead.type = {
        ZeroOverhead.super.<init>();
        ZeroOverhead.this.$asInstanceOf[App$class]().$init$();
        ()
      };
      private[this] val x: String = ("(a|b)": String);
      <stable> <accessor> def x(): String = ZeroOverhead.this.x;
      private[this] val y: String = "(a|b)";
      <stable> <accessor> def y(): String = ZeroOverhead.this.y;
      scala.this.Predef.println(scala.Boolean.box(ZeroOverhead.this.x().==(ZeroOverhead.this.y())))
    }
  }
}
*/
