package dungeon;

import java.io.InputStreamReader;

/**
 * driver for the dungeon driver.
 */
public class DungeonDriver {

  /**
   * the main class for dungeon.
   *
   * @param args the arguments.
   */
  public static void main(String[] args) {
    if (args.length != 6) {
      throw new IllegalArgumentException("Requires 6 command value arguments for the Game");
    }
    final int rows = Integer.parseInt(args[0]);
    final int cols = Integer.parseInt(args[1]);
    final int interConnectivity = Integer.parseInt(args[2]);
    final boolean isWrapping = Boolean.parseBoolean(args[3]);
    final int treasurePercent = Integer.parseInt(args[4]);
    final int difficulty = Integer.parseInt(args[5]);

    final Readable input = new InputStreamReader(System.in);
    final Appendable output = System.out;

    // Dungeon set up
    Dungeon dungeon = new Dungeon(rows, cols, interConnectivity, isWrapping, treasurePercent,
            difficulty);
    dungeon.assignTreasuresAndArrows();
    dungeon.addStart();
    dungeon.addEnd();
    dungeon.assignMonsters();
    dungeon.getMonsters()
            .forEach(monster -> dungeon.setLocationSmellLevel(monster.getLocation(), true));
    Player player = new Player(1);
    dungeon.addPlayer(player);

    //start to play game
    new DungeonConsoleController(input, output).playGame(dungeon, player);
  }

}
