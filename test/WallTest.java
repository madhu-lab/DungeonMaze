package test;

import static org.junit.Assert.assertEquals;

import dungeon.Orientation;
import dungeon.Wall;
import org.junit.Before;
import org.junit.Test;


/**
 * a class to test the wall.
 */
public class WallTest {
  private Wall wallTest;

  /**
   * a wall is instantiated.
   */
  @Before
  public void setUp() {

    wallTest = new Wall(2, 4, Orientation.VERTICAL);
  }

  @Test
  public void constructorTest() {
    assertEquals(wallTest.getClass(), Wall.class);
  }


  @org.junit.Test(expected = IllegalArgumentException.class)
  public void invalidConstructorTest() {
    wallTest = new Wall(-2, 4, Orientation.VERTICAL);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void invalidConstructorTest2() {
    wallTest = new Wall(2, -4, Orientation.VERTICAL);
  }

  @org.junit.Test
  public void getRow() {
    assertEquals(2, wallTest.getRow());
  }


  @org.junit.Test
  public void getCol() {
    assertEquals(4, wallTest.getCol());
  }

  @org.junit.Test
  public void getOrientation() {
    assertEquals(Orientation.VERTICAL, wallTest.getOrientation());
  }
}