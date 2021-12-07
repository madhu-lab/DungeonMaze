package test;

import static org.junit.Assert.assertEquals;

import dungeon.AbstractLocation;
import dungeon.Tunnel;
import org.junit.Before;
import org.junit.Test;


/**
 * a class to test the tunnel.
 */
public class TunnelTest {
  private Tunnel tunnel;

  /**
   * tunnel class is instantiated.
   */
  @Before
  public void setUp()  {
    AbstractLocation location = new AbstractLocation(2, 4);
    tunnel = new Tunnel(location);
  }

  @Test
  public void constructorTest() {
    assertEquals(tunnel.getClass(), Tunnel.class);
  }
}