package popup

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
import java.nio.charset.Charset

import org.scalatest.FunSuite

class ProblemSuite(main: Array[String] => Unit) extends FunSuite {

  private val charset = Charset.defaultCharset()

  private def run(in: String): String = {
    val (fakeIn, fakeOut) = (new ByteArrayInputStream(in.getBytes(charset)), new ByteArrayOutputStream())

    Console.withIn(fakeIn) {
      Console.withOut(new PrintStream(fakeOut)) {
        main(Array.empty)
      }
    }

    new String(fakeOut.toByteArray, charset)
  }

  def testSample(input: String)(expectedOutput: String): Unit = {
    assert(run(input) == expectedOutput)
  }

}
