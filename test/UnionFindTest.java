package test;

import static org.junit.Assert.assertEquals;

import dungeon.UnionFind;
import org.junit.Before;
import org.junit.Test;

/**
 * class to test the union find.
 */
public class UnionFindTest {
  private UnionFind unionTest;

  /**
   * union find data is instantiated.
   */
  @Before
  public void setUp() {
    unionTest = new UnionFind(3);
  }

  @Test
  public void constructorTest() {
    assertEquals(unionTest.getClass(), UnionFind.class);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidConstructorTest() {
    unionTest = new UnionFind(-3);
  }

  @Test
  public void find() {
    assertEquals(1, unionTest.find(1));
  }

  @Test
  public void union() {
    assertEquals(3, unionTest.getNumSubsets());
    unionTest.union(1, 2);
    assertEquals(2, unionTest.getNumSubsets());
  }

  @Test
  public void getNumSubsets() {
    assertEquals(3, unionTest.getNumSubsets());
    unionTest.union(1, 2);
    assertEquals(2, unionTest.getNumSubsets());
    unionTest.union(1, 2);
    assertEquals(2, unionTest.getNumSubsets());

  }
}