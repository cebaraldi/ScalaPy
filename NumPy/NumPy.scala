// Activate Python environment w/ numpy
//> using jvm 17
//> using scala 3.lts
//> using python

import me.shadaj.scalapy.py
import me.shadaj.scalapy.py.SeqConverters
import py.PyQuote

object NumPyDemo {

  @main
  def NumPy = {

    val numbers =
      py"[x * 2 for x in ${Iterator.from(3).take(10).toList.toPythonCopy}]"
        .as[Seq[Int]]
    println(numbers)

    py.local {
      val np = py.module("numpy")
      val rng = np.random.default_rng()
      val randoms = rng.standard_normal(10).as[Seq[Double]]
      randoms.foreach(println(_))
    }
  }
}
