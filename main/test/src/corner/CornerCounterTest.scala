package corner

class CornerCounterTest extends munit.ScalaCheckSuite {
  test("CornerCounter - source") {
    val counter = CornerCounter.fromResource("./tests/Basic.txt")

    assertEquals(counter.positions.size, 16)
    assertEquals(counter.regions.size, 5)
  }

  test("CornerCounter - source - with free space") {
    val counter = CornerCounter.fromResource("./tests/Basic-WithFreeSpace.txt")

    assertEquals(counter.positions.size, 17)
    assertEquals(counter.regions.size, 5)
  }
}