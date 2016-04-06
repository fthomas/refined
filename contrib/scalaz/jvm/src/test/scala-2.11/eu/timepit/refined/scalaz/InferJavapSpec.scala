package eu.timepit.refined.scalaz

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import eu.timepit.refined.scalaz.auto._
import eu.timepit.refined.string.StartsWith
import org.scalacheck.Prop._
import org.scalacheck.Properties

class InferAnyValTest {

  def Refined1: Refined[Int, Greater[W.`1`.T]] = 10

  def Refined0: Refined[Int, Greater[W.`0`.T]] = Refined1

  def shapeless_tag1: shapeless.tag.@@[Int, Greater[W.`1`.T]] = 10

  def shapeless_tag0: shapeless.tag.@@[Int, Greater[W.`0`.T]] = shapeless_tag1

  def scalaz_tag1: scalaz.@@[Int, Greater[W.`1`.T]] = 10

  def scalaz_tag0: scalaz.@@[Int, Greater[W.`0`.T]] = scalaz_tag1

  def unrefined1: Int = 10

  def unrefined0: Int = unrefined1
}

class InferAnyRefTest {

  def Refined1: Refined[String, StartsWith[W.`"abc"`.T]] = "abc"

  def Refined0: Refined[String, StartsWith[W.`"ab"`.T]] = Refined1

  def shapeless_tag1: shapeless.tag.@@[String, StartsWith[W.`"abc"`.T]] = "abc"

  def shapeless_tag0: shapeless.tag.@@[String, StartsWith[W.`"ab"`.T]] = shapeless_tag1

  def scalaz_tag1: scalaz.@@[String, StartsWith[W.`"abc"`.T]] = "abc"

  def scalaz_tag0: scalaz.@@[String, StartsWith[W.`"ab"`.T]] = scalaz_tag1

  def unrefined1: String = "abc"

  def unrefined0: String = unrefined1
}

class InferJavapSpec extends Properties("InferJavapTest") {

  property("javap -c InferAnyValTest") = secure {
    val actual = javapOutput(new InferAnyValTest, "-c")
    val expected =
      """Compiled from "InferJavapSpec.scala"
        |public class eu.timepit.refined.scalaz.InferAnyValTest {
        |  public int Refined1();
        |    Code:
        |       0: getstatic     #16                 // Field eu/timepit/refined/api/Refined$.MODULE$:Leu/timepit/refined/api/Refined$;
        |       3: bipush        10
        |       5: invokestatic  #22                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        |       8: invokevirtual #26                 // Method eu/timepit/refined/api/Refined$.unsafeApply:(Ljava/lang/Object;)Ljava/lang/Object;
        |      11: checkcast     #28                 // class java/lang/Integer
        |      14: areturn
        |  public int Refined0();
        |    Code:
        |       0: getstatic     #16                 // Field eu/timepit/refined/api/Refined$.MODULE$:Leu/timepit/refined/api/Refined$;
        |       3: aload_0
        |       4: invokevirtual #33                 // Method Refined1:()Ljava/lang/Integer;
        |       7: invokevirtual #26                 // Method eu/timepit/refined/api/Refined$.unsafeApply:(Ljava/lang/Object;)Ljava/lang/Object;
        |      10: checkcast     #28                 // class java/lang/Integer
        |      13: areturn
        |  public java.lang.Object shapeless_tag1();
        |    Code:
        |       0: bipush        10
        |       2: invokestatic  #22                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        |       5: invokestatic  #39                 // Method scala/runtime/BoxesRunTime.unboxToInt:(Ljava/lang/Object;)I
        |       8: ireturn
        |  public java.lang.Object shapeless_tag0();
        |    Code:
        |       0: aload_0
        |       1: invokevirtual #42                 // Method shapeless_tag1:()I
        |       4: invokestatic  #22                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        |       7: invokestatic  #39                 // Method scala/runtime/BoxesRunTime.unboxToInt:(Ljava/lang/Object;)I
        |      10: ireturn
        |  public java.lang.Object scalaz_tag1();
        |    Code:
        |       0: bipush        10
        |       2: invokestatic  #22                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        |       5: areturn
        |  public java.lang.Object scalaz_tag0();
        |    Code:
        |       0: aload_0
        |       1: invokevirtual #47                 // Method scalaz_tag1:()Ljava/lang/Object;
        |       4: areturn
        |  public int unrefined1();
        |    Code:
        |       0: bipush        10
        |       2: ireturn
        |  public int unrefined0();
        |    Code:
        |       0: aload_0
        |       1: invokevirtual #51                 // Method unrefined1:()I
        |       4: ireturn
        |  public eu.timepit.refined.scalaz.InferAnyValTest();
        |    Code:
        |       0: aload_0
        |       1: invokespecial #55                 // Method java/lang/Object."<init>":()V
        |       4: return
        |}
      """.stripMargin.trim
    actual ?= expected
  }

  property("javap -c InferAnyRefTest") = secure {
    val actual = javapOutput(new InferAnyRefTest, "-c")
    val expected =
      """Compiled from "InferJavapSpec.scala"
        |public class eu.timepit.refined.scalaz.InferAnyRefTest {
        |  public java.lang.String Refined1();
        |    Code:
        |       0: getstatic     #16                 // Field eu/timepit/refined/api/Refined$.MODULE$:Leu/timepit/refined/api/Refined$;
        |       3: ldc           #18                 // String abc
        |       5: invokevirtual #22                 // Method eu/timepit/refined/api/Refined$.unsafeApply:(Ljava/lang/Object;)Ljava/lang/Object;
        |       8: checkcast     #24                 // class java/lang/String
        |      11: areturn
        |  public java.lang.String Refined0();
        |    Code:
        |       0: getstatic     #16                 // Field eu/timepit/refined/api/Refined$.MODULE$:Leu/timepit/refined/api/Refined$;
        |       3: aload_0
        |       4: invokevirtual #29                 // Method Refined1:()Ljava/lang/String;
        |       7: invokevirtual #22                 // Method eu/timepit/refined/api/Refined$.unsafeApply:(Ljava/lang/Object;)Ljava/lang/Object;
        |      10: checkcast     #24                 // class java/lang/String
        |      13: areturn
        |  public java.lang.String shapeless_tag1();
        |    Code:
        |       0: ldc           #18                 // String abc
        |       2: areturn
        |  public java.lang.String shapeless_tag0();
        |    Code:
        |       0: aload_0
        |       1: invokevirtual #33                 // Method shapeless_tag1:()Ljava/lang/String;
        |       4: areturn
        |  public java.lang.Object scalaz_tag1();
        |    Code:
        |       0: ldc           #18                 // String abc
        |       2: areturn
        |  public java.lang.Object scalaz_tag0();
        |    Code:
        |       0: aload_0
        |       1: invokevirtual #38                 // Method scalaz_tag1:()Ljava/lang/Object;
        |       4: areturn
        |  public java.lang.String unrefined1();
        |    Code:
        |       0: ldc           #18                 // String abc
        |       2: areturn
        |  public java.lang.String unrefined0();
        |    Code:
        |       0: aload_0
        |       1: invokevirtual #42                 // Method unrefined1:()Ljava/lang/String;
        |       4: areturn
        |  public eu.timepit.refined.scalaz.InferAnyRefTest();
        |    Code:
        |       0: aload_0
        |       1: invokespecial #46                 // Method java/lang/Object."<init>":()V
        |       4: return
        |}
      """.stripMargin.trim
    actual ?= expected
  }
}
