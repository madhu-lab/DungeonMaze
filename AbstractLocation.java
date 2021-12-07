package dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * An abstract class which shows the common functionalities of locations.
 */
public class AbstractLocation implements Location {
  private final int rowDim;
  private final int colDim;
  private boolean up;
  private boolean down;
  private boolean right;
  private boolean left;
  protected int numOfArrowsPresent;
  private int smellLevel;

  /**
   * to build an abstract location object with row and col.
   *
   * @param rowDim the row of the location
   * @param colDim the col of the location.
   * @throws IllegalArgumentException for invalid row and col
   */
  public AbstractLocation(int rowDim, int colDim) throws IllegalArgumentException {
    this(rowDim, colDim, true, true, true, true);
  }

  /**
   * to build an abstract location object with row and col.
   *
   * @param rowDim the row of the location
   * @param colDim the col of the location.
   * @param up     if the wall is present upside.
   * @param down   if the wall is present down
   * @param right  if the wall is present right
   * @param left   if the wall is present left
   */
  public AbstractLocation(int rowDim, int colDim, boolean up, boolean down, boolean right,
                          boolean left) {
    if (rowDim < 0 || colDim < 0) {
      throw new IllegalArgumentException("invalid row and col");
    }
    this.rowDim = rowDim;
    this.colDim = colDim;
    this.up = up;
    this.down = down;
    this.right = right;
    this.left = left;
    this.numOfArrowsPresent = 0;
  }

  /**
   * Return whether this cell's north wall exists.
   *
   * @return true if and only if the north wall exists
   */
  public boolean hasWallUpSide() {
    return up;
  }

  /**
   * Indicate whether this cell's north wall should exist.
   *
   * @param up true if wall exists; false otherwise
   */
  public void setUp(boolean up) {
    this.up = up;
  }

  /**
   * Return whether this cell's south wall exists.
   *
   * @return true if and only if the south wall exists
   */
  public boolean hasWallDownSide() {
    return down;
  }

  /**
   * Indicate whether this cell's south wall should exist.
   *
   * @param down true if wall exists; false otherwise
   */
  public void setDown(boolean down) {
    this.down = down;
  }

  /**
   * Return whether this cell's west wall exists.
   *
   * @return true if and only if the west wall exists
   */
  public boolean hasWallRightSide() {
    return right;
  }

  /**
   * Indicate whether this cell's west wall should exist.
   *
   * @param right true if wall exists; false otherwise
   */
  public void setRight(boolean right) {
    this.right = right;
  }

  /**
   * Return whether this cell's east wall exists.
   *
   * @return true if and only if the east wall exists
   */
  public boolean hasWallLeftSide() {

    return left;
  }

  /**
   * Indicate whether this cell's east wall should exist.
   *
   * @param left true if wall exists; false otherwise
   */
  public void setLeft(boolean left) {

    this.left = left;
  }

  /**
   * return the row dimension of the location.
   *
   * @return the row dimension of location.
   */
  @Override
  public int getRowDim() {
    return rowDim;
  }

  /**
   * return the column dimension of the location.
   *
   * @return the row dimension of location.
   */
  @Override
  public int getColDim() {
    return colDim;
  }

  /**
   * return the number of entrances.
   *
   * @return the entrances.
   */
  public int getNumberOfEntrance() {
    int result = 0;
    if (!hasWallUpSide()) {
      result++;
    }
    if (!hasWallDownSide()) {
      result++;
    }
    if (!hasWallLeftSide()) {
      result++;
    }
    if (!hasWallRightSide()) {
      result++;
    }
    return result;
  }

  /**
   * the possible moves of the location.
   *
   * @return the list of directions.
   */
  public List<Direction> getPossibleMoves() {
    List<Direction> res = new ArrayList<>();
    if (!hasWallUpSide()) {
      res.add(Direction.NORTH);
    }
    if (!hasWallDownSide()) {
      res.add(Direction.SOUTH);
    }
    if (!hasWallLeftSide()) {
      res.add(Direction.WEST);
    }
    if (!hasWallRightSide()) {
      res.add(Direction.EAST);
    }
    return res;
  }

  /**
   * return the column size by id.
   *
   * @param colSize the  the column size.
   * @return column size.
   */
  public int getIdByColSize(int colSize) {
    return this.rowDim * colSize + this.colDim;
  }

  /**
   * adds a new Arrow count in the location that can be picked by a player.
   */
  public void addArrows() {
    numOfArrowsPresent++;
  }

  /**
   * return the number of arrows.
   *
   * @return the arrows.
   */
  public int getNumOfArrowsPresent() {
    return numOfArrowsPresent;
  }

  /**
   * to remove the arrows and decrease the number.
   */
  public void removeArrow() {
    numOfArrowsPresent--;
  }

  /**
   * to increment the smell level.
   *
   * @param val the value to decrease.
   */
  public void incrementSmellLevel(int val) {
    this.smellLevel += val;
  }

  /**
   * to decrease the smell level.
   *
   * @param val the value to decrease.
   */
  public void decrementSmellLevel(int val) {
    this.smellLevel -= val;
  }

  /**
   * to get the smell level.
   *
   * @return the smell level.
   */
  public int getSmellLevel() {
    return smellLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbstractLocation that = (AbstractLocation) o;
    return rowDim == that.rowDim
            && colDim == that.colDim;
  }

  @Override
  public int hashCode() {
    return Objects.hash(rowDim, colDim);
  }

  protected String getPossibleMovesAsString() {
    final List<String> possibleMoves = getPossibleMoves().stream()
            .map(Direction::getShortCode).collect(Collectors.toList());
    return String.join(", ", possibleMoves);
  }

  protected String getSmellInfoAsString() {
    if (smellLevel > 1) {
      return "You smell something terribly nearby.\n";
    } else if (smellLevel == 1) {
      return "You smell something nearby, a less pungent smell.\n";
    } else {
      return "";
    }
  }

  protected String getArrowsCountAsString() {
    if (numOfArrowsPresent > 0) {
      return String.format("You can find %s arrows here.\n", numOfArrowsPresent);
    } else {
      return "";
    }
  }

  /**
   * to get the pick up objects.
   *
   * @return the list of strings.
   */
  public List<String> getPossiblePickupObjects() {
    List<String> possiblePickUpObjects = new ArrayList<>();
    if (getNumOfArrowsPresent() > 0) {
      possiblePickUpObjects.add("arrow");
    }
    return possiblePickUpObjects;
  }

}
