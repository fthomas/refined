package eu.timepit.refined.scalaz

import eu.timepit.refined.TestUtils.javapOutput
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.collection.Empty
import eu.timepit.refined.numeric.{Greater, Positive}
import eu.timepit.refined.scalaz.auto._
import eu.timepit.refined.string.StartsWith
import org.scalacheck.Prop._
import org.scalacheck.Properties

class RefineAnyValTest {

  def Refined: Refined[Int, Greater[W.`1`.T]] = 2

  def shapeless_tag: shapeless.tag.@@[Int, Greater[W.`1`.T]] = 2

  def scalaz_tag: scalaz.@@[Int, Greater[W.`1`.T]] = 2

  def unrefined: Int = 2
}

class RefineAnyRefTest {

  def Refined: Refined[String, StartsWith[W.`"abc"`.T]] = "abc"

  def shapeless_tag: shapeless.tag.@@[String, StartsWith[W.`"abc"`.T]] = "abc"

  def scalaz_tag: scalaz.@@[String, StartsWith[W.`"abc"`.T]] = "abc"

  def unrefined: String = "abc"
}

class RefineJavapSpec extends Properties("RefineJavap") {

  property("javap -c RefineAnyValTest") = secure {
    val actual = javapOutput(new RefineAnyValTest, "-c")
    val expected =
      """Compiled from "RefineJavapSpec.scala"
        |public class eu.timepit.refined.scalaz.RefineAnyValTest {
        |  public int Refined();
        |    Code:
        |       0: getstatic     #16                 // Field eu/timepit/refined/api/Refined$.MODULE$:Leu/timepit/refined/api/Refined$;
        |       3: iconst_2
        |       4: invokestatic  #22                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        |       7: invokevirtual #26                 // Method eu/timepit/refined/api/Refined$.unsafeApply:(Ljava/lang/Object;)Ljava/lang/Object;
        |      10: checkcast     #28                 // class java/lang/Integer
        |      13: areturn
        |  public java.lang.Object shapeless_tag();
        |    Code:
        |       0: iconst_2
        |       1: invokestatic  #22                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        |       4: invokestatic  #36                 // Method scala/runtime/BoxesRunTime.unboxToInt:(Ljava/lang/Object;)I
        |       7: ireturn
        |  public java.lang.Object scalaz_tag();
        |    Code:
        |       0: iconst_2
        |       1: invokestatic  #22                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        |       4: areturn
        |  public int unrefined();
        |    Code:
        |       0: iconst_2
        |       1: ireturn
        |  public eu.timepit.refined.scalaz.RefineAnyValTest();
        |    Code:
        |       0: aload_0
        |       1: invokespecial #43                 // Method java/lang/Object."<init>":()V
        |       4: return
        |}
      """.stripMargin.trim
    actual ?= expected
  }

  property("javap -c RefineAnyRefTest") = secure {
    val actual = javapOutput(new RefineAnyRefTest, "-c")
    val expected =
      """Compiled from "RefineJavapSpec.scala"
        |public class eu.timepit.refined.scalaz.RefineAnyRefTest {
        |  public java.lang.String Refined();
        |    Code:
        |       0: getstatic     #16                 // Field eu/timepit/refined/api/Refined$.MODULE$:Leu/timepit/refined/api/Refined$;
        |       3: ldc           #18                 // String abc
        |       5: invokevirtual #22                 // Method eu/timepit/refined/api/Refined$.unsafeApply:(Ljava/lang/Object;)Ljava/lang/Object;
        |       8: checkcast     #24                 // class java/lang/String
        |      11: areturn
        |  public java.lang.String shapeless_tag();
        |    Code:
        |       0: ldc           #18                 // String abc
        |       2: areturn
        |  public java.lang.Object scalaz_tag();
        |    Code:
        |       0: ldc           #18                 // String abc
        |       2: areturn
        |  public java.lang.String unrefined();
        |    Code:
        |       0: ldc           #18                 // String abc
        |       2: areturn
        |  public eu.timepit.refined.scalaz.RefineAnyRefTest();
        |    Code:
        |       0: aload_0
        |       1: invokespecial #34                 // Method java/lang/Object."<init>":()V
        |       4: return
        |}
      """.stripMargin.trim
    actual ?= expected
  }

  property("Array[AnyVal].getClass: Refined") = secure {
    Array(1: Refined[Int, Positive]).getClass == classOf[Array[Refined[
                Int, Positive]]]
  }

  property("Array[AnyVal].getClass: shapeless.tag.@@") = secure {
    Array(1: shapeless.tag.@@[Int, Positive]).getClass == classOf[Array[Int]]
  }

  property("Array[AnyVal].getClass: scalaz.@@") = secure {
    Array(1: scalaz.@@[Int, Positive]).getClass == classOf[Array[Any]]
  }

  property("Array[AnyRef].getClass: Refined") = secure {
    Array("": Refined[String, Empty]).getClass == classOf[Array[Refined[
                String, Empty]]]
  }

  property("Array[AnyRef].getClass: shapeless.tag.@@") = secure {
    Array("": shapeless.tag.@@[String, Empty]).getClass == classOf[Array[
            String]]
  }

  property("Array[AnyRef].getClass: scalaz.@@") = secure {
    Array("": scalaz.@@[String, Empty]).getClass == classOf[Array[Any]]
  }
}
