# scala-corner

A small scala lib to count corners for regions on a 2-dim grid.

## What problem does it solve?

Counting corners for regions on a 2-dim grid is a common task.

Given a 2-dim grid with various regions on it, e.g. ...
```
A A A A
B B C D
B B C C
E E E C
```
... we want to know how many corners each cell has, e.g. ...
```
2 0 0 2
1 1 2 4
1 1 2 2
2 0 2 2
```
We can use this to calculate the number of corners for a given region.
Which is also equal to the number of sides of the region.

## History

Not to surprisingly, this lib is a side-effect of solving Day12 of the
Advent of Code 2024, but will probably prove useful in other contexts.

## How to use it?

1. Put the lib into your build file, e.g. `build.sbt` ...
```
libraryDependencies += "com.github.rolandtritsch" %% "scala-corner" % "0.1.0" 
```
2. Create an instance of the `CornerCounter` class ...
```scala
val corners = new CornerCounter(grid)
```
3. Call the `count` method with the cordinates you are interested in to get 
   the number of corners for that cell, e.g. ...
```scala
val count = corners.count((0, 0))
```
The constructor either takes an `Array[Array[T]]` or a `Map[(Int, Int), T]`
to describe all cells of the regions that need to be counted.

For testing, grids can also be created with the `fromResource()` method.
Please consult the [documentation][] and the tests to learn how this works.

The regions cannot be overlaping. Every cell belongs to exactly one region.

There can be free space between the regions, e.g. ...
```
A A A A .
B B . C D
B B . C C
. . . . C
E E E . C
```
## How to build it?

We are using mill to build and test the lib ...
```bash
./mill main.test
```
Please take a look at `ci.yml` to see how you can build the [documentation][]
and the [scoverage][] report.

## How to publish a new version?

Update the `build.sc` file and commit and push the change. Call this commit
`Publish 1.0.3`.

Then you can tag the commit you want to publish and push the tag ...
```bash
git tag 1.0.3
git push origin --tags
```
Note: Make sure the tag is new and unique.

Please also create a new release on GitHub (manually).

## How to test and debug it?

If you need to debug the lib you can change the log-level in `logback.xml`.

## How to contribute?

Please feel free to fork the repo and submit pull requests.

## Design Notes

Internally the lib uses a `Map[(Int, Int), T]` to respresent all regions,
where `(Int, Int)` is the `Position` of the cell and `T` is the value of
the cell. If the `Map` got build from an `Array[Array[T]]` the origin of 
the grid (0, 0) is the top-left corner of the grid.

Note: Determining how many corners a cell has does not require that we check
on the boundaries of the grid. Instead we just assume that every cell that 
is not in the `Map` is free space (by returning a default value that represents
free space). This also means that the grid (or more specifically the `Map`) 
does not have to be a square.

Note: The grid can contain multiple regions with same cell values that are
not connected. This is not a region finder. It is a corner counter.

This implementation is not optimized for performance. But it will probably be 
good enough for a lot of use cases. And it can be used (as a reference 
implementation) to test faster implementations (with property based testing 
frameworks).

Conceptionally the implementation moves a 3x3 grid over all cells and checks
if the middle cell matches one of the patterns below.

Note: (Some of) These patterns need to be (flipped and then) rotated 4 times.

The patterns use the following symbols ...

- X the cell must be different
- O the cell must be the same
- ? the cell can be either

### No Cell
```
OOO
OOO
OOO
``` 
- 0 corners
### Single Cell
```
?X?
XOX
?X?
``` 
- 4 corners
### Double Cells
```
?X?
OOX
?X?
```
- 2 corners
### L-shaped Cells
```
XO?
OOX
?X?
```
- 2 corners
### O-shaped Cells
```
OO?
OOX
?X?
```
- 1 corners
### Z-shaped Cells
```
XOO
OOO
OOX
```
- 2 corners
### I-shaped Cells
```
?X?
OOO
?X?
```
- 0 corners
### I1-shaped Cells
```
?X?
OOO
OOO
```
- 0 corners
### T1-shaped Cells
``` 
XOX
OOO
?X?
``` 
- 2 corners
### T2-shaped Cells
``` 
XOX
OOO
OOO
``` 
- 2 corners
### T3-shaped Cells
``` 
XOO
OOO
OOO
``` 
- 1 corners
### T4-shaped Cells
``` 
XOO
OOO
?X?
``` 
- 1 corners
### X1-shaped Cells
```
XOX
OOO
XOX
```
- 4 corners
### X2-shaped Cells
```
OOX
OOO
XOX
```
- 3 corners

[documentation]: https://tedn.life/scala-corner/index.html
[scoverage]: https://tedn.life/scala-corner/scoverage/index.html