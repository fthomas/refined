package eu.timepit.refined

import scala.annotation.compileTimeOnly
import scala.compiletime.ops.int
import scala.compiletime.ops.long
import scala.compiletime.ops.string.CharAt
import scala.compiletime.ops.string.Length
import scala.compiletime.ops.string.Substring
import scala.language.dynamics

@compileTimeOnly("Illegal reference")
object W extends Dynamic {
  type StringTail[A <: String] = Substring[A, 1, Length[A]]

  type StringLast[A <: String] = CharAt[A, int.-[Length[A], 1]]

  type Literal[A <: String] = CharAt[A, 0] match {
    case '"' =>
      // String
      StringLast[A] match {
        case '"' =>
          Substring[A, 1, int.-[Length[A], 1]]
      }
    case '\'' =>
      // Char
      CharAt[A, 2] match {
        case '\'' =>
          Length[A] match {
            case 3 =>
              CharAt[A, 1]
          }
      }
    case _ =>
      // TODO support another types
      // e.g. Double, Float
      StringLast[A] match {
        case 'L' =>
          // Long
          StringToLong[Substring[A, 0, int.-[Length[A], 1]]]
        case _ =>
          // Int
          long.ToInt[StringToLong[A]]
      }
  }

  type StringToLong[Input <: String] <: Long = CharAt[Input, 0] match {
    case '-' =>
      long.Negate[Loop[StringTail[Input], 0L]]
    case _ =>
      Loop[Input, 0L]
  }

  type CharToLong[C <: Char] <: Long = C match {
    case '0' => 0L
    case '1' => 1L
    case '2' => 2L
    case '3' => 3L
    case '4' => 4L
    case '5' => 5L
    case '6' => 6L
    case '7' => 7L
    case '8' => 8L
    case '9' => 9L
  }

  type Loop[Input <: String, Acc <: Long] <: Long = Length[Input] match {
    case 0 =>
      Acc
    case _ =>
      Loop[
        StringTail[Input],
        long.+[
          long.*[10L, Acc],
          CharToLong[
            CharAt[Input, 0]
          ]
        ]
      ]
  }

  def selectDynamic(
      selector: String
  ): None.type { type T = Literal[selector.type] } =
    None.asInstanceOf[
      None.type { type T = Literal[selector.type] }
    ]
}
