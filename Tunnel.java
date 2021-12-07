package dungeon;

/**
 * the class to represent the tunnel class.
 */
public class Tunnel extends AbstractLocation {

  /**
   * tunnel object to be constructed with abstract location.
   *
   * @param abstractLocation the location
   */
  public Tunnel(AbstractLocation abstractLocation) {
    super(abstractLocation.getRowDim(), abstractLocation.getColDim(),
            abstractLocation.hasWallUpSide(), abstractLocation.hasWallDownSide(),
            abstractLocation.hasWallRightSide(), abstractLocation.hasWallLeftSide());
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append("You are in a Tunnel\n");
    str.append(getSmellInfoAsString());
    str.append("that continues to ").append(getPossibleMovesAsString()).append("\n");
    str.append(getArrowsCountAsString());
    return str.toString();
  }

}
