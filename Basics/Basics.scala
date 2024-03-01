// Activate Python environment w/ numpy
// [ScalaPy](https://scalapy.dev/docs/interacting-with-python)
//> using scala 3.lts
//> using python

import me.shadaj.scalapy.py

import me.shadaj.scalapy.py.SeqConverters
import py.PyQuote

// object BasicsDemo {

@main
def Basics = {

  /*
   *  Interacting with Python
   */
  println("\n// Global Scope")

  // Python ranges are exclusive
  val list = py.Dynamic.global.range(1, 3 + 1)
  println(s"list: ${list.getClass} = ${list}")

  // 1 + 2 + 3 == 6
  val listSum = py.Dynamic.global.sum(list)
  println(s"listSum: ${listSum.getClass} = ${listSum}")

  println("\n// Importing Modules")
  val np = py.module("numpy")
  println(s"np: ${np.getClass} = ${np}")

  val a = np.array(
    Seq(
      Seq(1, 0),
      Seq(0, 12)
    ).toPythonProxy
  )
  println(s"a: ${a.getClass} = ${a}")

  val aSquared = np.matmul(a, a)
  println(s"aSquared: ${aSquared.getClass} = ${aSquared}")

  println("\n// Scala-Python Conversions")
  val mySeqToCopy = Seq(Seq(1, 2), Seq(3))
  println(s"mySeqToCopy: ${mySeqToCopy.getClass} = ${mySeqToCopy}")
  // mySeqToCopy: Seq[Seq[Int]] = List(List(1, 2), List(3))
  mySeqToCopy.toPythonCopy

  val mySeqToProxy = Array(1, 2, 3)
  println(s"mySeqToProxy: ${mySeqToProxy.getClass} = ${mySeqToProxy}")
  val myProxy = mySeqToProxy.toPythonProxy
  println(s"myProxy: ${myProxy.getClass} = ${myProxy}")
  println(py.Dynamic.global.list(myProxy))
  mySeqToProxy(2) = 100
  println(py.Dynamic.global.list(myProxy))

  import scala.collection.mutable
  val myPythonList = py.Dynamic.global.list(py.Dynamic.global.range(10))
  println(s"myPythonList: ${myPythonList.getClass} = ${myPythonList}")
  val copyLoad = myPythonList.as[Vector[Int]]
  println(s"copyLoad: ${copyLoad.getClass} = ${copyLoad}")
  val proxyLoad = myPythonList.as[mutable.Seq[Int]]
  println(s"proxyLoad: ${proxyLoad.getClass} = ${proxyLoad}")

  println(copyLoad)
  println(proxyLoad)

  myPythonList.bracketUpdate(0, 100)

  println(copyLoad)
  println(proxyLoad)

  proxyLoad(0) = 200
  println(myPythonList)

  println("\n// Custom Python Snippets")
  import py.PyQuote

  val mappedList = py.Dynamic.global.list(
    py"map(lambda elem: elem + 1, ${Seq(1, 2, 3).toPythonProxy})"
  )
  println(s"mappedList: ${mappedList.getClass} = ${mappedList}")

  println("\n// Special Python Syntax")
  println("py.with")
  val myFile = py.Dynamic.global.open("../README.md")
  println(s"myFile: ${myFile.getClass} = ${myFile}")
  py.`with`(myFile) { file =>
    println(file.encoding.as[String])
  }
  // UTF-8
  println("bracketAccess, bracketUpdate, and bracketDelete")
  val pythonList = py.Dynamic.global.list(Seq(1, 2, 3).toPythonProxy)
  println(s"pythonList: ${pythonList.getClass} = ${pythonList}")
  println(pythonList)
  // [1, 2, 3]
  pythonList.bracketAccess(0)
  // res14: py.Dynamic = 1
  pythonList.bracketUpdate(1, 100)
  println(pythonList)
  // [1, 100, 3]

  val myDict = py.Dynamic.global.dict()
  // myDict: py.Dynamic = {}
  myDict.bracketUpdate("hello", "world")
  println(myDict)
  // {'hello': 'world'}
  myDict.bracketDelete("hello")
  println(myDict)
  // {}

  println("attrDelete")
  import me.shadaj.scalapy.interpreter.CPythonInterpreter
  CPythonInterpreter.execManyLines(
    """class MyClass:
      |  myAttribute = 0""".stripMargin
  )
  py.Dynamic.global.MyClass.attrDelete("myAttribute")

  //  println(".del()")
  //  val myValue = py.Dynamic.global.MyClass()
  //  myValue.del()
  //  println(myValue)

  println("\n// Zoned Memory Management")
  py.local {
    (1 to 100).foreach { _ =>
      py.Dynamic.global.len(Seq(1, 2, 3).toPythonCopy)
    }
  }
}
// }
