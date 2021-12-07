package dungeon;

/**
 * A class to depict the list of treasures in a location.
 */
public enum Treasure {
  DIAMOND("diamond"), RUBIES("ruby"), SAPPHIRES("sapphire");

  private String singularName;

  Treasure(String singularName) {
    this.singularName = singularName;
  }

  public String getSingularName() {
    return singularName;
  }
}
