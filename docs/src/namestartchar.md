http://www.w3.org/TR/xml11/#NT-NameStartChar

```
NameStartChar ::=
    ":"
  | [A-Z]
  | "_"
  | [a-z]
  | [#xC0-#xD6]
  | [#xD8-#xF6]
  | [#xF8-#x2FF]
  | [#x370-#x37D]
  | [#x37F-#x1FFF]
  | [#x200C-#x200D]
  | [#x2070-#x218F]
  | [#x2C00-#x2FEF]
  | [#x3001-#xD7FF]
  | [#xF900-#xFDCF]
  | [#xFDF0-#xFFFD]
  | [#x10000-#xEFFFF]
```

```tut:silent
import eu.timepit.refined._
import eu.timepit.refined.boolean._
import eu.timepit.refined.generic._
import eu.timepit.refined.implicits._
import eu.timepit.refined.numeric._
import shapeless.{ ::, HNil }
import shapeless.tag.@@
```

```tut
type NameStartChar = AnyOf[
     Equal[W.`':'`.T]
  :: Interval[W.`'A'`.T, W.`'Z'`.T]
  :: Equal[W.`'_'`.T]
  :: Interval[W.`'a'`.T, W.`'z'`.T]
  :: Interval[W.`'\u00C0'`.T, W.`'\u00D6'`.T]
  :: Interval[W.`'\u00D8'`.T, W.`'\u00F6'`.T]
  :: Interval[W.`'\u00F8'`.T, W.`'\u02FF'`.T]
  :: Interval[W.`'\u0370'`.T, W.`'\u037D'`.T]
  :: Interval[W.`'\u200C'`.T, W.`'\u200D'`.T]
  :: Interval[W.`'\u2070'`.T, W.`'\u218F'`.T]
  :: Interval[W.`'\u2C00'`.T, W.`'\u2FEF'`.T]
  :: Interval[W.`'\u3001'`.T, W.`'\uD7FF'`.T]
  :: Interval[W.`'\uF900'`.T, W.`'\uFDCF'`.T]
  :: Interval[W.`'\uFDF0'`.T, W.`'\uFFFD'`.T]
  :: HNil]

val a: Char @@ NameStartChar = 'Ä'
```

```tut:fail
val b: Char @@ NameStartChar = ';'
```
