package eu.timepit.refined.pureconfig

private[pureconfig] trait TypeDescribeVersionSpecific {
  final type DescribeType[A] = scala.reflect.runtime.universe.WeakTypeTag[A]

  protected def getDescription[A](d: DescribeType[A]): String = d.tpe.toString
}
