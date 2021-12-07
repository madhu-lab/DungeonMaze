package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import dungeon.AbstractLocation;
import dungeon.Cave;
import dungeon.Monster;
import dungeon.Otyugh;
import org.junit.Before;
import org.junit.Test;

/**
 * A test for Monster.
 */
public class MonsterTest {

  private Monster testMonster;

  @Before
  public void setUp() {
    testMonster = new Otyugh(new Cave(new AbstractLocation(1, 1)));
  }

  @Test
  public void testConstructor() {
    assertTrue(testMonster instanceof Otyugh);
  }

  @Test
  public void testGetHealth() {
    assertEquals(100, testMonster.getHealth());
  }

  @Test
  public void testSetHealthAfterHit() {
    assertEquals(100, testMonster.getHealth());
    testMonster.setHealthAfterHit();
    assertEquals(50, testMonster.getHealth());
  }

  @Test(expected = IllegalStateException.class)
  public void testSetHealthAfterHitForSlayedMonster() {
    assertEquals(100, testMonster.getHealth());
    testMonster.setHealthAfterHit();
    assertEquals(50, testMonster.getHealth());
    testMonster.setHealthAfterHit();
    assertEquals(0, testMonster.getHealth());
    testMonster.setHealthAfterHit();
  }

  @Test
  public void testSlayedMonster() {
    assertEquals(false, testMonster.isSlayed());
    testMonster.setHealthAfterHit();
    assertEquals(false, testMonster.isSlayed());
    testMonster.setHealthAfterHit();
    assertEquals(true, testMonster.isSlayed());
  }

}
