package eu.timepit.refined.scalaz

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import eu.timepit.refined.string.StartsWith
import org.scalacheck.Prop._
import org.scalacheck.Properties

class InferAnyValTest {

  val r = new RefineAnyValTest

  def Refined: Refined[Int, Greater[W.`0`.T]] = r.Refined

  def shapeless_tag: shapeless.tag.@@[Int, Greater[W.`0`.T]] = r.shapeless_tag

  def scalaz_tag: scalaz.@@[Int, Greater[W.`0`.T]] = r.scalaz_tag

  def unrefined: Int = r.unrefined
}

class InferAnyRefTest {

  val r = new RefineAnyRefTest

  def Refined: Refined[String, StartsWith[W.`"ab"`.T]] = r.Refined

  def shapeless_tag: shapeless.tag.@@[String, StartsWith[W.`"ab"`.T]] = r.shapeless_tag

  def scalaz_tag: scalaz.@@[String, StartsWith[W.`"ab"`.T]] = r.scalaz_tag

  def unrefined: String = r.unrefined
}

class InferJavapSpec extends Properties("InferJavapTest") {

  property("javap -c InferAnyValTest") = secure {
    val actual = javapOutput(new InferAnyValTest, "-c")
    val expected =
      """Compiled from "InferJavapSpec.scala"
        |public class eu.timepit.refined.scalaz.InferAnyValTest {
        |  public eu.timepit.refined.scalaz.RefineAnyValTest r();
        |    Code:
        |       0: aload_0
        |       1: getfield      #13                 // Field r:Leu/timepit/refined/scalaz/RefineAnyValTest;
        |       4: areturn
        |  public int Refined();
        |    Code:
        |       0: getstatic     #24                 // Field eu/timepit/refined/api/Refined$.MODULE$:Leu/timepit/refined/api/Refined$;
        |       3: aload_0
        |       4: invokevirtual #26                 // Method r:()Leu/timepit/refined/scalaz/RefineAnyValTest;
        |       7: invokevirtual #30                 // Method eu/timepit/refined/scalaz/RefineAnyValTest.Refined:()Ljava/lang/Integer;
        |      10: invokevirtual #34                 // Method eu/timepit/refined/api/Refined$.unsafeApply:(Ljava/lang/Object;)Ljava/lang/Object;
        |      13: checkcast     #36                 // class java/lang/Integer
        |      16: areturn
        |  public int shapeless_tag();
        |    Code:
        |       0: aload_0
        |       1: invokevirtual #26                 // Method r:()Leu/timepit/refined/scalaz/RefineAnyValTest;
        |       4: invokevirtual #39                 // Method eu/timepit/refined/scalaz/RefineAnyValTest.shapeless_tag:()I
        |       7: invokestatic  #45                 // Method scala/runtime/BoxesRunTime.boxToInteger:(I)Ljava/lang/Integer;
        |      10: invokestatic  #49                 // Method scala/runtime/BoxesRunTime.unboxToInt:(Ljava/lang/Object;)I
        |      13: ireturn
        |  public java.lang.Object scalaz_tag();
        |    Code:
        |       0: aload_0
        |       1: invokevirtual #26                 // Method r:()Leu/timepit/refined/scalaz/RefineAnyValTest;
        |       4: invokevirtual #53                 // Method eu/timepit/refined/scalaz/RefineAnyValTest.scalaz_tag:()Ljava/lang/Object;
        |       7: areturn
        |  public int unrefined();
        |    Code:
        |       0: aload_0
        |       1: invokevirtual #26                 // Method r:()Leu/timepit/refined/scalaz/RefineAnyValTest;
        |       4: invokevirtual #56                 // Method eu/timepit/refined/scalaz/RefineAnyValTest.unrefined:()I
        |       7: ireturn
        |  public eu.timepit.refined.scalaz.InferAnyValTest();
        |    Code:
        |       0: aload_0
        |       1: invokespecial #60                 // Method java/lang/Object."<init>":()V
        |       4: aload_0
        |       5: new           #28                 // class eu/timepit/refined/scalaz/RefineAnyValTest
        |       8: dup
        |       9: invokespecial #61                 // Method eu/timepit/refined/scalaz/RefineAnyValTest."<init>":()V
        |      12: putfield      #13                 // Field r:Leu/timepit/refined/scalaz/RefineAnyValTest;
        |      15: return
        |}
      """.stripMargin.trim
    actual ?= expected
  }

  property("javap -c InferAnyRefTest") = secure {
    val actual = javapOutput(new InferAnyRefTest, "-c")
    val expected =
      """Compiled from "InferJavapSpec.scala"
        |public class eu.timepit.refined.scalaz.InferAnyRefTest {
        |  public eu.timepit.refined.scalaz.RefineAnyRefTest r();
        |    Code:
        |       0: aload_0
        |       1: getfield      #13                 // Field r:Leu/timepit/refined/scalaz/RefineAnyRefTest;
        |       4: areturn
        |  public java.lang.String Refined();
        |    Code:
        |       0: getstatic     #23                 // Field eu/timepit/refined/api/Refined$.MODULE$:Leu/timepit/refined/api/Refined$;
        |       3: aload_0
        |       4: invokevirtual #25                 // Method r:()Leu/timepit/refined/scalaz/RefineAnyRefTest;
        |       7: invokevirtual #29                 // Method eu/timepit/refined/scalaz/RefineAnyRefTest.Refined:()Ljava/lang/String;
        |      10: invokevirtual #33                 // Method eu/timepit/refined/api/Refined$.unsafeApply:(Ljava/lang/Object;)Ljava/lang/Object;
        |      13: checkcast     #35                 // class java/lang/String
        |      16: areturn
        |  public java.lang.String shapeless_tag();
        |    Code:
        |       0: aload_0
        |       1: invokevirtual #25                 // Method r:()Leu/timepit/refined/scalaz/RefineAnyRefTest;
        |       4: invokevirtual #38                 // Method eu/timepit/refined/scalaz/RefineAnyRefTest.shapeless_tag:()Ljava/lang/String;
        |       7: areturn
        |  public java.lang.Object scalaz_tag();
        |    Code:
        |       0: aload_0
        |       1: invokevirtual #25                 // Method r:()Leu/timepit/refined/scalaz/RefineAnyRefTest;
        |       4: invokevirtual #42                 // Method eu/timepit/refined/scalaz/RefineAnyRefTest.scalaz_tag:()Ljava/lang/Object;
        |       7: areturn
        |  public java.lang.String unrefined();
        |    Code:
        |       0: aload_0
        |       1: invokevirtual #25                 // Method r:()Leu/timepit/refined/scalaz/RefineAnyRefTest;
        |       4: invokevirtual #45                 // Method eu/timepit/refined/scalaz/RefineAnyRefTest.unrefined:()Ljava/lang/String;
        |       7: areturn
        |  public eu.timepit.refined.scalaz.InferAnyRefTest();
        |    Code:
        |       0: aload_0
        |       1: invokespecial #49                 // Method java/lang/Object."<init>":()V
        |       4: aload_0
        |       5: new           #27                 // class eu/timepit/refined/scalaz/RefineAnyRefTest
        |       8: dup
        |       9: invokespecial #50                 // Method eu/timepit/refined/scalaz/RefineAnyRefTest."<init>":()V
        |      12: putfield      #13                 // Field r:Leu/timepit/refined/scalaz/RefineAnyRefTest;
        |      15: return
        |}
      """.stripMargin.trim
    actual ?= expected
  }
}
