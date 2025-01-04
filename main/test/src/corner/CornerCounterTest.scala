package corner
class CornerCounterTest extends munit.ScalaCheckSuite {

  test("CornerCounter - basic") {
    val counter = CornerCounter.fromResource("./tests/Basic.txt")
    val expected = CornerCounter.fromResourceExpected("./tests/Basic_Corners.txt")

    assertEquals(counter.positions.size, 16)
    assertEquals(counter.positions.size, counter.corners.size)
    assertEquals(counter.regions.size, 5)
    assertEquals(counter.corners.toSet, expected)

    //import corner.CornerCounter.given 
    //println(counter.toStringPrettyGrid)
    //println(counter.toStringPrettyCorners)
  }

  test("CornerCounter - basic - with free space") {
    val counter = CornerCounter.fromResource("./tests/Basic-WithFreeSpace.txt")
    val expected = CornerCounter.fromResourceExpected("./tests/Basic-WithFreeSpace_Corners.txt")

    assertEquals(counter.positions.size, 17)
    assertEquals(expected.size, 30)
    assertEquals(counter.positions.size, counter.corners.size)
    assertEquals(counter.regions.size, 5)
    assertEquals(counter.corners.toSet, expected.filter { case (p, _) => counter.positions.contains(p) })
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

  test("CornerCounter - L-shaped cell") {
    val counter = CornerCounter.fromResource("./tests/LShapedCell.txt")

    assertEquals(counter.positions.size, 3)
    assertEquals(counter.positions.size, counter.corners.size)
    assertEquals(counter.regions.size, 1)
    assertEquals(counter.corners((1, 1)), 2)
    assertEquals(counter.corners((1, 2)), 2)
    assertEquals(counter.corners((2, 1)), 2)
  }

  test("CornerCounter - O-shaped cell") {
    val counter = CornerCounter.fromResource("./tests/OShapedCell.txt")

    assertEquals(counter.positions.size, 4)
    assertEquals(counter.positions.size, counter.corners.size)
    assertEquals(counter.regions.size, 1)
    assertEquals(counter.corners((0, 0)), 1)
    assertEquals(counter.corners((1, 1)), 1)
    assertEquals(counter.corners((1, 0)), 1)
    assertEquals(counter.corners((0, 1)), 1)
  }

  test("CornerCounter - I-shaped cell") {
    val counter = CornerCounter.fromResource("./tests/IShapedCell.txt")

    assertEquals(counter.positions.size, 3)
    assertEquals(counter.positions.size, counter.corners.size)
    assertEquals(counter.regions.size, 1)
    assertEquals(counter.corners((1, 1)), 0)
    assertEquals(counter.corners((0, 1)), 2)
    assertEquals(counter.corners((2, 1)), 2)
  }

  test("CornerCounter - T-shaped cell") {
    val counter = CornerCounter.fromResource("./tests/TShapedCell.txt")

    assertEquals(counter.positions.size, 4)
    assertEquals(counter.positions.size, counter.corners.size)
    assertEquals(counter.regions.size, 1)
    assertEquals(counter.corners((1, 1)), 2)
    assertEquals(counter.corners((1, 0)), 2)
    assertEquals(counter.corners((1, 2)), 2)
    assertEquals(counter.corners((2, 1)), 2)
  }

  test("CornerCounter - X-shaped cell") {
    val counter = CornerCounter.fromResource("./tests/XShapedCell.txt")

    assertEquals(counter.positions.size, 5)
    assertEquals(counter.positions.size, counter.corners.size)
    assertEquals(counter.regions.size, 1)
    assertEquals(counter.corners((1, 1)), 4)
    assertEquals(counter.corners((1, 0)), 2)
    assertEquals(counter.corners((1, 2)), 2)
    assertEquals(counter.corners((2, 1)), 2)
    assertEquals(counter.corners((0, 1)), 2)
  }

  test("CornerCounter - Medium") {
    val counter = CornerCounter.fromResource("./tests/Medium.txt")
    val expected = CornerCounter.fromResourceExpected("./tests/Medium_Corners.txt")

    assertEquals(counter.positions.size, 100)
    assertEquals(counter.positions.size, counter.corners.size)
    assertEquals(counter.regions.size, 9)
    assertEquals(counter.corners.toSet, expected)
  }

  test("CornerCounter - Large") {
    val counter = CornerCounter.fromResource("./tests/Large.txt")
    val expected = CornerCounter.fromResourceExpected("./tests/Large_Corners.txt")

    assertEquals(counter.positions.size, 19600)
    assertEquals(counter.positions.size, counter.corners.size)
    assertEquals(counter.regions.size, 26)
    assertEquals(counter.corners.toSet, expected)
  }
}