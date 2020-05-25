
package edu.holycross.shot.seqcomp


import org.scalatest.FlatSpec

class FeatureMatrixSpec extends FlatSpec {

  val a = Vector("a","b","c","d","f", "g")
  val b = Vector("b","c","d", "e")
  val c = Vector("a", "c", "e", "f", "g")
  val many = Vector(a,b,c)
  val labels = Vector("SCS", "long", "short", "spotty")
  val matrix =  FeatureMatrix( many)

  "A FeatureMatrix" should "have a two-dimensional Vector of type-parameterized Options" in {
    // Test example uses Strings as underlying type:
    matrix.features match {
      case m: Vector[Vector[Option[String]]] => assert(true)
      case _ => fail("Something went wrong: wrong type for matrix.")
    }
  }


  it should "count rows" in {
    // 3 rows of source data plus automatically built SCS
    val expectedRows = 4
    assert(matrix.rows == expectedRows)
  }

  it should "count columns" in {
    // a - g
    val expectedColumns = 7
    assert(matrix.columns == expectedColumns)
  }

  it should "create a new matrix by transposition" in {
    val rotated = matrix.transpose
    assert(rotated.rows == matrix.columns)
    assert(rotated.columns == matrix.rows)
  }

  it should "object if the matrix is not fully populated by Options" in pending

}
