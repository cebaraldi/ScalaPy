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
    println(s"user: ${json("user").str}")
    println(s"pass: ${json("pass").str}")
    println(s"server: ${json("server").str}")
    println(s"local: ${json("local").str}")
    println(s"remote: ${json("remote").str}")

  }
}
