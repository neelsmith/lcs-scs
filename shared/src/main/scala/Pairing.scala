package edu.holycross.shot.seqcomp

//import scala.scalajs.js
//import scala.scalajs.js.annotation._

// Should ensure that at leats one of left, right is not None
/**
*/
//@JSExportTopLevel("Pairing")
case class Pairing[T] (left: Option[T], right: Option[T])  {

  def cex : String = {
    left.getOrElse("") + "#" + right.getOrElse("")
  }
}
