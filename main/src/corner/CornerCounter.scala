package corner

type Position = (Int, Int)

/** The CornerCounter. Initialized with a set of positions. */
class CornerCounter[T](val positions: Map[Position, T]) {
  val logger = com.typesafe.scalalogging.Logger(this.getClass.getName)

  /** @return a CornerCounter initialized with the contents of the given Array. */
  def this(grid: Array[Array[T]])(using fromGrid: Array[Array[T]] => Map[Position, T]) = this(fromGrid(grid))

  override def toString: String = 
    s"CornerCounter(positions=${positions}, regions=${regions}, corners=${corners})"

  /** @return a pretty string representation of the grid. */
  def toStringPrettyGrid(using freeSpaceValue: T): String = {
    val (maxX, maxY) = dimensions
    val ps = 
      (0 to maxX).map { x => {
        (0 to maxY).map { y => {
          positions.getOrElse((x, y), freeSpaceValue)
        }}.mkString
      }}.mkString("\n")
    s"grid:\n${ps}"
  }

  /** @return a pretty string representation of the corner counts. */  
  def toStringPrettyCorners: String = {
    val (maxX, maxY) = dimensions
    val cs = 
      (0 to maxX).map { x => {
        (0 to maxY).map { y => {
          corners.getOrElse((x, y), 0)
        }}.mkString
      }}.mkString("\n")
    s"corners:\n${cs}"
  }

  /** All regions in the grid. Just used for testing. */
  val regions = positions.groupMap(_._2)(_._1).view.mapValues(_.toSet).toMap

  /** Map of all positions and their corner count in the grid. This is the thing to use. */
  val corners = positions.view.map((p, v) => calcCorner(p, v)).toMap.withDefault(_ => 0)

  // --- private stuff

  private val dimensions = {
    val maxX = positions.keySet.map(_._1).max
    val maxY = positions.keySet.map(_._2).max
    (maxX, maxY)
  }

  private def calcCorner[T](p: Position, v: T): (Position, Int) = {
    if(isNoCell(p)) (p, 0)
    else if(isSingleCell(p)) (p, 4)
    else if(isDoubleCell(p)) (p, 2)
    else if(isX1ShapedCell(p)) (p, 4)
    else if(isX2ShapedCell(p)) (p, 3)
    else if(isZShapedCell(p)) (p, 2)
    else if(isLShapedCell(p)) (p, 2)
    else if(isOShapedCell(p)) (p, 1)
    else if(isI1ShapedCell(p)) (p, 0)
    else if(isI2ShapedCell(p)) (p, 0)
    else if(isT1ShapedCell(p)) (p, 2)
    else if(isT2ShapedCell(p)) (p, 2)
    else if(isT3ShapedCell(p)) (p, 1)
    else if(isT4ShapedCell(p)) (p, 1)
    else throw new RuntimeException(s"Unexpected case - ${p}")
  }

  private def isNoCell(p: Position): Boolean = {
    val different = Set.empty[Position]
    val same = Set(
      left(p), 
      right(p),
      up(p),
      down(p),
      left(up(p)),
      right(up(p)),
      left(down(p)),
      right(down(p)),
    )

    isValid((different, same), positions(p))
  }

  private def isSingleCell(p: Position): Boolean = {
    val different = Set(
      left(p), 
      right(p),
      up(p),
      down(p),
    )
    val same = Set.empty[Position]

    isValid((different, same), positions(p))
  }

  private def isDoubleCell(p: Position): Boolean = {
    val different = Set(
      up(p), 
      down(p),  
      right(p),
    )
    val same = Set(
      left(p),
    )

    rotated((different, same), p).exists(isValid(_, positions(p)))
  }

  private def isLShapedCell(p: Position): Boolean = {
    val different = Set(
      down(p),  
      right(p),
      left(up(p)),
    )
    val same = Set(
      left(p),
      up(p),
    )

    rotated((different, same), p).exists(isValid(_, positions(p)))
  }

  private def isOShapedCell(p: Position): Boolean = {
    val different = Set(
      down(p),  
      right(p),
    )
    val same = Set(
      left(p),
      up(p),
      left(up(p)),
    )

    rotated((different, same), p).exists(isValid(_, positions(p)))
  }

  private def isI1ShapedCell(p: Position): Boolean = {
    val different = Set(
      down(p),
      up(p),
    )
    val same = Set(
      right(p),
      left(p),
    )

    rotated((different, same), p).exists(isValid(_, positions(p)))
  }

  private def isI2ShapedCell(p: Position): Boolean = {
    val different = Set(
      up(p),
    )
    val same = Set(
      right(p),
      left(p),
      down(p),
      left(down(p)),
      right(down(p)),
    )

    rotated((different, same), p).exists(isValid(_, positions(p)))
  }

  private def isT1ShapedCell(p: Position): Boolean = {
    val different = Set(
      left(up(p)),
      right(up(p)),
      down(p),
    )
    val same = Set(
      up(p),
      right(p),
      left(p),
    )

    rotated((different, same), p).exists(isValid(_, positions(p)))
  }

