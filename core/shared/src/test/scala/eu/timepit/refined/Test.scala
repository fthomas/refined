package eu.timepit.refined

object Test {
  val c = Concat[W.`"abc"`.T, W.`"xyz"`.T]

  val x: c.Out = "abcxyz"

  // val y: c.Out = "xyzabc"
  //  [error]  found   : String("xyzabc")
  //  [error]  required: eu.timepit.refined.Test.c.Out
  //  [error]     (which expands to)  String("abcxyz")
  //  [error]   val y: c.Out = "xyzabc"
  //  [error]                  ^

}

