package popup.hw1

import scala.io.StdIn._

object CardTrick extends App {

  val t = readInt()
  val ns = Seq.fill(t)(readInt())

  ns.foreach{ n =>

    def buildDeck(k: Int, acc: Seq[Int]): Seq[Int] = {
      if(k > 0) {
        val added = k +: acc
        val (l, r) = added.splitAt(added.size - (k % added.size))
        val shuffled = r ++ l
        buildDeck(k - 1, shuffled)
      } else {
        acc
      }
    }

    println(buildDeck(n, Seq.empty).mkString(" "))
  }

}