  private def isT2ShapedCell(p: Position): Boolean = {
    val different = Set(
      left(up(p)),
      right(up(p)),
    )
    val same = Set(
      up(p),
      right(p),
      left(p),
      down(p),
      left(down(p)),
      right(down(p)),
    )

    rotated((different, same), p).exists(isValid(_, positions(p)))
  }

  private def isT3ShapedCell(p: Position): Boolean = {
    val different = Set(
      left(up(p)),
    )
    val same = Set(
      up(p),
      right(p),
      left(p),
      down(p),
      left(down(p)),
      right(down(p)),
      right(up(p)),
    )

    val (dFlipped, sFlipped) = flipped((different, same), p)
    rotated((different, same), p).exists(isValid(_, positions(p)))
    || rotated((dFlipped, sFlipped), p).exists(isValid(_, positions(p)))
  }

  private def isT4ShapedCell(p: Position): Boolean = {
    val different = Set(
      left(up(p)),
      down(p),
    )
    val same = Set(
      up(p),
      right(p),
      left(p),
      right(up(p)),
    )

    val (dFlipped, sFlipped) = flipped((different, same), p)
    rotated((different, same), p).exists(isValid(_, positions(p)))
    || rotated((dFlipped, sFlipped), p).exists(isValid(_, positions(p)))
  }

  private def isX1ShapedCell(p: Position): Boolean = {
    val different = Set(
      left(up(p)),
      right(up(p)),
      left(down(p)),
      right(down(p)),
    )
    val same = Set(
      up(p),
      right(p),
      left(p),
      down(p),
    )

    isValid((different, same), positions(p))
  }

  private def isX2ShapedCell(p: Position): Boolean = {
    val different = Set(
      right(up(p)),
      left(down(p)),
      right(down(p)),
    )
    val same = Set(
      up(p),
      right(p),
      left(p),
      down(p),
      left(up(p)),
    )

    val (dFlipped, sFlipped) = flipped((different, same), p)
    rotated((different, same), p).exists(isValid(_, positions(p)))
    || rotated((dFlipped, sFlipped), p).exists(isValid(_, positions(p)))
  }

  private def isZShapedCell(p: Position): Boolean = {
    val different = Set(
      left(up(p)),
      right(down(p)),
    )
    val same = Set(
      up(p),
      right(p),
      left(p),
      down(p),
      left(down(p)),
      right(up(p)),
    )

    val (dFlipped, sFlipped) = flipped((different, same), p)
    rotated((different, same), p).exists(isValid(_, positions(p)))
    || rotated((dFlipped, sFlipped), p).exists(isValid(_, positions(p)))
  }

  private def rotated(positions: (Set[Position], Set[Position]), pivot: Position): Set[(Set[Position], Set[Position])] = {
    LazyList.iterate(positions) { case (different, same) => {
      (different.map(p => rotate(pivot, p)), same.map(p => rotate(pivot, p)))
    }}.take(4).toSet
  }

  private def flipped(positions: (Set[Position], Set[Position]), pivot: Position): (Set[Position], Set[Position]) = {
    val (different, same) = positions
    (different.map(p => flip(pivot, p)), same.map(p => flip(pivot, p)))
  }

  private def isValid[T](ps: (Set[Position], Set[Position]), value: T): Boolean = {
    val (different, same) = ps
    different.forall(p => positions(p) != value) && same.forall(p => positions(p) == value) 
  }

  private def left(p: Position): Position = (p._1, p._2 - 1)
  private def right(p: Position): Position = (p._1, p._2 + 1)
  private def up(p: Position): Position = (p._1 - 1, p._2)
  private def down(p: Position): Position = (p._1 + 1, p._2)

  /** @return the flipped position (vertial the given pivot) */
  private def flip(pivot: Position, p: Position): Position = {
    if (p == up(pivot)) p 
    else if (p == down(pivot)) p 
    else if (p == left(pivot)) right(pivot) 
    else if (p == right(pivot)) left(pivot)
    else if (p == left(up(pivot))) right(up(pivot)) 
    else if (p == right(up(pivot))) left(up(pivot)) 
    else if (p == right(down(pivot))) left(down(pivot)) 
    else if (p == left(down(pivot))) right(down(pivot)) 
    else throw new RuntimeException("Unexpected case") 
  }

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

/** The CornerCounter companion. */
object CornerCounter {
  val logger = com.typesafe.scalalogging.Logger(this.getClass.getName)

  /** @return a Mapinitialized with the contents of the given Array. Used by the ctor. */
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

  /** @return a CornerCounter initialized with the contents of the given resource. */
  def fromResource(path: String): CornerCounter[Char] = {
    require(path.nonEmpty, "path.nonEmpty")
    logger.debug(s"path: ${path}")

    val source = scala.io.Source.fromResource(path)
    val grid = source.getLines().map(_.toCharArray()).toArray
    new CornerCounter(grid)
  }

  /** @return the Set of expected corner counts (for testing). */
  def fromResourceExpected(path: String): Set[((Int, Int), Int)] = {
    require(path.nonEmpty, "path.nonEmpty")
    logger.debug(s"path: ${path}")

    val source = scala.io.Source.fromResource(path)
    source.getLines().zipWithIndex.flatMap { case (line, x) => {
      line.zipWithIndex.map { case (char, y) => ((x, y), char.toString.toInt) }
    }}.toSet
  }
}
