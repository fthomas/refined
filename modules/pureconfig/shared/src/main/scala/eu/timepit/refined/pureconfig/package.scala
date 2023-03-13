package eu.timepit.refined

import _root_.pureconfig.ConfigConvert
import eu.timepit.refined.api.{RefType, Validate}

package object pureconfig extends BaseInstances {

  implicit def refTypeConfigConvert[F[_, _], T, P](implicit
      configConvert: ConfigConvert[T],
      refType: RefType[F],
      validate: Validate[T, P],
      typeTag: DescribeType[F[T, P]]
  ): ConfigConvert[F[T, P]] =
    ConfigConvert.fromReaderAndWriter(refTypeConfigReader, refTypeConfigWriter)
}
