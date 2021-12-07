package dungeon;

/**
 * the directions of the move.
 */
public enum Direction {
  NORTH("N"), SOUTH("S"), EAST("E"), WEST("W");

  private final String shortcode;

  Direction(String shortcode) {
    this.shortcode = shortcode;
  }

  /**
   * return the code of the enum.
   *
   * @return the string value.
   */
  public String getShortCode() {
    return shortcode;
  }

}
