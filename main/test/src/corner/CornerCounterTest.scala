package corner

class CornerCounterTest extends munit.ScalaCheckSuite {
  test("CornerCounter - source") {
    val counter = CornerCounter.fromResource("./tests/Basic.txt")

    assertEquals(counter.positions.size, 16)
  }

  test("CornerCounter - regions") {
    val counter = CornerCounter.fromResource("./tests/Basic.txt")

    assertEquals(counter.regions.size, 5)
  }
}