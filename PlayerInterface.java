package dungeon;

import java.util.List;

/**
 * Interface for the players.
 */
public interface PlayerInterface {


  void addTreasure(Treasure treasure);

  void incrementArrowCount();

  void decrementArrowCount();

  AbstractLocation getCurrLocation();

  int getNumberOfArrows();

  void pickupObject(String objectShortCode);

  void pickUpArrow();

  void pickUpTreasure(Treasure treasure);

  boolean shoot(Dungeon dungeon, Direction shootDirection, int shootDistance);

  void setCurrLocation(AbstractLocation currLocation);

  List<Treasure> getTreasures();

}
