package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import dungeon.AbstractLocation;
import org.junit.Before;
import org.junit.Test;

/**
 * a class to test the abstract location.
 */
public class AbstractLocationTest {
  private AbstractLocation locationTest;

  /**
   * to instantiate a location object.
   */
  @Before
  public void setUp() {
    locationTest = new AbstractLocation(2, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidConstructorTest() {
    locationTest = new AbstractLocation(-2, 3);
  }

  @Test
  public void constructorTest() {
    assertEquals(locationTest.getClass(), AbstractLocation.class);
  }

  @Test
  public void hasWallUpSideTest() {
    assertTrue(locationTest.hasWallUpSide());
    locationTest.setUp(false);
    assertFalse(locationTest.hasWallUpSide());
  }

  @Test
  public void hasWallUpDownTest() {
    assertTrue(locationTest.hasWallDownSide());
    locationTest.setDown(false);
    assertFalse(locationTest.hasWallDownSide());
  }

  @Test
  public void hasWallRightSideTest() {
    assertTrue(locationTest.hasWallRightSide());
    locationTest.setRight(false);
    assertFalse(locationTest.hasWallRightSide());
  }

  @Test
  public void hasWallLeftSideTest() {
    assertTrue(locationTest.hasWallLeftSide());
    locationTest.setLeft(false);
    assertFalse(locationTest.hasWallLeftSide());
  }

  @Test
  public void getRowDimTest() {
    assertEquals(2, locationTest.getRowDim());
  }

  @Test
  public void getColDimTest() {
    assertEquals(3, locationTest.getColDim());
  }

  @Test
  public void getNumberOfEntrance() {
    assertEquals(0, locationTest.getNumberOfEntrance());
    locationTest.setLeft(false);
    assertEquals(1, locationTest.getNumberOfEntrance());
  }


}