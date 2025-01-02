package corner

class CornerCounterTest extends munit.ScalaCheckSuite {
  test("CornerCounter - source") {
    val counter = CornerCounter.fromResource("./tests/Basic.txt")

    assertEquals(counter.regions.size, 16)
  }
}