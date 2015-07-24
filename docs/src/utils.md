## Statically checked regexes, URIs, URLs and more

The combination of compile-time validation and implicit conversions to
refined types allows to build statically checking constructors of types
that can be instantiated with literals. One common example are regular
expressions that are often built of constant string literals. But not
all strings are valid regexes, therefore the `scala.util.matching.Regex`
constructor checks at runtime if a given string is a valid regex:

```tut:nofail
"(a|b)".r // succeeds at runtime

"(a|b".r // fails at runtime
```

The library provides its own constructor for regexes in the `util.string`
object. Together with an implicit conversion macro in the `implicits`
object, these constructors check at compile-time if a given string literal
is a valid regex. That means that those constructors will never throw an
exception at runtime.

```tut:silent
import eu.timepit.refined.implicits._
import eu.timepit.refined.util.string._
```
```tut:nofail
regex("(a|b)") // succeeds at compile- and runtime

regex("(a|b") // fails at compile-time
```

There are also similar constructors for `java.net.URI`, `java.net.URL` and
`java.util.UUID`:
```tut:nofail
uri("/valid/path")
uri("/path/ with/space")

url("http://scala-lang.org/")
url("htp://example.com")

uuid("9ecce884-47fe-4ba4-a1bb-1a3d71ed6530")
uuid("whops")
```
