## Statically checked regexes, URLs, XML and more

The combination of compile-time validation and implicit conversions to
refined types allows to build statically checking constructors of types
that can be instantiated with literals. One common example are regular
expressions that are often built of constant string literals. But not
all strings are valid regexes, therefore the `scala.util.matching.Regex`
constructor checks at runtime if a given string is a valid regex:

```scala
scala> "(a|b)".r // succeeds at runtime
res0: scala.util.matching.Regex = (a|b)

scala> "(a|b".r // fails at runtime
java.util.regex.PatternSyntaxException: Unclosed group near index 4
(a|b
    ^
  at java.util.regex.Pattern.error(Pattern.java:1955)
  at java.util.regex.Pattern.accept(Pattern.java:1813)
  at java.util.regex.Pattern.group0(Pattern.java:2908)
  at java.util.regex.Pattern.sequence(Pattern.java:2051)
  at java.util.regex.Pattern.expr(Pattern.java:1996)
  at java.util.regex.Pattern.compile(Pattern.java:1696)
  at java.util.regex.Pattern.<init>(Pattern.java:1351)
  at java.util.regex.Pattern.compile(Pattern.java:1028)
  at scala.util.matching.Regex.<init>(Regex.scala:223)
  at scala.collection.immutable.StringLike.r(StringLike.scala:281)
  at scala.collection.immutable.StringLike.r$(StringLike.scala:281)
  at scala.collection.immutable.StringOps.r(StringOps.scala:29)
  at scala.collection.immutable.StringLike.r(StringLike.scala:270)
  at scala.collection.immutable.StringLike.r$(StringLike.scala:270)
  at scala.collection.immutable.StringOps.r(StringOps.scala:29)
  ... 118 elided
```

The library provides its own constructor for regexes in the `util.string`
object. Together with an implicit conversion macro in the `auto` object,
these constructors check at compile-time if a given string literal is a
valid regex. That means that those constructors will never throw an
exception at runtime.

```scala
import eu.timepit.refined.auto._
import eu.timepit.refined.util.string._
```
```scala
scala> regex("(a|b)") // succeeds at compile- and runtime
res2: scala.util.matching.Regex = (a|b)

scala> regex("(a|b") // fails at compile-time
<console>:19: error: Regex predicate failed: Unclosed group near index 4
(a|b
    ^
       regex("(a|b") // fails at compile-time
             ^
```

There are also similar constructors for
* `java.net.URI`
* `java.net.URL`
* `java.util.UUID`
* `javax.xml.xpath.XPathExpression`
* `scala.xml.Elem`

```scala
scala> uri("/valid/path")
res4: java.net.URI = /valid/path

scala> uri("/path/ with/space")
<console>:19: error: Uri predicate failed: Illegal character in path at index 6: /path/ with/space
       uri("/path/ with/space")
           ^

scala> url("http://scala-lang.org/")
res6: java.net.URL = http://scala-lang.org/

scala> url("htp://example.com")
<console>:19: error: Url predicate failed: unknown protocol: htp
       url("htp://example.com")
           ^

scala> uuid("9ecce884-47fe-4ba4-a1bb-1a3d71ed6530")
res8: java.util.UUID = 9ecce884-47fe-4ba4-a1bb-1a3d71ed6530

scala> uuid("whops")
<console>:19: error: Uuid predicate failed: Invalid UUID string: whops
       uuid("whops")
            ^

scala> xml("<a>link</a>")
res10: scala.xml.Elem = <a>link</a>

scala> xml("<a>link</a")
<console>:19: error: Xml predicate failed: XML document structures must start and end within the same entity.
       xml("<a>link</a")
           ^

scala> xpath("A//B/*[1]").isInstanceOf[javax.xml.xpath.XPathExpression]
res12: Boolean = true

scala> xpath("A//B/*[1")
<console>:19: error: XPath predicate failed: javax.xml.transform.TransformerException: Expected ], but found:
       xpath("A//B/*[1")
             ^
```

Manage interoperability with impure code to safely handle `null`. `NonNull[P]` wraps a predicate `P` and validates if the value is defined.

```scala
import java.net.URL                          
import eu.timepit.refined.api.Refined
import eu.timepit.refined.impure._
import eu.timepit.refined.refineV
import eu.timepit.refined.collection.NonEmpty

scala> val url = new URL("https://www.google.com")
<console>url: java.net.URL = https://www.google.com
scala> val unsafe = url.getUserInfo
<console>unsafe: String = null
scala> val refined = refineV[NonNull[NonEmpty]](unsafe).toOption
<console>refined: Option[Refined[String,NonNull[NonEmpty]]] = None

```

