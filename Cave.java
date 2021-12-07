package dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A class to represent the cave with 1,3 or 4 entrances.
 */
public class Cave extends AbstractLocation {

  private Otyugh otyugh;
  private final Map<Treasure, Integer> treasureMap;

  /**
   * build a cave object with abstract location.
   *
   * @param abstractLocation the abstract location.
   */
  public Cave(AbstractLocation abstractLocation) {
    super(abstractLocation.getRowDim(), abstractLocation.getColDim(),
            abstractLocation.hasWallUpSide(), abstractLocation.hasWallDownSide(),
            abstractLocation.hasWallRightSide(), abstractLocation.hasWallLeftSide());
    this.treasureMap = new HashMap<>();
  }

  /**
   * adds a new treasure to the player.
   */
  public void addTreasures(Treasure treasure) {
    if (treasure == null) {
      throw new IllegalArgumentException("Treasure is invalid to be assigned to a cave");
    }
    if (treasureMap.containsKey(treasure)) {
      Integer val = treasureMap.get(treasure);
      treasureMap.put(treasure, val++);
    } else {
      treasureMap.put(treasure, 1);
    }
  }

  /**
   * remove the treasure.
   *
   * @param treasure to be removed.
   * @throws IllegalArgumentException if treasure is null.
   */
  public void removeTreasure(Treasure treasure) throws IllegalArgumentException {
    if (treasure == null) {
      throw new IllegalArgumentException("Treasure is invalid to be removed from a cave");
    }
    if (!treasureMap.containsKey(treasure) || treasureMap.get(treasure) <= 0) {
      throw new IllegalArgumentException("Cannot pick a treasure that is not available");
    } else {
      int val = treasureMap.get(treasure);
      treasureMap.put(treasure, val - 1);
    }
  }

  /**
   * return the treasures of the cave.
   *
   * @return map of treasure.
   */
  public Map<Treasure, Integer> getTreasures() {
    return new HashMap<>(treasureMap);
  }

  /**
   * adds a new Otyugh to the Cave.
   */
  public void addMonster() {
    if (hasOtyugh()) {
      throw new IllegalStateException("Otyugh already present in the cave");
    }
    otyugh = new Otyugh(this);
  }

  /**
   * to return the monster in cave.
   *
   * @return the monster object.
   */
  public Otyugh getMonster() {
    return otyugh;
  }

  /**
   * to check if the monster is in cave.
   *
   * @return flag value.
   */
  public Boolean hasOtyugh() {
    return (otyugh != null && !otyugh.isSlayed());
  }

  @Override
  public List<String> getPossiblePickupObjects() {
    List<String> possiblePickUpObjects = new ArrayList<>();

    List<String> possiblePickUpTreasures = getTreasures().entrySet().stream()
            .filter(e -> e.getValue() > 0)
            .map(e -> e.getKey().getSingularName()).collect(Collectors.toList());
    List<String> possiblePickUpArrows = super.getPossiblePickupObjects();
    possiblePickUpObjects.addAll(possiblePickUpTreasures);
    possiblePickUpObjects.addAll(possiblePickUpArrows);
    return possiblePickUpObjects;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append("You are in a Cave\n");
    str.append(getSmellInfoAsString());
    final int rubiesCount = treasureMap.getOrDefault(Treasure.RUBIES, 0);
    final int diamondCount = treasureMap.getOrDefault(Treasure.DIAMOND, 0);
    final int sapphiresCount = treasureMap.getOrDefault(Treasure.SAPPHIRES, 0);
    if (rubiesCount > 0) {
      str.append(String.format("You can find %s rubies here.\n", rubiesCount));
    }
    if (diamondCount > 0) {
      str.append(String.format("You can find %s diamonds here.\n", diamondCount));
    }
    if (sapphiresCount > 0) {
      str.append(String.format("You can find %s sapphires here.\n", sapphiresCount));
    }
    str.append(getArrowsCountAsString());
    str.append("Tunnels lead to the ").append(getPossibleMovesAsString()).append("\n");
    return str.toString();
  }

}







