package eu.timepit.refined.cats

/*

Problems of the current encoding of predicates and validation results:

- Parameters passed to predicates are unconstrained which makes it easy
  to write nonsensical code
  https://github.com/fthomas/refined/issues/506

- no public API for "result as data"


 */

object Test1 {
  // the current encoding

  final case class Positive()

  final case class Even()

  final case class Not[P](p: P)

  final case class And[A, B](a: A, b: B)

  final case class Forall[P](p: P)

  case class Result()

  case class WithResult[P](p: P, result: Result)

  val x1: WithResult[Positive] = WithResult(Positive(), Result())

  val x2: WithResult[Not[WithResult[Even]]] =
    WithResult(Not(WithResult(Even(), Result())), Result())

  val x3: WithResult[And[WithResult[Positive], WithResult[Not[WithResult[Even]]]]] =
    WithResult(And(x1, x2), Result())

}

object Test2 {
  // a simple marker trait simplifies the type of x2 and x3

  trait Predicate

  final case class Positive() extends Predicate

  final case class Even() extends Predicate

  final case class Not[P](p: P) extends Predicate

  final case class And[A, B](a: A, b: B) extends Predicate

  case class Result()

  case class WithResult[P](p: P, result: Result)

  val x1: WithResult[Predicate] = WithResult(Positive(), Result())

  val x2: WithResult[Predicate] = WithResult(Not(WithResult(Even(), Result())), Result())

  val x3: WithResult[Predicate] = WithResult(And(x1, x2), Result())

}

object Test3 {

  trait PredicateF[A]

  final case class Positive[A]() extends PredicateF[A]

  final case class Even[A]() extends PredicateF[A]

  final case class Not[P](p: P) extends PredicateF[P]

  final case class And[A, B](a: A, b: B) extends PredicateF[A]

  case class Result()

  val x: And[Positive[Nothing], Not[Even[Nothing]]] = And(Positive(), Not(Even()))

  case class Cofree[A, F[_]](a: A, fa: F[Cofree[A, F]])

  val r = Result()
  val z1: Cofree[Result, PredicateF] = Cofree(r, Positive())
  val z2: Cofree[Result, PredicateF] = Cofree(r, Not(z1))
  val z4: Cofree[Result, PredicateF] = Cofree(r, Even())
  val z3: Cofree[Result, PredicateF] = Cofree(r, And(z2, z4))

  val z5: Cofree[Result, PredicateF] = Cofree(r, And(Cofree(r, Not(z1)), z4))

  //Cofree(Result(), And(Cofree(Result(), Positive()), ???))

}
