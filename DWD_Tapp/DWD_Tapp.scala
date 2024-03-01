//> using scala 3.lts
//> using python

import me.shadaj.scalapy.py
import me.shadaj.scalapy.py.SeqConverters
import scala.util.Random
import scala.math.{Pi, sin, random}

object DWD_Tapp {

  @main
  def plot = {
//  def plot = {
    val sequences = generate3DataSeqs

    py.local {
      val plt = py.module("matplotlib.pyplot")

      for {
        (seq, color) <- sequences.zip(Seq("b", "r", "g"))
      } {
        plt.plot(seq.toPythonProxy, color = color)
        plt.show()
      }
    }
  }

  def generate3DataSeqs: Seq[Seq[Double]] = {
    val amplitude = 1.0 // Amplitude of the sine wave
    val numSamples = 1000
    val numSequences = 3
    val noiseAmplitude = 0.2 // Amplitude of noise

    // Generate three sequences with varying numbers of cycles
    val sequences = (1 to numSequences).map { seqIdx =>
      val frequency = seqIdx // Varying frequency for each sequence
      (1 to numSamples).map { sampleIdx =>
        val noise = (random * 2 - 1) * noiseAmplitude // Generate random noise
        val phase = 2 * Pi * frequency * sampleIdx / numSamples
        amplitude * sin(phase) + noise
      }
    }
    sequences
  }
}
/*
import ftputil
import os

from ftputil.error import FTPOSError, PermanentError

USER = "anonymous"
PASS = "guest"
SERVER = "opendata.dwd.de"
LOCAL = "/media/datalake/dwd"
REMOTE = "climate_environment/CDC/observations_germany/climate/daily/kl"
os.chdir(LOCAL)

REC = "recent"
if not os.path.exists(REC):
  os.mkdir(REC)
HIST = "historical"
if not os.path.exists(HIST):
  os.mkdir(HIST)
try:
with ftputil.FTPHost(SERVER, USER, PASS) as host:
  host.chdir(REMOTE)

host.chdir(REC)
os.chdir(REC)
files = host.listdir(host.curdir) #[0:11]
for f in files:
  host.download_if_newer(f, f)
print(f)

host.chdir("../historical")
os.chdir(f"../{HIST}")
files = host.listdir(host.curdir) #[22:33]
for f in files:
  host.download_if_newer(f, f)
print(f)
except FTPOSError as e:
  print(f"{type(e).__name__} at line {e.__traceback__.tb_lineno} of {__file__}: {e}")
except PermanentError as e:
  print(f"{type(e).__name__} at line {e.__traceback__.tb_lineno} of {__file__}: {e}")
except Exception as e:
  print(f"{type(e).__name__} at line {e.__traceback__.tb_lineno} of {__file__}: {e}")
*/