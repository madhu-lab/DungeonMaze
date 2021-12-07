package dungeon;


/**
 * An interface for every Location in the dungeon.
 */
public interface Location {

  /**
   * gets the row dimension of the location.
   *
   * @return the  value of the number of rows.
   */
  int getRowDim();

  /**
   * gets the column dimension of the location.
   *
   * @return the value of the column.
   */
  int getColDim();


}
