package dungeon;

/**
 * A class to represent the otyugh.
 */
public class Otyugh implements Monster {
  private int health;
  private final AbstractLocation location;

  /**
   * to construct an otyugh in a location.
   *
   * @param location the location of monster.
   */
  public Otyugh(AbstractLocation location) {
    this.health = 100;
    this.location = location;
  }

  /**
   * to return the health.
   *
   * @return the health value.
   */
  public int getHealth() {
    return health;
  }

  /**
   * to set health after hit.
   */
  public void setHealthAfterHit() {
    if (!isSlayed()) {
      this.health = Math.max(0, health - 50);
    } else {
      throw new IllegalStateException("Monster already slayed cannot get hit");
    }
  }

  /**
   * to check if the monster is slayed.
   *
   * @return the boolean.
   */
  public Boolean isSlayed() {
    return health == 0;
  }

  /**
   * to get the location of the monster.
   *
   * @return the location.
   */
  public AbstractLocation getLocation() {
    return location;
  }

}
