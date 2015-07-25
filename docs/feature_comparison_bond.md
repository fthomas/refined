# Feature comparison between bond and refined

## What bond and refined have in common

* Additional constraints on ordinary types:
    ```scala
    // bond
    def x: Int with GreaterThan[T.`14`.T] = ???

    // refined
    def x: Int @@ Greater[W.`14`.T] = ???
    ```

* Functions to validate runtime values:
    ```scala
    // bond
    scala> GreaterThan(14).validate(16)
    res4: Result[Int with GreaterThan[Int(14)]] = Valid(16)

    // refined
    scala> refineT[Greater[W.`14`.T]](16)
    res0: Either[String, Int @@ Greater[Int(14)]] = Right(16)
    ```
    
* Weaken constraints at compile time:
    ```scala
    // bond
    scala> val x = GreaterThan(14).validate(16).get
    x: Int with net.fwbrasil.bond.GreaterThan[Int(14)] = 16

    scala> GreaterThan(12).lift(x)
    res6: Int with GreaterThan[Int(14)] with GreaterThan[Int(12)] = 16

    scala> val y: Int with GreaterThan[T.`12`.T] = GreaterThan(12).lift(x)
    y: Int with GreaterThan[Int(12)] = 16

    // refined
    scala> val x: Int @@ Greater[W.`14`.T] = 16
    x: Int @@ Greater[Int(14)] = 16

    scala> val y: Int @@ Greater[W.`12`.T] = x
    y: Int @@ Greater[Int(12)] = 16
    ```

 Invalid constraint transformations are compile errors in both libraries.

* User defined constraints

## What bond has but refined not

* The Result type can accumulate validation errors in a for comprehension:
    ```scala
    scala> for {
         |   x <- GreaterThan(12).validate(10)
         |   y <- LesserThan(0).validate(5)
         | } yield (x, y)
    res3: Result[(Int with GreaterThan[Int(12)], Int with LesserThan[Int(0)])] = Invalid(List(Violation(10,GreaterThan(12)), Violation(5,LesserThan(0))))
    ```

## What refined has but bond not

* Check if literals confirm to constraints at compile-time:
    ```scala
    scala> val y: Int @@ Greater[W.`12`.T] = 13
    y: Int @@ Greater[Int(12)] = 13

    scala> val y: Int @@ Greater[W.`12`.T] = 11
    <console>:40: error: Predicate failed: (11 > 12).
           val y: Int @@ Greater[W.`12`.T] = 11
                                             ^
    ```

* Constraints that take other contraints as parameters:
    ```scala
    scala> val y: Char @@ And[Letter, UpperCase] = 'R'
    y: Char @@ And[Letter, UpperCase] = R
    ```
