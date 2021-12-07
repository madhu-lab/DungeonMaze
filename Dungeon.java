package dungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * A class to create a dungeon with a player.
 */
public class Dungeon implements DungeonInterface {
  private final Random rand;

  private final AbstractLocation[][] locations;
  private final int rowSize;
  private final int colSize;
  private final int interconnectivity;
  private final boolean isWrapping;
  private final int treasurePercent;
  private final int difficulty;
  private final List<Cave> caves;
  private final List<Tunnel> tunnels;
  private Cave start;
  private Cave end;
  private Player player;
  private final List<Monster> monsters;

  /**
   * A dungeon class to represent the dungeon.
   *
   * @param rows              the rows of the dungeon.
   * @param cols              the columns of the dungeon.
   * @param interconnectivity the interconnectivity of the dungeon.
   * @param isWrapping        if the dungeon is wrapping or non-wrapping.
   * @param treasurePercent   percent of treasure in the dungeon.
   * @throws IllegalArgumentException for invalid values.
   */
  public Dungeon(int rows, int cols, int interconnectivity, boolean isWrapping,
                 int treasurePercent, int difficulty) throws IllegalArgumentException {
    this(rows, cols, interconnectivity, isWrapping, treasurePercent, difficulty, new Random());
  }

  /**
   * A dungeon class to represent the dungeon.
   *
   * @param rows              the rows of the dungeon.
   * @param cols              the columns of the dungeon.
   * @param interconnectivity the interconnectivity of the dungeon.
   * @param isWrapping        if the dungeon is wrapping or non-wrapping.
   * @param treasurePercent   percent of treasure in the dungeon.
   * @throws IllegalArgumentException for invalid values.
   */
  public Dungeon(int rows, int cols, int interconnectivity, boolean isWrapping,
                 int treasurePercent, int difficulty, Random rand) throws IllegalArgumentException {
    if (rows < 1 || cols < 1 || interconnectivity < 0 || treasurePercent < 0
            || treasurePercent > 100) {
      throw new IllegalArgumentException("invalid arguments to create a dungeon");
    }
    this.rowSize = rows;
    this.colSize = cols;
    this.isWrapping = isWrapping;
    this.interconnectivity = interconnectivity;
    this.rand = rand;

    final DungeonHelper dungeonHelper = new DungeonHelper();
    this.locations = dungeonHelper.buildAbstractLocationsWithConnectivity(rows, cols, isWrapping,
            interconnectivity, rand);

    this.caves = new ArrayList<>();
    this.tunnels = new ArrayList<>();
    dungeonHelper.setLocationsByType(locations, caves, tunnels);

    this.treasurePercent = treasurePercent;
    this.difficulty = difficulty;
    this.monsters = new ArrayList<>();
  }

  /**
   * assign treasure and arrows to the caves.
   */
  public void assignTreasuresAndArrows() {
    int numberOfCavesWithTreasuresAndArrows = Math.min(caves.size(),
            (caves.size() * this.treasurePercent / 100) + 1);
    for (int i = 0; i < numberOfCavesWithTreasuresAndArrows; i++) {
      caves.get(i).addTreasures(getRandomTreasure());
    }
    for (int i = 0; i < numberOfCavesWithTreasuresAndArrows; i++) {
      final int x = this.rand.nextInt(rowSize);
      final int y = this.rand.nextInt(colSize);
      locations[x][y].addArrows();
    }
  }

  /**
   * assign monsters to the caves.
   */
  public void assignMonsters() {
    end.addMonster();
    monsters.add(end.getMonster());
    int i = Math.min(caves.size() - 1, difficulty) - 1;
    while (i > 0) {
      Cave randomCave = caves.get(this.rand.nextInt(caves.size()));
      if (!randomCave.hasOtyugh() && !randomCave.equals(start)) {
        randomCave.addMonster();
        monsters.add(randomCave.getMonster());
        i--;
      }
    }
  }

  private Treasure getRandomTreasure() {
    int randTreasure = this.rand.nextInt(4);
    switch (randTreasure) {
      case 1:
        return Treasure.DIAMOND;
      case 2:
        return Treasure.RUBIES;
      default:
        return Treasure.SAPPHIRES;
    }
  }

  /**
   * return the caves.
   *
   * @return the list of caves.
   */
  public List<Cave> getCaves() {
    return new ArrayList<>(caves);
  }

  /**
   * the tunnels of the dungeons.
   *
   * @return list of tunnels.
   */
  public List<Tunnel> getTunnels() {
    return new ArrayList<>(tunnels);
  }

  /**
   * add player to the location.
   *
   * @param player the player added.
   */
  public void addPlayer(Player player) {
    if (player == null) {
      throw new IllegalArgumentException("Player is not valid to be added to the dungeon");
    } else if (this.player != null) {
      throw new IllegalStateException("A player is already present in the dungeon");
    } else if (this.start == null) {
      throw new IllegalStateException("No start location is defined to put the player on");
    } else {
      this.player = player;
      player.setCurrLocation(start);
    }
  }

