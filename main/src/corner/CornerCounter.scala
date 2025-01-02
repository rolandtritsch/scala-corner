package corner

type Position = (Int, Int)

/** The CornerCounter. */
class CornerCounter[T](val regions: Map[Position, T]) {
  val corners = regions.view.mapValues(_.toString.toInt)

  def this(grid: Array[Array[T]])(using fromGrid: Array[Array[T]] => Map[Position, T]) = this(fromGrid(grid))
  def count(p: Position) = corners(p)
}

object CornerCounter {
  given fromGrid[T]: (Array[Array[T]] => Map[Position, T]) = { grid =>
    val positions = for {
      i <- grid.indices
      j <- grid(i).indices
    } yield (i, j) -> grid(i)(j)
    positions.toMap
  }

  /** @return A CornerCounter initialized with the contents of the given resource. */
  def fromResource(path: String): CornerCounter[Char] = {
    val source = scala.io.Source.fromResource(path)
    val grid = source.getLines().map(_.toCharArray()).toArray
    new CornerCounter(grid)
  }
}
