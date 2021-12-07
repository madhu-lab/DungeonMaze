package dungeon;

/**
 * A class to represent the walls inside a maze.
 */
public class Wall {

  /**
   * The row preceding the wall.
   */
  private final int row;

  /**
   * The column preceding the wall.
   */
  private final int col;

  /**
   * A char representing the orientation of the wall.
   */
  private Orientation orientation;

  /**
   * A constructor for a Wall object.
   *
   * @param row         the row preceding the wall
   * @param col         the column preceding the wall
   * @param orientation a char representing the orientation of the wall
   */
  public Wall(int row, int col, Orientation orientation) throws IllegalArgumentException {
    if (row < 0 || col < 0) {
      throw new IllegalArgumentException("row and column cannot be negative");
    }
    this.row = row;
    this.col = col;
    this.orientation = orientation;
  }

  /**
   * Returns the row of the wall.
   *
   * @return the row of the wall
   */
  public int getRow() {

    return this.row;
  }

  /**
   * Returns the column of the wall.
   *
   * @return the column of the wall
   */
  public int getCol() {

    return this.col;
  }

  /**
   * Returns the orientation of the wall.
   *
   * @return the orientation of the wall
   */
  public Orientation getOrientation() {
    return this.orientation;
  }

}

