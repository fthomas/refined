package eu.timepit.refined.play

import eu.timepit.refined.TestUtils._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric.Positive
import org.scalacheck.Prop._
import org.scalacheck.Properties
import _root_.play.api.libs.json._
import shapeless.test.illTyped

class PlayJsonSpec extends Properties("PlayReadsWrites") {
  type PosInt = Int Refined Positive

  property("reads success") = secure {
    Json.fromJson[PosInt](JsNumber(10)) ?= JsSuccess(10: PosInt)
  }

  property("reads failure") = secure {
    Json.fromJson[PosInt](JsNumber(-42)) ?= JsError("Predicate failed: (-42 > 0).")
  }

  property("writes success") = secure {
    Json.toJson(10: PosInt) ?= JsNumber(10)
  }

  property("writes failure") = wellTyped {
    illTyped("""Json.toJson[PosInt](-10)""", """Predicate failed.*""")
  }
}
