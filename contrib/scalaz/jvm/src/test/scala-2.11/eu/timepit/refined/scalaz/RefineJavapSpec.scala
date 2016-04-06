package eu.timepit.refined.scalaz

import eu.timepit.refined.TestUtils.javapOutput
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.collection.Empty
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.scalaz.auto._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class RefineAnyValTest {

  def Refined: Refined[Int, Positive] = 1

  def shapeless_tag: shapeless.tag.@@[Int, Positive] = 1

  def scalaz_tag: scalaz.@@[Int, Positive] = 1

  def unrefined: Int = 1
}

class RefineAnyRefTest {

  def Refined: Refined[String, Empty] = ""

  def shapeless_tag: shapeless.tag.@@[String, Empty] = ""

  def scalaz_tag: scalaz.@@[String, Empty] = ""

  def unrefined: String = ""
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
        |       3: iconst_1
        |       4: invokestatic  #22                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        |       7: invokevirtual #26                 // Method eu/timepit/refined/api/Refined$.unsafeApply:(Ljava/lang/Object;)Ljava/lang/Object;
        |      10: checkcast     #28                 // class java/lang/Integer
        |      13: areturn
        |  public java.lang.Object shapeless_tag();
        |    Code:
        |       0: iconst_1
        |       1: invokestatic  #22                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        |       4: invokestatic  #36                 // Method scala/runtime/BoxesRunTime.unboxToInt:(Ljava/lang/Object;)I
        |       7: ireturn
        |  public java.lang.Object scalaz_tag();
        |    Code:
        |       0: iconst_1
        |       1: invokestatic  #22                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        |       4: areturn
        |  public int unrefined();
        |    Code:
        |       0: iconst_1
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
        |       3: ldc           #18                 // String
        |       5: invokevirtual #22                 // Method eu/timepit/refined/api/Refined$.unsafeApply:(Ljava/lang/Object;)Ljava/lang/Object;
        |       8: checkcast     #24                 // class java/lang/String
        |      11: areturn
        |  public java.lang.String shapeless_tag();
        |    Code:
        |       0: ldc           #18                 // String
        |       2: areturn
        |  public java.lang.Object scalaz_tag();
        |    Code:
        |       0: ldc           #18                 // String
        |       2: areturn
        |  public java.lang.String unrefined();
        |    Code:
        |       0: ldc           #18                 // String
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
}
