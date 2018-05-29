# Macro pitfalls

```scala
import eu.timepit.refined._
import eu.timepit.refined.api.Validate
```

When using the `refineMT` or `refineMV` macros with custom predicates
make sure to define the predicate and its `Validate` instance in a
different compilation unit. Otherwise macro expansion will fail because
it tries to evaluate an uninitialized object as you can see below:

```scala
scala> object Test1 {
     |   case class Foo()
     | 
     |   implicit def fooValidate[T]: Validate[T, Foo] =
     |     Validate.fromPredicate(_ => true, x => s"$x = Foo", Foo())
     | 
     |   val x = refineMV[Foo](0)
     | }
<console>:22: error: exception during macro expansion:
java.lang.ClassNotFoundException: Test1$
	at scala.reflect.internal.util.AbstractFileClassLoader.findClass(AbstractFileClassLoader.scala:64)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:424)
	at java.lang.ClassLoader.loadClass(ClassLoader.java:357)
	at __wrapper$3$f38a6a622e29419f821a3636081863ce.__wrapper$3$f38a6a622e29419f821a3636081863ce$.wrapper(<no source file>:22)

         val x = refineMV[Foo](0)
                              ^
```

Using the macro in a different compilation unit is fine:

```scala
scala> object Test1 {
     |   case class Foo()
     | 
     |   implicit def fooValidate[T]: Validate[T, Foo] =
     |     Validate.fromPredicate(_ => true, x => s"$x = Foo", Foo())
     | }
defined object Test1

scala> object Test2 {
     |   val x = refineMV[Test1.Foo](0)
     | }
defined object Test2
```
