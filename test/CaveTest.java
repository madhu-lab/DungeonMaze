package test;

import static org.junit.Assert.assertEquals;

import dungeon.AbstractLocation;
import dungeon.Cave;
import org.junit.Before;
import org.junit.Test;


/**
 * a class to test the cave.
 */
public class CaveTest {
  private Cave cave;

  /**
   * cave is instantiated.
   */
  @Before
  public void setUp()  {
    AbstractLocation location = new AbstractLocation(2, 4);
    cave = new Cave(location);
  }

  @Test
  public void constructorTest() {
    assertEquals(cave.getClass(), Cave.class);
  }

}