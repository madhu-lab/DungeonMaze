package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import dungeon.Dungeon;
import dungeon.Player;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;

/**
 * A class to test the dungeon class.
 */
public class DungeonTest {
  private Dungeon testDungeon;
  private Random random;

  /**
   * the dungeon object is instantiated.
   */
  @Before
  public void setUp() {
    final int[] randomSequence = {5, 14, 8, 4, 10, 12, 15, 3, 9, 1, 11, 6, 13, 2, 7, 21, 17, 16,
                                     23, 20, 18, 22, 19, 24};
    random = new PredictableRandom(randomSequence);
    testDungeon = new Dungeon(4, 4, 3, true, 10, 10, random);
  }

  @Test
  public void constructorTest() {
    assertEquals(testDungeon.getClass(), Dungeon.class);
  }

  @Test
  public void constructorTestForNonWrapping() {
    final int[] randomSequence1 = {5, 14, 8, 4, 10, 12, 15, 3, 9, 1, 11, 6, 13, 2, 7, 21, 17, 16,
                                      23, 20, 18, 22, 19, 24};
    random = new PredictableRandom(randomSequence1);
    testDungeon = new Dungeon(4, 4, 3, false, 10, 20, random);
    assertEquals(testDungeon.getClass(), Dungeon.class);
  }


