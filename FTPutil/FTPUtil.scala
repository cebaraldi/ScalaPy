//> using scala 3.lts
//> using toolkit default
//> using python

import me.shadaj.scalapy.py
import me.shadaj.scalapy.py.*
import py.PyQuote
import java.nio.file.{NoSuchFileException}

// SEE: https://scalapy.dev/

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
//    val list = py.Dynamic.global.range(1, 3 + 1)
//    val listSum = py.Dynamic.global.sum(list)
//    println(s"Global Scope: listSum = ${listSum}")
    py"print('Hello from Python!')"

    py.local { // python local scope
      val user = json("user").str
      val pass = json("pass").str
      val server = json("server").str
      val local = json("local").str
      val remote = json("remote").str

      // import Python module os
      val os = py.module("os")

      // import Python module ftputil
      val ftp = py.module("ftputil")
//      SEE https://techbuddy.dev/scala-python-integration-datascience
//      val ftp = py.Dynamic.global.selectDynamic("ftputil") NOT WORKING!

      py.`with`(ftp.FTPHost(server, user, pass)) { host =>
        host.chdir(remote)
        host.chdir("recent")
        val files = host.listdir(host.curdir)
        // println(files)

        // FTP download files from remote to local
        os.chdir(local)
        var count = 0
        for (i <- 0 until py.Dynamic.global.len(files).as[Int]) {
          val f = files.bracketAccess(i)
          val downloaded = host.download_if_newer(f, f).as[Boolean]
          if (downloaded) {
            println(s"${f} newly downloaded")
            count += 1
          }
        }
        println(s"In total ${count} files downloaded.")

      }
    }
  }
}
