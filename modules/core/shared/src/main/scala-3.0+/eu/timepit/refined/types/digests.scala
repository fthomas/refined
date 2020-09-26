package eu.timepit.refined.types

import eu.timepit.refined.api.{Refined, RefinedTypeOps}
import eu.timepit.refined.boolean.And
import eu.timepit.refined.collection.Size
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.string.HexStringSpec

/** Module for type representing message digests. */
object digests {
  type MD5 = String Refined (HexStringSpec And Size[Equal[32]])
  object MD5 extends RefinedTypeOps[MD5, String]

  type SHA1 = String Refined (HexStringSpec And Size[Equal[40]])
  object SHA1 extends RefinedTypeOps[SHA1, String]

  type SHA224 = String Refined (HexStringSpec And Size[Equal[56]])
  object SHA224 extends RefinedTypeOps[SHA224, String]

  type SHA256 = String Refined (HexStringSpec And Size[Equal[64]])
  object SHA256 extends RefinedTypeOps[SHA256, String]

  type SHA384 = String Refined (HexStringSpec And Size[Equal[96]])
  object SHA384 extends RefinedTypeOps[SHA384, String]

  type SHA512 = String Refined (HexStringSpec And Size[Equal[128]])
  object SHA512 extends RefinedTypeOps[SHA512, String]
}

trait DigestTypes {
  final type MD5 = digests.MD5
  final val MD5 = digests.MD5

  final type SHA1 = digests.SHA1
  final val SHA1 = digests.SHA1

  final type SHA224 = digests.SHA224
  final val SHA224 = digests.SHA224

  final type SHA256 = digests.SHA256
  final val SHA256 = digests.SHA256

  final type SHA384 = digests.SHA384
  final val SHA384 = digests.SHA384

  final type SHA512 = digests.SHA512
  final val SHA512 = digests.SHA512
}
