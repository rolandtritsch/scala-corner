package corner

class CornerCounterTest extends munit.ScalaCheckSuite {
  test("CornerCounter - source") {
    val counter = CornerCounter.fromResource("./tests/Basic.txt")

    assertEquals(counter.positions.size, 16)
    assertEquals(counter.positions.size, counter.corners.size)
    assertEquals(counter.regions.size, 5)
  }

  test("CornerCounter - source - with free space") {
    val counter = CornerCounter.fromResource("./tests/Basic-WithFreeSpace.txt")

    assertEquals(counter.positions.size, 17)
    assertEquals(counter.positions.size, counter.corners.size)
    assertEquals(counter.regions.size, 5)
  }

  test("CornerCounter - single cell") {
    val counter = CornerCounter.fromResource("./tests/SingleCell.txt")

    assertEquals(counter.positions.size, 1)
    assertEquals(counter.positions.size, counter.corners.size)
    assertEquals(counter.regions.size, 1)
    assertEquals(counter.corners((1, 1)), 4)
  }

  test("CornerCounter - double cell") {
    val counter = CornerCounter.fromResource("./tests/DoubleCell.txt")

    assertEquals(counter.positions.size, 2)
    assertEquals(counter.positions.size, counter.corners.size)
    assertEquals(counter.regions.size, 1)
    assertEquals(counter.corners((1, 1)), 2)
    assertEquals(counter.corners((2, 1)), 2)
  }
}