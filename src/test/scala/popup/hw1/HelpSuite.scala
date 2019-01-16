package popup.hw1

import popup.ProblemSuite

class HelpSuite extends ProblemSuite(Help.main) {

  testSample(
    """3
      |how now brown <animal>
      |<foo> now <color> cow
      |who are you
      |<a> <b> <a>
      |<a> b
      |c <a>
      |""".stripMargin)(
    """how now brown cow
      |-
      |c b
      |""".stripMargin)

  testSample(
    """1
      |<a> abc <b> hij <c> klm <e>
      |<a> <b> def hij <d> <e> <e>
      |""".stripMargin)(
    """* abc def hij * klm klm
      |""".stripMargin)

}
