package corner

type Position = (Int, Int)

/** The CornerCounter. */
class CornerCounter[T](val positions: Map[Position, T]) {
  val logger = com.typesafe.scalalogging.Logger(this.getClass.getName)

  def this(grid: Array[Array[T]])(using fromGrid: Array[Array[T]] => Map[Position, T]) = this(fromGrid(grid))

  val regions = positions.groupMap(_._2)(_._1).view.mapValues(_.toSet).toMap
  val corners = positions.view.map((p, v) => calcCorner(p, v)).toMap

  def calcCorner[T](p: Position, v: T): (Position, Int) = {
    if(isSingleCell(p)) (p, 4)
    else if(isDoubleCell(p)) (p, 2)
    else if(isLShapedCell(p)) (p, 2)
    else (p, 0)
  }

  def isSingleCell(p: Position): Boolean = {
    val different = Set(
      left(p), 
      right(p),
      up(p),
      down(p),
    )
    val same = Set.empty[Position]

    isValid((different, same), positions(p))
  }

  def isDoubleCell(p: Position): Boolean = {
    val different = Set(
      up(p), 
      down(p),  
      right(p),
    )
    val same = Set(
      left(p),
    )

    rotate((different, same), p).exists(isValid(_, positions(p)))
  }

  def isLShapedCell(p: Position): Boolean = {
    val different = Set(
      down(p),  
      right(p),
      left(up(p))
    )
    val same = Set(
      left(p),
      up(p),
    )

    rotate((different, same), p).exists(isValid(_, positions(p)))
  }

  def rotate(positions: (Set[Position], Set[Position]), pivot: Position): Set[(Set[Position], Set[Position])] = {
    LazyList.iterate(positions) { case (different, same) => {
      (different.map(p => rotate(pivot, p)), same.map(p => rotate(pivot, p)))
    }}.take(4).toSet
  }

  def isValid[T](ps: (Set[Position], Set[Position]), value: T): Boolean = {
    val (different, same) = ps
    different.forall(p => positions(p) != value) && same.forall(p => positions(p) == value) 
  }

  def left(p: Position): Position = (p._1, p._2 - 1)
  def right(p: Position): Position = (p._1, p._2 + 1)
  def up(p: Position): Position = (p._1 - 1, p._2)
  def down(p: Position): Position = (p._1 + 1, p._2)

  /** @return the rotated position (90 degrees (clockwise) around the given pivot) */
  def rotate(pivot: Position, p: Position): Position = {
    if (p == up(pivot)) right(pivot) 
    else if (p == down(pivot)) left(pivot) 
    else if (p == left(pivot)) up(pivot) 
    else if (p == right(pivot)) down(pivot)
    else if (p == left(up(pivot))) right(up(pivot)) 
    else if (p == right(up(pivot))) right(down(pivot)) 
    else if (p == right(down(pivot))) left(down(pivot)) 
    else if (p == left(down(pivot))) left(up(pivot)) 
    else throw new RuntimeException("Unexpected case") 
  }
}

object CornerCounter {
  val logger = com.typesafe.scalalogging.Logger(this.getClass.getName)

  given fromGrid[T](using isFreeSpace: (T => Boolean))(using freeSpaceValue: T): (Array[Array[T]] => Map[Position, T]) = { grid =>
    val positions = for {
      x <- grid.indices
      y <- grid(x).indices
      if !isFreeSpace(grid(x)(y))
    } yield (x, y) -> grid(x)(y)
    positions.toMap.withDefaultValue(freeSpaceValue)
  }

  // Note: For Int this can be Int.MinValue
  given freeSpaceValue: Char = '.'
  given isFreeSpace: (Char => Boolean) = _ == freeSpaceValue

  /** @return A CornerCounter initialized with the contents of the given resource. */
  def fromResource(path: String): CornerCounter[Char] = {
    require(path.nonEmpty, "path.nonEmpty")
    logger.debug(s"path: ${path}")

    val source = scala.io.Source.fromResource(path)
    val grid = source.getLines().map(_.toCharArray()).toArray
    new CornerCounter(grid)
  }
}
