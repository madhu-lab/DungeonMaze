package test;

import static org.junit.Assert.assertEquals;

import dungeon.AbstractLocation;
import dungeon.Cave;
import dungeon.Player;
import dungeon.Treasure;
import dungeon.Tunnel;

import org.junit.Before;
import org.junit.Test;

/**
 * a class to test the player class.
 */
public class PlayerTest {
  private Player player;
  private AbstractLocation currLocation;

  /**
   * to instantiate the player class.
   */
  @Before
  public void setUp() {
    player = new Player(2);
    currLocation = new Cave(new AbstractLocation(2, 3, false, true,
            false, false));
  }

  @Test
  public void testConstructor() {
    assertEquals(true, player instanceof Player);
    assertEquals(3, player.getNumberOfArrows());
  }

  @Test
  public void addTreasuresTest() {
    assertEquals(0, player.getTreasures().size());
    player.addTreasure(Treasure.RUBIES);
    assertEquals(1, player.getTreasures().size());
  }

  @Test
  public void getCurrLocation() {
    player.setCurrLocation(currLocation);
    assertEquals(player.getCurrLocation().getClass(), currLocation.getClass());
  }

  @Test
  public void setCurrLocation() {
    player.setCurrLocation(currLocation);
    assertEquals(player.getCurrLocation().getClass(), currLocation.getClass());
  }

  @Test
  public void testIncrementArrowCount() {
    assertEquals(3, player.getNumberOfArrows());
    player.incrementArrowCount();
    assertEquals(4, player.getNumberOfArrows());
  }

  @Test
  public void testDecrementArrowCount() {
    assertEquals(3, player.getNumberOfArrows());
    player.decrementArrowCount();
    assertEquals(2, player.getNumberOfArrows());
  }

  @Test
  public void testValidPickUpTreasure() {
    player.setCurrLocation(currLocation);
    assertEquals(0, player.getTreasures().size());
    ((Cave) currLocation).addTreasures(Treasure.RUBIES);
    player.pickUpTreasure(Treasure.RUBIES);
    assertEquals(1, player.getTreasures().size());
    assertEquals(Treasure.RUBIES, player.getTreasures().get(0));
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidPickUpTreasureNotAvailable() {
    player.setCurrLocation(currLocation);
    assertEquals(0, player.getTreasures().size());
    ((Cave) currLocation).addTreasures(Treasure.RUBIES);
    player.pickUpTreasure(Treasure.DIAMOND);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidPickUpTreasureFromTunnel() {
    AbstractLocation newLocation = new Tunnel(new AbstractLocation(2, 3, false, true,
            false, false));
    player.setCurrLocation(newLocation);
    player.pickUpTreasure(Treasure.RUBIES);
  }

  @Test
  public void testValidPickUpArrow() {
    player.setCurrLocation(currLocation);
    assertEquals(3, player.getNumberOfArrows());
    currLocation.addArrows();
    player.pickUpArrow();
    assertEquals(4, player.getNumberOfArrows());
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidPickUpArrow() {
    player.setCurrLocation(currLocation);
    assertEquals(3, player.getNumberOfArrows());
    assertEquals(0, currLocation.getNumOfArrowsPresent());
    player.pickUpArrow();
  }

  @Test
  public void testToString() {
    player.setCurrLocation(currLocation);
    player.addTreasure(Treasure.RUBIES);
    currLocation.addArrows();
    ((Cave) currLocation).addTreasures(Treasure.RUBIES);
    currLocation.incrementSmellLevel(2);
    assertEquals("You are in a Cave\n"
            + "You smell something terribly nearby.\n"
            + "You can find 1 rubies here.\n"
            + "You can find 1 arrows here.\n"
            + "Tunnels lead to the N, W, E\n"
            + "Your treasures possessed are: \n"
            + "1. DIAMOND = 0\n"
            + "2. RUBIES = 1\n"
            + "3. SAPPHIRES = 0\n"
            + "You have 3 arrows left.\n", player.toString());
  }

}