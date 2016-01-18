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
  at java.util.regex.Pattern.error(Pattern.java:1924)
  at java.util.regex.Pattern.accept(Pattern.java:1782)
  at java.util.regex.Pattern.group0(Pattern.java:2857)
  at java.util.regex.Pattern.sequence(Pattern.java:2018)
  at java.util.regex.Pattern.expr(Pattern.java:1964)
  at java.util.regex.Pattern.compile(Pattern.java:1665)
  at java.util.regex.Pattern.<init>(Pattern.java:1337)
  at java.util.regex.Pattern.compile(Pattern.java:1022)
  at scala.util.matching.Regex.<init>(Regex.scala:191)
  at scala.collection.immutable.StringLike$class.r(StringLike.scala:255)
  at scala.collection.immutable.StringOps.r(StringOps.scala:30)
  at scala.collection.immutable.StringLike$class.r(StringLike.scala:244)
  at scala.collection.immutable.StringOps.r(StringOps.scala:30)
  ... 190 elided
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
<console>:18: error: Regex predicate failed: Unclosed group near index 4
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
<console>:18: error: Uri predicate failed: Illegal character in path at index 6: /path/ with/space
       uri("/path/ with/space")
           ^

scala> url("http://scala-lang.org/")
res6: java.net.URL = http://scala-lang.org/

scala> url("htp://example.com")
<console>:18: error: Url predicate failed: unknown protocol: htp
       url("htp://example.com")
           ^

scala> uuid("9ecce884-47fe-4ba4-a1bb-1a3d71ed6530")
res8: java.util.UUID = 9ecce884-47fe-4ba4-a1bb-1a3d71ed6530

scala> uuid("whops")
<console>:18: error: Uuid predicate failed: Invalid UUID string: whops
       uuid("whops")
            ^

scala> xml("<a>link</a>")
res10: scala.xml.Elem = <a>link</a>

scala> xml("<a>link</a")
<console>:18: error: Xml predicate failed: XML document structures must start and end within the same entity.
xml("<a>link</a")
    ^

scala> xpath("A//B/*[1]").isInstanceOf[javax.xml.xpath.XPathExpression]
res12: Boolean = true

scala> xpath("A//B/*[1")
<console>:18: error: XPath predicate failed: javax.xml.transform.TransformerException: Expected ], but found:
       xpath("A//B/*[1")
             ^
```
