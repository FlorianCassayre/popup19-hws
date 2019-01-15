package popup.hw1

import popup.ProblemSuite

class FerryLoadingSuite extends ProblemSuite(FerryLoading.main) {

  testSample(
    """2
      |2 10 10
      |0 left
      |10 left
      |20 left
      |30 left
      |40 left
      |50 left
      |60 left
      |70 left
      |80 left
      |90 left
      |2 10 3
      |10 right
      |25 left
      |40 left""".stripMargin)(
    """10
      |30
      |30
      |50
      |50
      |70
      |70
      |90
      |90
      |110
      |
      |30
      |40
      |60
      |""".stripMargin)

}
