package popup

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, PrintStream}
import java.nio.charset.Charset

import org.scalatest.FunSuite

class ProblemSuite(main: Array[String] => Unit) extends FunSuite {

  private val charset = Charset.defaultCharset()

  def run(in: String): String = {
    val (fakeIn, fakeOut) = (new ByteArrayInputStream(in.getBytes(charset)), new ByteArrayOutputStream())

    val (stdIn, stdOut) = (System.in, System.out)

    System.setIn(fakeIn)
    System.setOut(new PrintStream(fakeOut))

    Console.withIn(fakeIn) {
      Console.withOut(new PrintStream(fakeOut)) {
        main(Array.empty)
      }
    }

    System.setIn(stdIn)
    System.setOut(stdOut)

    val s = new String(fakeOut.toByteArray, charset)
    s.replace("\r\n", "\n")
  }

  def testSample(input: String)(expectedOutput: String): Unit = {
    test(input) {
      val ran = run(input)
      println("Result:")
      println(ran)
      println("Expected:")
      println(expectedOutput)
      assert(ran == expectedOutput)
    }
  }

}
