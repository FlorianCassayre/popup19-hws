package popup.hw1

import popup.ProblemSuite

class CardTrickSuite extends ProblemSuite(JCardTrick.main) {

  testSample("1\n1\n")("1\n")

  testSample("1\n2\n")("2 1\n")

  testSample("1\n3\n")("3 1 2\n")

  testSample("2\n4\n5\n")("2 1 4 3\n3 1 4 5 2\n")

}