  @Test(expected = IllegalArgumentException.class)
  public void invalidRowConstructorTest() {
    testDungeon = new Dungeon(0, 4, 3, true, 10, 10, random);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidColumnConstructorTest() {
    testDungeon = new Dungeon(4, 0, 3, true, 10, 10, random);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidInterconnectivityConstructorTest() {
    testDungeon = new Dungeon(2, 2, 8, true, 10, 10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidLowTreasurePercentageConstructorTest() {
    testDungeon = new Dungeon(2, 2, 8, true, 10, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidHighTreasurePercentageConstructorTest() {
    testDungeon = new Dungeon(2, 2, 8, true, 10, 101);
  }

  @Test
  public void testCaves() {
    final int[] newRandomSequence = {5, 2, 4, 1, 3};
    Random newRandom = new PredictableRandom(newRandomSequence);
    testDungeon = new Dungeon(2, 2, 0, true, 10, 10, newRandom);
    assertEquals(2, testDungeon.getCaves().size());
    List<Integer> actualCaveIds = testDungeon.getCaves().stream()
            .map(cave -> cave.getIdByColSize(2)).collect(Collectors.toList());
    List<Integer> expectedCaveIds = Arrays.asList(2, 3);
    assertEquals(expectedCaveIds, actualCaveIds);
  }

  @Test
  public void testTunnels() {
    final int[] newRandomSequence = {5, 2, 4, 1, 3};
    Random newRandom = new PredictableRandom(newRandomSequence);
    testDungeon = new Dungeon(2, 2, 0, true, 10, 10, newRandom);
    assertEquals(2, testDungeon.getTunnels().size());
    List<Integer> actualTunnelIds = testDungeon.getTunnels().stream()
            .map(tunnel -> tunnel.getIdByColSize(2)).collect(Collectors.toList());
    List<Integer> expectedTunnelIds = Arrays.asList(0, 1);
    assertEquals(expectedTunnelIds, actualTunnelIds);
  }

  @Test
  public void testTreasureAndArrowAssignment() {
    final int[] newRandomSequence = {5, 2, 4, 1, 3, 2, 1, 1, 0, 1, 1, 0, 1};
    Random newRandom = new PredictableRandom(newRandomSequence);
    testDungeon = new Dungeon(2, 2, 0, true, 10, 10, newRandom);
    testDungeon.assignTreasuresAndArrows();
    int actualNoOfCavesWithTreasure = (int) testDungeon.getCaves().stream()
            .filter(cave -> cave.getTreasures().size() > 0).count();
    assertEquals(1, actualNoOfCavesWithTreasure);
    assertEquals(1, testDungeon.getLocations()[0][0].getNumOfArrowsPresent());

    final int[] newRandomSequence1 = {5, 2, 4, 1, 3, 2, 1, 1, 1, 2, 1, 1, 2, 2, 2};
    Random newRandom1 = new PredictableRandom(newRandomSequence1);
    testDungeon = new Dungeon(2, 2, 0, true, 100, 10, newRandom1);
    testDungeon.assignTreasuresAndArrows();
    actualNoOfCavesWithTreasure = (int) testDungeon.getCaves().stream()
            .filter(cave -> cave.getTreasures().size() > 0).count();
    assertEquals(2, actualNoOfCavesWithTreasure);
    assertEquals(1, testDungeon.getLocations()[0][0].getNumOfArrowsPresent());
    assertEquals(1, testDungeon.getLocations()[1][0].getNumOfArrowsPresent());
  }

  @Test
  public void testAddStart() {
    final int[] newRandomSequence = {5, 2, 4, 1, 3, 2, 1};
    Random newRandom = new PredictableRandom(newRandomSequence);
    testDungeon = new Dungeon(2, 2, 0, true, 10, 10, newRandom);
    testDungeon.addStart();
    assertEquals(3, testDungeon.getStart().getIdByColSize(2));
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidEndWithoutStart() {
    final int[] newRandomSequence = {5, 2, 4, 1, 3, 2, 1};
    Random newRandom = new PredictableRandom(newRandomSequence);
    testDungeon = new Dungeon(2, 2, 0, true, 10, 10, newRandom);
    testDungeon.addEnd();
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidEnd() {
    final int[] newRandomSequence = {5, 2, 4, 1, 3, 2, 1};
    Random newRandom = new PredictableRandom(newRandomSequence);
    testDungeon = new Dungeon(2, 2, 0, true, 10, 10, newRandom);
    testDungeon.addStart();
    testDungeon.addEnd();
  }

  @Test
  public void testValidEnd() {
    final int[] randomSequence1 = {5, 14, 8, 4, 10, 12, 15, 3, 9, 1, 11, 6, 13, 2, 7, 21, 17, 16,
                                      8, 20, 18, 22, 19, 24, 28, 27, 28, 30, 34, 36, 38, 42, 45,
                                      40, 43, 44, 47, 1};
    random = new PredictableRandom(randomSequence1);
    testDungeon = new Dungeon(6, 4, 0, true, 10, 10, random);
    testDungeon.addStart();
    testDungeon.addEnd();
    assertEquals(2, testDungeon.getStart().getIdByColSize(4));
    assertEquals(5, testDungeon.getEnd().getIdByColSize(4));
  }

  @Test
  public void testMoveForWrapping() {
    final int[] newRandomSequence = {5, 2, 4, 1, 3, 2, 1};
    Random newRandom = new PredictableRandom(newRandomSequence);
    testDungeon = new Dungeon(2, 2, 0, true, 10, 10, newRandom);
    testDungeon.addStart();
    testDungeon.addPlayer(new Player(1));
    testDungeon.movePlayer("N");
    assertEquals(1, testDungeon.getPlayer().getCurrLocation().getIdByColSize(2));
  }

  @Test
  public void testMoveForNonWrapping() {
    final int[] newRandomSequence = {4, 2, 4, 1, 2, 1};
    Random newRandom = new PredictableRandom(newRandomSequence);
    testDungeon = new Dungeon(2, 2, 0, false, 10, 10, newRandom);
    testDungeon.addStart();
    testDungeon.addPlayer(new Player(1));
    testDungeon.movePlayer("W");
    assertEquals(2, testDungeon.getPlayer().getCurrLocation().getIdByColSize(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMove() {
    final int[] newRandomSequence = {5, 2, 4, 1, 3, 2, 1};
    Random newRandom = new PredictableRandom(newRandomSequence);
    testDungeon = new Dungeon(2, 2, 0, true, 10, 10, newRandom);
    testDungeon.addStart();
    testDungeon.addPlayer(new Player(1));
    testDungeon.movePlayer("S");
    assertEquals(3, testDungeon.getPlayer().getCurrLocation().getIdByColSize(2));
  }

  @Test
  public void testMonsterSetUp() {
    final int[] randomSequence1 = {5, 14, 8, 4, 10, 12, 15, 3, 9, 1, 11, 6, 13, 2, 7, 21, 17, 16,
        8, 20, 18, 22, 19, 24, 28, 27, 28, 30, 34, 36, 38, 42, 45,
        40, 43, 44, 47, 1, 5, 7};
    random = new PredictableRandom(randomSequence1);
    testDungeon = new Dungeon(6, 4, 0, true, 10, 2, random);
    testDungeon.addStart();
    testDungeon.addEnd();
    assertEquals(2, testDungeon.getStart().getIdByColSize(4));
    assertEquals(5, testDungeon.getEnd().getIdByColSize(4));
    testDungeon.assignMonsters();
    assertEquals(2, testDungeon.getMonsters().size());
  }

  @Test
  public void testMonsterIsAtEnd() {
    final int[] randomSequence1 = {5, 14, 8, 4, 10, 12, 15, 3, 9, 1, 11, 6, 13, 2, 7, 21, 17, 16,
        8, 20, 18, 22, 19, 24, 28, 27, 28, 30, 34, 36, 38, 42, 45,
        40, 43, 44, 47, 1, 5, 7};
    random = new PredictableRandom(randomSequence1);
    testDungeon = new Dungeon(6, 4, 0, true, 10, 2, random);
    testDungeon.addStart();
    testDungeon.addEnd();
    testDungeon.assignMonsters();
    assertTrue(testDungeon.getEnd().hasOtyugh());
  }


  @Test
  public void testMonsterNotPresentAtStart() {
    final int[] randomSequence1 = {5, 14, 8, 4, 10, 12, 15, 3, 9, 1, 11, 6, 13, 2, 7, 21, 17, 16,
        8, 20, 18, 22, 19, 24, 28, 27, 28, 30, 34, 36, 38, 42, 45,
        40, 43, 44, 47, 1, 5, 7};
    random = new PredictableRandom(randomSequence1);
    testDungeon = new Dungeon(6, 4, 0, true, 10, 2, random);
    testDungeon.addStart();
    testDungeon.addEnd();
    testDungeon.assignMonsters();
    assertFalse(testDungeon.getStart().hasOtyugh());
  }

  @Test
  public void testDifficultyMoreThanCaves() {
    final int[] randomSequence1 = {5, 14, 8, 4, 10, 12, 15, 3, 9, 1, 11, 6, 13, 2, 7, 21, 17, 16,
        8, 20, 18, 22, 19, 24, 28, 27, 28, 30, 34, 36, 38, 42, 45,
        40, 43, 44, 47, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
    random = new PredictableRandom(randomSequence1);
    testDungeon = new Dungeon(6, 4, 0, true, 10, 14, random);
    testDungeon.addStart();
    testDungeon.addEnd();
    assertEquals(2, testDungeon.getStart().getIdByColSize(4));
    assertEquals(5, testDungeon.getEnd().getIdByColSize(4));
    testDungeon.assignMonsters();
    assertEquals(13, testDungeon.getCaves().size());
    assertEquals(12, testDungeon.getMonsters().size());
  }

}