package dungeon;

/**
 * An interface for the monster.
 */
public interface Monster {

  /**
   * to return the health of the monster.
   *
   * @return the health.
   */
  int getHealth();

  /**
   * to set the health after hit.
   */
  void setHealthAfterHit();

  /**
   * to return if the monster is slayed.
   *
   * @return the flag
   */
  Boolean isSlayed();

  /**
   * to get the location of the monster.
   *
   * @return the location.
   */
  AbstractLocation getLocation();

}
