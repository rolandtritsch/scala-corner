package corner

type Position = (Int, Int)

/** The CornerCounter. */
class CornerCounter[T](val positions: Map[Position, T]) {
  val logger = com.typesafe.scalalogging.Logger(this.getClass.getName)

  val corners = positions.view.mapValues(_.toString.toInt)
  val regions = positions.groupMap(_._2)(_._1).view.mapValues(_.toSet).toMap

  def this(grid: Array[Array[T]])(using fromGrid: Array[Array[T]] => Map[Position, T]) = this(fromGrid(grid))
  def count(p: Position) = corners(p)
}

object CornerCounter {
  val logger = com.typesafe.scalalogging.Logger(this.getClass.getName)

  given fromGrid[T](using isFreeSpace: (T => Boolean)): (Array[Array[T]] => Map[Position, T]) = { grid =>
    val positions = for {
      i <- grid.indices
      j <- grid(i).indices
      if !isFreeSpace(grid(i)(j))
    } yield (i, j) -> grid(i)(j)
    positions.toMap
  }

  given isFreeSpace: (Char => Boolean) = _ == '.'

  /** @return A CornerCounter initialized with the contents of the given resource. */
  def fromResource(path: String): CornerCounter[Char] = {
    require(path.nonEmpty, "path.nonEmpty")
    logger.debug(s"path: ${path}")

    val source = scala.io.Source.fromResource(path)
    val grid = source.getLines().map(_.toCharArray()).toArray
    new CornerCounter(grid)
  }
}
