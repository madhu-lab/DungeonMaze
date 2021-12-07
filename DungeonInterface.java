package dungeon;

import java.util.List;

/**
 * An interface to implement the dungeon class.
 */
public interface DungeonInterface {

  /**
   * to assign treasures to the location.
   */
  void assignTreasuresAndArrows();

  /**
   * to get the list of caves.
   *
   * @return the caves.
   */
  List<Cave> getCaves();

  /**
   * to get the list of tunnels.
   *
   * @return the tunnels.
   */
  List<Tunnel> getTunnels();

  /**
   * add player tyo the location.
   *
   * @param player player to be added.
   */
  void addPlayer(Player player);

  /**
   * to determine the start position of the player.
   */
  void addStart();

  /**
   * to determine the end position of the player.
   */
  void addEnd();

  /**
   * to return the start location.
   *
   * @return the location.
   */
  AbstractLocation getStart();

  /**
   * to move the player.
   *
   * @param directionCode the direction to move.
   */
  void movePlayer(String directionCode);


  /**
   * to return the player.
   *
   * @return the player.
   */
  Player getPlayer();


  /**
   * assign monsters to the locations.
   */
  void assignMonsters();


}
