package dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A class to represent the player.
 */
public class Player implements PlayerInterface {
  private AbstractLocation currLocation;
  private final List<Treasure> treasures;
  private final int playerId;
  private int numberOfArrows;

  /**
   * to construct a player with player id.
   *
   * @param playerId the value of the player id.
   */
  public Player(int playerId) throws IllegalArgumentException {
    if (playerId < 0) {
      throw new IllegalArgumentException("invalid playerId");
    }
    this.playerId = playerId;
    this.treasures = new ArrayList<>();
    this.numberOfArrows = 3;
  }

  /**
   * to create a copy of another Player.
   *
   * @param anotherPlayer Player for which the copy has to be made.
   */
  public Player(Player anotherPlayer) throws IllegalArgumentException {
    if (anotherPlayer != null) {
      this.playerId = anotherPlayer.playerId;
      this.treasures = anotherPlayer.treasures;
      this.numberOfArrows = anotherPlayer.numberOfArrows;
      this.currLocation = anotherPlayer.currLocation;
    } else {
      throw new IllegalArgumentException("Player cannot be null");
    }
  }

  /**
   * to add treasure to the player's treasure list.
   *
   * @param treasure treasure object.
   */
  @Override
  public void addTreasure(Treasure treasure) {
    if (treasure != null) {
      this.treasures.add(treasure);
    } else {
      throw new IllegalArgumentException("Treasures to be added to player is invalid.");
    }
  }

  /**
   * increases arrow count by 1.
   */
  @Override
  public void incrementArrowCount() {
    numberOfArrows++;
  }

  /**
   * decreases arrow count by 1.
   */
  @Override
  public void decrementArrowCount() {
    numberOfArrows--;
  }

  /**
   * get the current location.
   *
   * @return abstract location.
   */
  @Override
  public AbstractLocation getCurrLocation() {
    return currLocation;
  }

  /**
   * to return the arrows of the player.
   *
   * @return the number of arrows.
   */
  @Override
  public int getNumberOfArrows() {
    return numberOfArrows;
  }

  /**
   * to pick up the object.
   *
   * @param objectShortCode the code of the object enum.
   */
  @Override
  public void pickupObject(String objectShortCode) {
    switch (objectShortCode) {
      case "ruby":
        pickUpTreasure(Treasure.RUBIES);
        break;
      case "diamond":
        pickUpTreasure(Treasure.DIAMOND);
        break;
      case "sapphire":
        pickUpTreasure(Treasure.SAPPHIRES);
        break;
      case "arrow":
        pickUpArrow();
        break;
      default:
        throw new IllegalArgumentException("Invalid pickUp Object.");
    }
  }

  /**
   * to pick up the arrow of the location.
   *
   * @throws IllegalStateException when empty arrows.
   */
  @Override
  public void pickUpArrow() throws IllegalStateException {
    if (currLocation.getNumOfArrowsPresent() > 0) {
      this.incrementArrowCount();
      currLocation.removeArrow();
    } else {
      throw new IllegalStateException("Arrow cannot be picked up when empty.");
    }
  }

  /**
   * to pick up the treasure of the location.
   *
   * @param treasure the object to pick up.
   * @throws IllegalStateException if the treasure is invalid.
   */
  @Override
  public void pickUpTreasure(Treasure treasure) throws IllegalStateException {
    if (currLocation instanceof Cave) {
      Cave currCave = (Cave) currLocation;
      try {
        currCave.removeTreasure(treasure);
        addTreasure(treasure);
      } catch (IllegalArgumentException e) {
        throw new IllegalStateException(e.getMessage());
      }
    } else {
      throw new IllegalStateException(String.format("Tunnels dont have %s to pick up.",
              treasure.name()));
    }
  }

  /**
   * to shoot the arrow.
   *
   * @param dungeon        the dungeon being played.
   * @param shootDirection the direction enum
   * @param shootDistance  the distance to shoot
   * @return the boolean value.
   */
  @Override
  public boolean shoot(Dungeon dungeon, Direction shootDirection, int shootDistance) {
    decrementArrowCount();
    AbstractLocation currShootLocation = currLocation;
    while (shootDistance > 0) {
      List<Direction> possibleNextMoves = currShootLocation.getPossibleMoves();
      if (!possibleNextMoves.contains(shootDirection)) {
        return false;
      }
      currShootLocation = dungeon.getNextLocationByDirection(currShootLocation, shootDirection);
      if (currShootLocation instanceof Cave) {
        shootDistance--;
      } else {
        if (!possibleNextMoves.contains(shootDirection)) {
          final Direction oldDirection = shootDirection;
          final Optional<Direction> newDirection = possibleNextMoves.stream()
                  .filter(d -> !d.equals(getOppositeDirection(oldDirection))).findFirst();
          if (newDirection.isPresent()) {
            shootDirection = newDirection.get();
          }
        }
      }
    }
    if (currShootLocation instanceof Cave) {
      Cave shootCave = (Cave) currShootLocation;
      if (shootCave.hasOtyugh()) {
        shootCave.getMonster().setHealthAfterHit();
        if (shootCave.getMonster().isSlayed()) {
          dungeon.setLocationSmellLevel(shootCave, false);
        }
        return true;
      }
    }
    return false;
  }

  private Direction getOppositeDirection(Direction direction) {
    switch (direction) {
      case NORTH:
        return Direction.SOUTH;
      case SOUTH:
        return Direction.NORTH;
      case EAST:
        return Direction.WEST;
      case WEST:
        return Direction.EAST;
      default:
        throw new IllegalArgumentException("Invalid direction to get Opposite");
    }
  }

  /**
   * to set the current location of the player.
   *
   * @param currLocation the abstract location.
   */
  @Override
  public void setCurrLocation(AbstractLocation currLocation) {
    this.currLocation = currLocation;
  }

  /**
   * to string of the player.
   *
   * @return string of the player
   */
  @Override
  public String toString() {
    StringBuilder str;
    str = new StringBuilder();
    str.append(currLocation.toString());
    str.append("Your treasures possessed are: \n");
    str.append("1. DIAMOND = ").append(Collections.frequency(treasures, Treasure.DIAMOND))
            .append("\n");
    str.append("2. RUBIES = ").append(Collections.frequency(treasures, Treasure.RUBIES))
            .append("\n");
    str.append("3. SAPPHIRES = ").append(Collections.frequency(treasures, Treasure.SAPPHIRES))
            .append("\n");
    str.append("You have ").append(numberOfArrows).append(" arrows left.\n");
    return str.toString();
  }

  /**
   * the treasures of the player.
   *
   * @return the list.
   */
  @Override
  public List<Treasure> getTreasures() {
    return new ArrayList<>(treasures);
  }
}