  /**
   * add the start location to dungeon.
   */
  public void addStart() {
    if (this.start != null) {
      throw new IllegalStateException("Dungeon already assigned with a start location");
    } else {
      this.start = this.caves.get(this.rand.nextInt(caves.size()));
    }
  }

  /**
   * add the end location to dungeon.
   */
  public void addEnd() {
    if (this.start == null) {
      throw new IllegalStateException("Dungeon is not yet assigned a start location");
    } else if (this.end != null) {
      throw new IllegalStateException("Dungeon is already assigned an end location");
    }
    AbstractLocation location = start;
    Stack<AbstractLocation> bfsStack = new Stack<>();
    List<Cave> res = new ArrayList<>();
    List<AbstractLocation> processedLocations = new ArrayList<>();
    bfsStack.add(location);
    int minimumPath = 5;
    while (!bfsStack.isEmpty() && (minimumPath > 0
            || !res.isEmpty() || !processedLocations.containsAll(caves))) {
      AbstractLocation currLoc = bfsStack.pop();
      if (processedLocations.contains(currLoc)) {
        continue;
      }
      processedLocations.add(currLoc);
      final List<AbstractLocation> unProcessedNeighbors = getUnprocessedNeighbors(currLoc,
              processedLocations);
      bfsStack.addAll(unProcessedNeighbors);
      minimumPath--;
      if (minimumPath <= 0) {
        List<Cave> newCaveList = unProcessedNeighbors.stream()
                .filter(e -> e instanceof Cave)
                .map(e -> (Cave) e)
                .collect(Collectors.toList());
        if (!newCaveList.isEmpty()) {
          res.addAll(newCaveList);
        }
      }
    }
    if (!res.isEmpty()) {
      this.end = res.get(0);
    } else {
      throw new IllegalStateException("No end cave can be assigned with 5 as minimum path");
    }
  }

  /**
   * to get the minimum paths.
   *
   * @param location1 the location of the start.
   * @param location2 the end location.
   * @return the minimum paths.
   */
  private int getMinimumPathBetweenLocations(Location location1,
                                             Location location2) {
    final int rowDistance = Math.abs(location1.getRowDim() - location2.getRowDim());
    final int colDistance = Math.abs(location1.getColDim() - location2.getColDim());
    if (!this.isWrapping) {
      return rowDistance + colDistance;
    } else {
      final int wrappingRowDistance = Math.min(rowDistance, rowSize - rowDistance);
      final int wrappingColDistance = Math.min(colDistance, colSize - colDistance);
      return wrappingRowDistance + wrappingColDistance;
    }
  }

  /**
   * return the start location.
   *
   * @return location.
   */
  public Cave getStart() {
    return this.start;
  }

  /**
   * return the end location.
   *
   * @return location.
   */
  public Cave getEnd() {
    return this.end;
  }

  /**
   * to get the locations.
   *
   * @return the locations of the dungeon.
   */
  public AbstractLocation[][] getLocations() {
    return locations;
  }

