//> using scala 3.lts
//> using toolkit default
//> using python

import me.shadaj.scalapy.py
import me.shadaj.scalapy.py.*
import py.PyQuote
import java.nio.file.{NoSuchFileException}

object FTPUtil {

  def pario(): String = {
    val parFileName = "FTPUtil.json"
    val parFullPath = os.pwd / parFileName
    try {
      // read parFileName
      val json = ujson.read(os.read(parFullPath))
      ujson.write(json)
    } catch {
      case e: NoSuchFileException => {
        val jsonString = """
          {
            "user": "anonymous",
            "pass": "guest",
            "server": "opendata.dwd.de",
            "local": ".",
            "remote": "climate_environment/CDC/observations_germany/climate/daily/kl"
          }
        """
        val json = ujson.read(jsonString)
        // create local parFileName
        os.write.over(os.pwd / parFileName, ujson.write(json))
        ujson.write(json)
      }
    }
  }

  @main
  def run(): Unit = {
    // Parameter input/output
    val json = ujson.read(pario())

    // Global Scope
    val list = py.Dynamic.global.range(1, 3 + 1)
    val listSum = py.Dynamic.global.sum(list)
    println(s"Global Scope: listSum = ${listSum}")

    py.local { // python local scope
      val user = json("user").str
      val pass = json("pass").str
      val server = json("server").str
      val local = json("local").str
      val remote = json("remote").str

      val os = py.module("os")
      os.chdir(local)
      val ftp = py.module("ftputil")
      py.`with`(ftp.FTPHost(server, user, pass)) { host =>
        host.chdir(remote)
        host.chdir("recent")
        val files = host.listdir(host.curdir) // [11:22].toPythonCopy
        // println(files)
        //files.

        val f = files.bracketAccess(11)
        val d = host.download_if_newer(f, f)
        println(s"... ${d.getClass}")

        //if (d === "False") println("something")
        println(s"downloaded ${f}")
        println(d)

      }
    }
  }
}
