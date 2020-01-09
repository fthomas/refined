package eu.timepit.refined.cats

/**
 * Typeclass to shift Negative values to Non Negative values.
 */
private[cats] trait NonNegShift[T] extends Serializable {
  def shift(t: T): T
}

private[cats] object NonNegShift {
  def apply[T](implicit ev: NonNegShift[T]): NonNegShift[T] = ev

  def instance[T](function: T => T): NonNegShift[T] = new NonNegShift[T] {
    def shift(t: T): T = function(t)
  }

  // Instances
  implicit val byteNonNegShift: NonNegShift[Byte] = instance(t => (t & Byte.MaxValue).toByte)
  implicit val shortNonNegShift: NonNegShift[Short] = instance(t => (t & Short.MaxValue).toShort)
  implicit val intNonNegShift: NonNegShift[Int] = instance(t => t & Int.MaxValue)
  implicit val longNonNegShift: NonNegShift[Long] = instance(t => t & Long.MaxValue)
}

/**
 * Typeclass to shift Positive values to Negative values.
 */
private[cats] trait NegShift[T] extends Serializable {
  def shift(t: T): T
}

private[cats] object NegShift {
  def apply[T](implicit ev: NegShift[T]): NegShift[T] = ev

  def instance[T](function: T => T): NegShift[T] = new NegShift[T] {
    def shift(t: T): T = function(t)
  }

  // Instances
  implicit val byteNegShift: NegShift[Byte] = instance(t => (t | Byte.MinValue).toByte)
  implicit val shortNegShift: NegShift[Short] = instance(t => (t | Short.MinValue).toShort)
  implicit val intNegShift: NegShift[Int] = instance(t => t | Int.MinValue)
  implicit val longNegShift: NegShift[Long] = instance(t => t | Long.MinValue)
}
