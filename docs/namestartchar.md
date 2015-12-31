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

```scala
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.boolean.AnyOf
import eu.timepit.refined.generic.Equal
import eu.timepit.refined.numeric.Interval
import shapeless.{ ::, HNil }
```

```scala
scala> type NameStartChar = Char Refined AnyOf[
     |      Equal[W.`':'`.T]
     |   :: Interval.Closed[W.`'A'`.T, W.`'Z'`.T]
     |   :: Equal[W.`'_'`.T]
     |   :: Interval.Closed[W.`'a'`.T, W.`'z'`.T]
     |   :: Interval.Closed[W.`'\u00C0'`.T, W.`'\u00D6'`.T]
     |   :: Interval.Closed[W.`'\u00D8'`.T, W.`'\u00F6'`.T]
     |   :: Interval.Closed[W.`'\u00F8'`.T, W.`'\u02FF'`.T]
     |   :: Interval.Closed[W.`'\u0370'`.T, W.`'\u037D'`.T]
     |   :: Interval.Closed[W.`'\u200C'`.T, W.`'\u200D'`.T]
     |   :: Interval.Closed[W.`'\u2070'`.T, W.`'\u218F'`.T]
     |   :: Interval.Closed[W.`'\u2C00'`.T, W.`'\u2FEF'`.T]
     |   :: Interval.Closed[W.`'\u3001'`.T, W.`'\uD7FF'`.T]
     |   :: Interval.Closed[W.`'\uF900'`.T, W.`'\uFDCF'`.T]
     |   :: Interval.Closed[W.`'\uFDF0'`.T, W.`'\uFFFD'`.T]
     |   :: HNil]
defined type alias NameStartChar

scala> val a: NameStartChar = 'Ä'
a: NameStartChar = Refined(Ä)
```

```scala
scala> val b: NameStartChar = ';'
<console>:21: error: Predicate failed: ((; == :) || (!(; < A) && !(; > Z)) || (; == _) || (!(; < a) && !(; > z)) || (!(; < À) && !(; > Ö)) || (!(; < Ø) && !(; > ö)) || (!(; < ø) && !(; > ˿)) || (!(; < Ͱ) && !(; > ͽ)) || (!(; < ‌) && !(; > ‍)) || (!(; < ⁰) && !(; > ↏)) || (!(; < Ⰰ) && !(; > ⿯)) || (!(; < 、) && !(; > ퟿)) || (!(; < 豈) && !(; > ﷏)) || (!(; < ﷰ) && !(; > �)) || false).
       val b: NameStartChar = ';'
                              ^
```
