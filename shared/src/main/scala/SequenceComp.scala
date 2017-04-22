package edu.holycross.shot.seqcomp
import scala.scalajs.js
import js.annotation.JSExport
import scala.collection.mutable.ArrayBuffer
import scala.annotation.tailrec

/** A class for comparing pairs of vectors.
*
* @param v1 First Vector to compare.
* @param v2 Second Vector to compare.
*/
@JSExport class SequenceComp[T] (val v1: Vector[T], val v2: Vector[T])  {

  /** Create a memoizing array by comparing each pair of elements in [[v1]] and [[v2]]
  * and saving the resulting counts of the length of the common Vector (or lcs) at that point.
  */
  def memo: Array[Array[Int]] = {
    val memoized = Array.ofDim[Int](v1.size + 1, v2.size + 1)
    for {
        i <- v1.size - 1 to 0 by -1
        j <- v2.size - 1 to 0 by -1
      } {
       if (v1(i) == v2(j)) {
        memoized(i)(j)= memoized(i+1)(j+1) + 1
       } else {
         memoized(i)(j) = math.max(memoized(i+1)(j), memoized(i)(j+1))
       }
    }
    memoized
  }

  /** Compute Longest Common Subsequence for two Vectors of objects by
  * walking back through the memoizing array to recover the common values in
  * each of the two Vectors.
  *
  * Compare the textbook discussion at http://introcs.cs.princeton.edu/java/23recursion/
  * with accompanying java impementation at http://introcs.cs.princeton.edu/java/23recursion/LongestCommonSubsequence.java.html
  */
  def  lcs : Vector[T] = {
    var common = ArrayBuffer[T]()
    var i1 = 0
    var i2 = 0
    while ((i1 < v1.size) && (i2 < v2.size)) {
      if (v1(i1) == v2(i2)) {
        common  += v1(i1)
        i1 = i1 + 1
        i2 = i2 + 1
      } else {
        if(memo(i1 + 1)(i2) >= memo(i1)(i2 + 1)) {
          i1 = i1 + 1
        } else {
          i2 = i2 + 1
        }
      }
    }
    common.toVector
  }


  /** Recursively insert unique elements from a pair of vectors into a vector of their
  * common or overlapping elements.  This is equivalent to solving the SCS problem for
  * a pair of Vectors by inserting elements into the LCS solution for same Vectors.
  * Initial elements of the source vectors and the common vector are compared to decide
  * whether or what to add to the composite mashup, then the process is repeated recursively
  * until no elements remain in the overlap vector.  Finally, any remaining elements in `src1` and `src2`
  * are added to the mashup with the convention that `src1` precedes `src2`.  This means
  * that reversing the order of `src1` and `src2` might or might not produce
  * identical results.
  *
  * @param src1 First vector to examine.
  * @param src2 Second vector to examine.
  * @param overlap Common vector (LCS of src1 and src2).
  * @param mashup Accumulator with previously inserted elements.
  */
  @tailrec final def  insertSingles(src1: Vector[T], src2: Vector[T], overlap: Vector[T], mashup: ArrayBuffer[T]) : Vector[T] = {
    if (overlap.size == 0){
      mashup.toVector ++ src1 ++ src2

    } else {
      if ((src1(0) == overlap(0)) && (src2(0) == overlap(0))) {
        // common to both. Add first element to mashup, and
        // remove first element from all vectors.
        val mashed = mashup += overlap(0)
        if (overlap.size == 1) {
          mashed.toVector ++ src1.drop(1) ++ src2.drop(1)
        } else {
          insertSingles(src1.drop(1), src2.drop(1),overlap.drop(1), mashed)
        }

      } else if (src1(0)== overlap(0)){
        // so element missing from src2, add that to mashup
        val mashed = mashup += src2(0)
        if (overlap.size == 1) {
          mashed.toVector ++ src1.drop(1) ++ src2.drop(1)
        } else {
          insertSingles(src1, src2.drop(1),overlap, mashed)
        }

      } else {
        // then missing src1, so add it to mashup
        val mashed = mashup += src1(0)
        if (overlap.size == 1) {
          mashed.toVector ++ src1.drop(1) ++ src2.drop(1)
        } else {
          insertSingles(src1.drop(1), src2,overlap, mashed)
        }
      }
    }
  }

  /** Compute Shortest Common Supersequence for [[v1]] and [[v2]] by
  * finding LCS and inserting into it at the approrpiate point accompanying
  * any elements of [[v1]] and [[v2]] that appear only in one Vector.
  */
  def  scs: Vector[T] = {
    insertSingles(v1,v2, lcs,ArrayBuffer[T]())
  }

  /** Print to stdout a display of a specified row
  * of the memoizing array.  Used by [[printMemo]].
  *
  * @param i Index of row to print out.
  */
  def printRow(i: Integer): Unit = {
    print(v1(i) + "=>")
    for (j <- 0 to v2.size - 1) {
     print (v2(j) + ":" + memo(i)(j) + ", ")
    }
    println(memo(i)(v2.size))
  }

  /** Print to stdout a display of the memoizing array.
  * Useful for teaching and explaining how the LCS algorithm works.
  */
  def printMemo: Unit = {
    for (i <- 0 to v1.size - 1) {
      printRow(i)
    }
    print(" =>")
    for (j <- 0 to v2.size - 1) { print(" :" + memo(v1.size)(j) + ", ") }
    println(memo(v1.size)(v2.size))
  }

}



/** Factory for making [[SequenceComp]] objects directly from
* pairs of Vectors.
*/
object SequenceComp {

  /** Create a [[SequenceComp]] from two Vectors.
  *
  * @param v1 First Vector.
  * @param v2 Second Vector.
  */
  def apply[T] (v1: Vector[T],  v2: Vector[T] ) = {
    new SequenceComp(v1, v2)
  }

}