  /**
   * to get the next location.
   *
   * @param originLocation the location
   * @param direction      the direction
   * @return the location
   */
  public AbstractLocation getNextLocationByDirection(AbstractLocation originLocation,
                                                     Direction direction) {
    int row = originLocation.getRowDim();
    int column = originLocation.getColDim();
    switch (direction) {
      case NORTH:
        if (row == 0 && isWrapping) {
          row = rowSize - 1;
        } else {
          row--;
        }
        break;
      case SOUTH:
        if (row == rowSize - 1 && isWrapping) {
          row = 0;
        } else {
          row++;
        }
        break;
      case WEST:
        if (column == 0 && isWrapping) {
          column = colSize - 1;
        } else {
          column--;
        }
        break;
      case EAST:
        if (column == colSize - 1 && isWrapping) {
          column = 0;
        } else {
          column++;
        }
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + direction);
    }
    return locations[row][column];
  }

  /**
   * the movement of the player.
   *
   * @param directionCode the direction code for the next move.
   */
  public void movePlayer(String directionCode) throws IllegalArgumentException {
    Direction direction;
    switch (directionCode.toUpperCase()) {
      case "N":
        direction = Direction.NORTH;
        break;
      case "S":
        direction = Direction.SOUTH;
        break;
      case "W":
        direction = Direction.WEST;
        break;
      case "E":
        direction = Direction.EAST;
        break;
      default:
        throw new IllegalArgumentException("Invalid Direction code");
    }
    AbstractLocation playerCurrLocation = this.player.getCurrLocation();
    if (!playerCurrLocation.getPossibleMoves().contains(direction)) {
      throw new IllegalArgumentException("Invalid Direction code");
    }
    this.player.setCurrLocation(getNextLocationByDirection(playerCurrLocation, direction));
  }

  /**
   * to set the location smell level.
   *
   * @param location        the location to set
   * @param isIncrementFlag the increment flag.
   */
  public void setLocationSmellLevel(AbstractLocation location, boolean isIncrementFlag) {
    int smellLevel = 3;
    List<AbstractLocation> processedLocations = new ArrayList<>();
    Stack<AbstractLocation> bfsStack = new Stack<>();
    bfsStack.add(location);
    while (!bfsStack.isEmpty() && smellLevel > 0) {
      AbstractLocation currLoc = bfsStack.pop();
      if (processedLocations.contains(currLoc)) {
        continue;
      }
      if (isIncrementFlag) {
        currLoc.incrementSmellLevel(smellLevel);
      } else {
        currLoc.decrementSmellLevel(smellLevel);
      }
      processedLocations.add(currLoc);
      smellLevel--;
      final List<AbstractLocation> unProcessedNeighbors = getUnprocessedNeighbors(location,
              processedLocations);
      List<AbstractLocation> caves = unProcessedNeighbors.stream()
              .filter(e -> e instanceof Cave).collect(Collectors.toList());
      List<AbstractLocation> tunnels = unProcessedNeighbors.stream()
              .filter(e -> e instanceof Tunnel).collect(Collectors.toList());
      bfsStack.addAll(caves);
      final int tunnelSmellLevel = smellLevel;
      tunnels.forEach(t -> setSmellLevelByDfsForTunnels(t, tunnelSmellLevel, bfsStack,
              processedLocations, isIncrementFlag));
    }
  }

  private void setSmellLevelByDfsForTunnels(AbstractLocation tunnelLocation, int smellLevel,
                                            Stack<AbstractLocation> bfsStack,
                                            List<AbstractLocation> processedLocations,
                                            boolean isIncrementFlag) {
    if (isIncrementFlag) {
      tunnelLocation.incrementSmellLevel(smellLevel);
    } else {
      tunnelLocation.decrementSmellLevel(smellLevel);
    }
    processedLocations.add(tunnelLocation);
    List<AbstractLocation> unProcessedNeighbors = getUnprocessedNeighbors(tunnelLocation,
            processedLocations);
    List<AbstractLocation> caves = unProcessedNeighbors.stream()
            .filter(e -> e instanceof Cave).collect(Collectors.toList());
    bfsStack.addAll(caves);
    List<AbstractLocation> tunnels = unProcessedNeighbors.stream()
            .filter(e -> e instanceof Tunnel).collect(Collectors.toList());
    tunnels.forEach(t -> setSmellLevelByDfsForTunnels(t, smellLevel, bfsStack,
            processedLocations, isIncrementFlag));
  }

  private List<AbstractLocation> getUnprocessedNeighbors(AbstractLocation
                                                                 location, List<AbstractLocation>
                                                                 processedLocations) {
    return location.getPossibleMoves().stream()
            .map(direction -> getNextLocationByDirection(location, direction))
            .filter(e -> !processedLocations.contains(e))
            .collect(Collectors.toList());
  }

  /**
   * a new Player which is a copy of the player in the dungeon.
   *
   * @return a Player in this dungeon.
   */
  public Player getPlayer() {
    return new Player(this.player);
  }

  /**
   * to get the monsters.
   *
   * @return the list of monsters.
   */
  public List<Monster> getMonsters() {
    return this.monsters;
  }

  /**
   * to check if the game over.
   *
   * @return the boolean value.
   */
  public boolean isGameOver() {
    return (isPlayerEatenByMonster() || hasPlayerWon());
  }

  /**
   * to check if the player won.
   *
   * @return the boolean value
   */
  public Boolean hasPlayerWon() {
    return (player.getCurrLocation().equals(end) && !isPlayerEatenByMonster());
  }

  /**
   * to check if the player is eaten.
   *
   * @return the boolean value.
   */
  public Boolean isPlayerEatenByMonster() {
    AbstractLocation playerCurrLocation = player.getCurrLocation();
    if (playerCurrLocation instanceof Cave && ((Cave) playerCurrLocation).hasOtyugh()) {
      int monsterHealth = ((Cave) playerCurrLocation).getMonster().getHealth();
      return monsterHealth == 100 || (monsterHealth == 50 && player.getNumberOfArrows() < 1);
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    String wrapName = isWrapping ? "wrapping" : "non-wrapping";
    StringBuilder s;
    s = new StringBuilder();
    s.append(rowSize).append("*").append(colSize).append(" ").append(wrapName)
            .append(" dungeon is created with ").append(caves.size()).append(" caves & ")
            .append(tunnels.size()).append(" tunnels with interconnectivity ")
            .append(interconnectivity).append(" and difficulty ").append(difficulty)
            .append(". \n").append("Start position is: ")
            .append(start.getRowDim()).append(", ").append(start.getColDim()).append("\n")
            .append("End position is: ").append(end.getRowDim()).append(", ")
            .append(end.getColDim()).append("\n");
    return s.toString();
  }

}