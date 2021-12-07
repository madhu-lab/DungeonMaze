package dungeon;

/**
 * An interface for the controller of the dungeon.
 */
public interface DungeonController {

  /**
   * Execute a single game of Dungeon given a Dungeon Model. When the game is over,
   * the playGame method ends.
   *
   * @param dungeon a non-null Dungeon Model
   * @param player a non-null Player in the dungeon.
   */
  void playGame(Dungeon dungeon, Player player);

}
