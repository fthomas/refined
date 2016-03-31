package eu.timepit.refined.scalaz

import eu.timepit.refined.TestUtils.javapOutput
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.collection.Empty
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.scalaz.auto._
import org.scalacheck.Prop._
import org.scalacheck.Properties

class AnyValTest {

  def Refined: Refined[Int, Positive] = 1

  def shapeless_tag: shapeless.tag.@@[Int, Positive] = 1

  def scalaz_tag: scalaz.@@[Int, Positive] = 1

  def unrefined: Int = 1
}

class AnyRefTest {

  def Refined: Refined[String, Empty] = ""

  def shapeless_tag: shapeless.tag.@@[String, Empty] = ""

  def scalaz_tag: scalaz.@@[String, Empty] = ""

  def unrefined: String = ""
}

class RefTypeJavapSpec extends Properties("RefTypeJavap") {

  property("javap AnyValTest") = secure {
    val actual = javapOutput(new AnyValTest)
    val expected =
      """Compiled from "RefTypeJavapSpec.scala"
        |public class eu.timepit.refined.scalaz.AnyValTest {
        |  public int Refined();
        |  public java.lang.Object shapeless_tag();
        |  public java.lang.Object scalaz_tag();
        |  public int unrefined();
        |  public eu.timepit.refined.scalaz.AnyValTest();
        |}
      """.stripMargin.trim
    actual ?= expected
  }

  property("javap -c AnyValTest") = secure {
    val actual = javapOutput(new AnyValTest, "-c")
    val expected =
      """Compiled from "RefTypeJavapSpec.scala"
        |public class eu.timepit.refined.scalaz.AnyValTest {
        |  public int Refined();
        |    Code:
        |       0: getstatic     #16                 // Field eu/timepit/refined/api/RefType$.MODULE$:Leu/timepit/refined/api/RefType$;
        |       3: invokevirtual #20                 // Method eu/timepit/refined/api/RefType$.refinedRefType:()Leu/timepit/refined/api/RefType;
        |       6: iconst_1
        |       7: invokeinterface #26,  2           // InterfaceMethod eu/timepit/refined/api/RefType.unsafeWrap$mIc$sp:(I)Ljava/lang/Object;
        |      12: checkcast     #28                 // class eu/timepit/refined/api/Refined
        |      15: invokevirtual #32                 // Method eu/timepit/refined/api/Refined.get:()Ljava/lang/Object;
        |      18: checkcast     #34                 // class java/lang/Integer
        |      21: areturn
        |  public java.lang.Object shapeless_tag();
        |    Code:
        |       0: getstatic     #16                 // Field eu/timepit/refined/api/RefType$.MODULE$:Leu/timepit/refined/api/RefType$;
        |       3: invokevirtual #41                 // Method eu/timepit/refined/api/RefType$.tagRefType:()Leu/timepit/refined/api/RefType;
        |       6: iconst_1
        |       7: invokeinterface #26,  2           // InterfaceMethod eu/timepit/refined/api/RefType.unsafeWrap$mIc$sp:(I)Ljava/lang/Object;
        |      12: invokestatic  #47                 // Method scala/runtime/BoxesRunTime.unboxToInt:(Ljava/lang/Object;)I
        |      15: ireturn
        |  public java.lang.Object scalaz_tag();
        |    Code:
        |       0: getstatic     #53                 // Field eu/timepit/refined/scalaz/package$.MODULE$:Leu/timepit/refined/scalaz/package$;
        |       3: invokevirtual #56                 // Method eu/timepit/refined/scalaz/package$.scalazTagRefType:()Leu/timepit/refined/api/RefType;
        |       6: iconst_1
        |       7: invokeinterface #26,  2           // InterfaceMethod eu/timepit/refined/api/RefType.unsafeWrap$mIc$sp:(I)Ljava/lang/Object;
        |      12: areturn
        |  public int unrefined();
        |    Code:
        |       0: iconst_1
        |       1: ireturn
        |  public eu.timepit.refined.scalaz.AnyValTest();
        |    Code:
        |       0: aload_0
        |       1: invokespecial #61                 // Method java/lang/Object."<init>":()V
        |       4: return
        |}
      """.stripMargin.trim
    actual ?= expected
  }

  property("javap AnyRefTest") = secure {
    val actual = javapOutput(new AnyRefTest)
    val expected =
      """Compiled from "RefTypeJavapSpec.scala"
        |public class eu.timepit.refined.scalaz.AnyRefTest {
        |  public java.lang.String Refined();
        |  public java.lang.String shapeless_tag();
        |  public java.lang.Object scalaz_tag();
        |  public java.lang.String unrefined();
        |  public eu.timepit.refined.scalaz.AnyRefTest();
        |}
      """.stripMargin.trim
    actual ?= expected
  }

  property("javap -c AnyRefTest") = secure {
    val actual = javapOutput(new AnyRefTest, "-c")
    val expected =
      """Compiled from "RefTypeJavapSpec.scala"
        |public class eu.timepit.refined.scalaz.AnyRefTest {
        |  public java.lang.String Refined();
        |    Code:
        |       0: getstatic     #16                 // Field eu/timepit/refined/api/RefType$.MODULE$:Leu/timepit/refined/api/RefType$;
        |       3: invokevirtual #20                 // Method eu/timepit/refined/api/RefType$.refinedRefType:()Leu/timepit/refined/api/RefType;
        |       6: ldc           #22                 // String
        |       8: invokeinterface #28,  2           // InterfaceMethod eu/timepit/refined/api/RefType.unsafeWrap:(Ljava/lang/Object;)Ljava/lang/Object;
        |      13: checkcast     #30                 // class eu/timepit/refined/api/Refined
        |      16: invokevirtual #34                 // Method eu/timepit/refined/api/Refined.get:()Ljava/lang/Object;
        |      19: checkcast     #36                 // class java/lang/String
        |      22: areturn
        |  public java.lang.String shapeless_tag();
        |    Code:
        |       0: getstatic     #16                 // Field eu/timepit/refined/api/RefType$.MODULE$:Leu/timepit/refined/api/RefType$;
        |       3: invokevirtual #42                 // Method eu/timepit/refined/api/RefType$.tagRefType:()Leu/timepit/refined/api/RefType;
        |       6: ldc           #22                 // String
        |       8: invokeinterface #28,  2           // InterfaceMethod eu/timepit/refined/api/RefType.unsafeWrap:(Ljava/lang/Object;)Ljava/lang/Object;
        |      13: checkcast     #36                 // class java/lang/String
        |      16: areturn
        |  public java.lang.Object scalaz_tag();
        |    Code:
        |       0: getstatic     #48                 // Field eu/timepit/refined/scalaz/package$.MODULE$:Leu/timepit/refined/scalaz/package$;
        |       3: invokevirtual #51                 // Method eu/timepit/refined/scalaz/package$.scalazTagRefType:()Leu/timepit/refined/api/RefType;
        |       6: ldc           #22                 // String
        |       8: invokeinterface #28,  2           // InterfaceMethod eu/timepit/refined/api/RefType.unsafeWrap:(Ljava/lang/Object;)Ljava/lang/Object;
        |      13: areturn
        |  public java.lang.String unrefined();
        |    Code:
        |       0: ldc           #22                 // String
        |       2: areturn
        |  public eu.timepit.refined.scalaz.AnyRefTest();
        |    Code:
        |       0: aload_0
        |       1: invokespecial #56                 // Method java/lang/Object."<init>":()V
        |       4: return
        |}
      """.stripMargin.trim
    actual ?= expected
  }
}
