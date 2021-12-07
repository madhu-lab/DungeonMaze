package test;

import static org.junit.Assert.assertEquals;

import dungeon.Dungeon;
import dungeon.DungeonConsoleController;
import dungeon.DungeonController;
import dungeon.Player;
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;

/**
 * A test for Dungeon Controller.
 */
public class DungeonConsoleControllerTest {

  private Dungeon testDungeon;
  private Player player;
  private DungeonController testDungeonController;

  /**
   * the dungeon object is instantiated.
   */
  @Before
  public void setUp() {
    final int[] randomSequence = {5, 14, 8, 4, 10, 12, 15, 3, 9, 1, 11, 6, 13, 2, 7, 21, 17, 16,
        8, 20, 18, 22, 19, 24, 28, 27, 28, 30, 34, 36, 38, 42, 45,
        40, 43, 44, 47, 3, 2, 1, 3, 1, 3, 2, 4};
    Random random = new PredictableRandom(randomSequence);
    testDungeon = new Dungeon(6, 4, 2, true, 10, 2, random);
    testDungeon.addStart();
    testDungeon.addEnd();
    testDungeon.assignTreasuresAndArrows();
    testDungeon.assignMonsters();
    testDungeon.getMonsters()
            .forEach(monster -> testDungeon.setLocationSmellLevel(monster.getLocation(), true));
    player = new Player(1);
    testDungeon.addPlayer(player);
  }

  @Test
  public void testInitialMessageIsRight() {
    StringReader input = new StringReader("M\nE\nM\nW");
    StringBuilder gameLog = new StringBuilder();
    testDungeonController = new DungeonConsoleController(input, gameLog);
    testDungeonController.playGame(testDungeon, player);
    String[] lines = gameLog.toString().split("\n");
    for (String line : lines) {
      System.out.println(line);
    }
    assertEquals("You are in a Cave", lines[0]);
    assertEquals("You smell something nearby, a less pungent smell.", lines[1]);
    assertEquals("Tunnels lead to the S, W, E", lines[2]);
    assertEquals("You have 3 arrows left.", lines[7]);
    assertEquals("Move, Pickup, or Shoot (M-P-S)?", lines[10]);
  }

  @Test
  public void testMonsterEatsPlayer() {
    StringReader input = new StringReader("M\nS\nM\nW");
    StringBuilder gameLog = new StringBuilder();
    testDungeonController = new DungeonConsoleController(input, gameLog);
    testDungeonController.playGame(testDungeon, player);
    String[] lines = gameLog.toString().split("\n");
    assertEquals("Chomp, chomp, chomp, you are eaten by an Otyugh!", lines[lines.length - 2]);
    assertEquals("Better luck next time", lines[lines.length - 1]);
  }

  @Test
  public void testPlayerPicksArrow() {
    StringReader input = new StringReader("M\nS\nP\narrow\nM\nE\n");
    StringBuilder gameLog = new StringBuilder();
    testDungeonController = new DungeonConsoleController(input, gameLog);
    try {
      testDungeonController.playGame(testDungeon, player);
    } catch (NoSuchElementException exception) {
      System.out.println(exception.getMessage());
    }
    String[] lines = gameLog.toString().split("\n");
    for (String line : lines) {
      System.out.println(line);
    }
    assertEquals("You pick up a arrow", lines[26]);
  }

  @Test
  public void testPlayerPicksShootsIntoDarkness() {
    StringReader input = new StringReader("S\n2\nE\n");
    StringBuilder gameLog = new StringBuilder();
    testDungeonController = new DungeonConsoleController(input, gameLog);
    try {
      testDungeonController.playGame(testDungeon, player);
    } catch (NoSuchElementException exception) {
      System.out.println(exception.getMessage());
    }
    String[] lines = gameLog.toString().split("\n");
    for (String line : lines) {
      System.out.println(line);
    }
    assertEquals("You shoot an arrow into the darkness", lines[11]);
  }

  @Test
  public void testPlayerPicksShootsIntoOtyugh() {
    StringReader input = new StringReader("S\n1\nE\n");
    StringBuilder gameLog = new StringBuilder();
    testDungeonController = new DungeonConsoleController(input, gameLog);
    try {
      testDungeonController.playGame(testDungeon, player);
    } catch (NoSuchElementException exception) {
      System.out.println(exception.getMessage());
    }
    String[] lines = gameLog.toString().split("\n");
    for (String line : lines) {
      System.out.println(line);
    }
    assertEquals("You hear a great howl in the distance", lines[12]);
  }

  @Test
  public void testPlayerMovesInvalidOption() {
    StringReader input = new StringReader("M\n2\nW\n");
    StringBuilder gameLog = new StringBuilder();
    testDungeonController = new DungeonConsoleController(input, gameLog);
    try {
      testDungeonController.playGame(testDungeon, player);
    } catch (NoSuchElementException exception) {
      System.out.println(exception.getMessage());
    }
    String[] lines = gameLog.toString().split("\n");
    for (String line : lines) {
      System.out.println(line);
    }
    assertEquals("Invalid move: 2", lines[11]);
    assertEquals("Invalid move: W", lines[12]); //invalid move as W is not possible move
  }

  @Test
  public void testPlayerInvalidPickupOption() {
    StringReader input = new StringReader("M\nS\nP\nchocolate\n");
    StringBuilder gameLog = new StringBuilder();
    testDungeonController = new DungeonConsoleController(input, gameLog);
    try {
      testDungeonController.playGame(testDungeon, player);
    } catch (NoSuchElementException exception) {
      System.out.println(exception.getMessage());
    }
    String[] lines = gameLog.toString().split("\n");
    for (String line : lines) {
      System.out.println(line);
    }
    assertEquals("Invalid pick up object: chocolate", lines[lines.length - 1]);
  }


}
