package dungeon;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A helper class for building the dungeon.
 */
public class DungeonHelper {

  /**
   * build locations with connectivity.
   *
   * @param rowSize           the row of the dungeon.
   * @param colSize           the col of the dungeon.
   * @param isWrapping        wrapping flag.
   * @param interconnectivity interconnectivity of the the dungeon.
   * @param rand              random value.
   * @return the locations grid.
   */
  public AbstractLocation[][] buildAbstractLocationsWithConnectivity(int rowSize, int colSize,
                                                                     boolean isWrapping,
                                                                     int interconnectivity,
                                                                     Random rand) {
    final int numWalls;
    if (isWrapping) {
      numWalls = 2 * rowSize * colSize;
    } else {
      numWalls = 2 * rowSize * colSize - rowSize - colSize;
    }
    final Wall[] walls = new Wall[numWalls];
    final AbstractLocation[][] locations = new AbstractLocation[rowSize][colSize];
    setAbstractLocations(locations, isWrapping, walls);
    generateInterConnectivityForLocations(locations, walls, interconnectivity, rand);
    return locations;
  }

  private void setAbstractLocations(AbstractLocation[][] locations, boolean isWrapping,
                                    Wall[] walls) {
    final int rowSize = locations.length;
    final int colSize = locations[0].length;

    int num = 0;
    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < colSize; j++) {
        locations[i][j] = new AbstractLocation(i, j);
        if (isWrapping) {
          walls[num++] = new Wall(i, j, Orientation.VERTICAL);
          walls[num++] = new Wall(i, j, Orientation.HORIZONTAL);
        } else {
          if (i < rowSize - 1) {
            walls[num++] = new Wall(i, j, Orientation.VERTICAL);
          }
          if (j < colSize - 1) {
            walls[num++] = new Wall(i, j, Orientation.HORIZONTAL);
          }
        }
      }
    }
  }

  private void generateInterConnectivityForLocations(AbstractLocation[][] locations, Wall[] walls,
                                                     int interconnectivity, Random rand) {
    final int rowSize = locations.length;
    final int colSize = locations[0].length;
    // number of locations in the maze
    int numLocations = rowSize * colSize;
    UnionFind unions = new UnionFind(numLocations);
    Set<Wall> brokenWalls = new HashSet<>();
    while (unions.getNumSubsets() > 1) {
      final Wall randWall = walls[rand.nextInt(walls.length)];

      final int aRow = randWall.getRow();
      final int aCol = randWall.getCol();
      final int aLocation = aRow * colSize + aCol;

      final int bLocation;
      if (randWall.getOrientation() == Orientation.VERTICAL) {
        if (aRow != rowSize - 1) {
          bLocation = (aRow + 1) * colSize + aCol;
        } else {
          bLocation = aCol;
        }
        /* If there is no path between the cells, knock down a wall. */
        if (unions.find(aLocation) != unions.find(bLocation)) {
          locations[aRow][aCol].setDown(false);
          if (aRow != rowSize - 1) {
            locations[aRow + 1][aCol].setUp(false);
          } else {
            locations[0][aCol].setUp(false);
          }
          brokenWalls.add(randWall);
          unions.union(aLocation, bLocation);
        }
      } else {
        if (aCol != colSize - 1) {
          bLocation = aRow * colSize + (aCol + 1);
        } else {
          bLocation = aRow * colSize;
        }
        /* If there is no path between the cells, knock down a wall. */
        if (unions.find(aLocation) != unions.find(bLocation)) {
          locations[aRow][aCol].setRight(false);
          if (aCol != colSize - 1) {
            locations[aRow][aCol + 1].setLeft(false);
          } else {
            locations[aRow][0].setLeft(false);
          }
          unions.union(aLocation, bLocation);
          brokenWalls.add(randWall);
        }
      }
    }
    Set<Wall> newWalls = Arrays.stream(walls).collect(Collectors.toSet());
    newWalls.removeAll(brokenWalls);

    final Object[] leftOverWalls = newWalls.toArray();
    if (interconnectivity > leftOverWalls.length) {
      throw new IllegalArgumentException("interconnectivity is exceeding the maximum");
    }
    // add interconnectivity degree
    for (int i = 0; i < interconnectivity; i++) {
      Wall newWall = (Wall) leftOverWalls[i];
      breakWall(locations, newWall);
    }
  }

  private void breakWall(AbstractLocation[][] locations, Wall wall) {
    final int rowSize = locations.length;
    final int colSize = locations[0].length;

    final int wallRow = wall.getRow();
    final int wallCol = wall.getCol();
    if (wall.getOrientation() == Orientation.VERTICAL) {
      locations[wallRow][wallCol].setDown(false);
      if (wallRow != rowSize - 1) {
        locations[wallRow + 1][wallCol].setUp(false);
      } else {
        locations[0][wallCol].setDown(false);
      }
    } else {
      locations[wallRow][wallCol].setRight(false);
      if (wallCol != colSize - 1) {
        locations[wallRow][wallCol + 1].setLeft(false);
      } else {
        locations[wallRow][0].setLeft(false);
      }
    }
  }

  /**
   * to set the locations by type.
   *
   * @param locations the locations grid.
   * @param caves     the caves of the dungeon.
   * @param tunnels   the tunnels of the dungeon.
   */
  public void setLocationsByType(AbstractLocation[][] locations, List<Cave> caves,
                                 List<Tunnel> tunnels) {
    final int rowSize = locations.length;
    final int colSize = locations[0].length;

    for (int i = 0; i < rowSize; i++) {
      for (int j = 0; j < colSize; j++) {
        final AbstractLocation thisLocation = locations[i][j];
        if (thisLocation.getNumberOfEntrance() == 2) {
          Tunnel newTunnel = new Tunnel(thisLocation);
          locations[i][j] = newTunnel;
          tunnels.add(newTunnel);
        } else if (thisLocation.getNumberOfEntrance() == 1
                || thisLocation.getNumberOfEntrance() == 3
                || thisLocation.getNumberOfEntrance() == 4) {
          Cave newCave = new Cave(thisLocation);
          locations[i][j] = newCave;
          caves.add(newCave);
        }
      }
    }
  }

}
