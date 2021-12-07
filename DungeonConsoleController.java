package dungeon;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * A class for the dungeon controller console.
 */
public class DungeonConsoleController implements DungeonController {
  private final Appendable out;
  private final Scanner scan;

  /**
   * to construct the console.
   *
   * @param in  the input.
   * @param out the output.
   */
  public DungeonConsoleController(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);
  }

  /**
   * Execute a single game of Dungeon given a Dungeon Model. When the game is over, the playGame
   * method ends.
   *
   * @param dungeon a non-null Dungeon Model
   */
  @Override
  public void playGame(Dungeon dungeon, Player player) {
    if (dungeon == null || player == null) {
      throw new IllegalArgumentException("Given model is null.");
    }
    try {
      while (!dungeon.isGameOver()) {
        out.append(player.toString());
        String move1 = null;
        while (move1 == null) {
          out.append("\n\nMove, Pickup, or Shoot (M-P-S)?\n");
          move1 = scan.nextLine();
          switch (move1.toUpperCase().trim()) {
            case "M":
              out.append("Where to?\n");
              String move2 = scan.nextLine();
              List<String> nextPossibleMoves = player.getCurrLocation().getPossibleMoves()
                      .stream().map(Direction::getShortCode).collect(Collectors.toList());
              while (!nextPossibleMoves.contains(move2.toUpperCase())) {
                out.append("Invalid move: ").append(move2).append("\n");
                move2 = scan.nextLine();
              }
              dungeon.movePlayer(move2);
              break;

            case "P":
              List<String> possiblePickUpObjects = player.getCurrLocation()
                      .getPossiblePickupObjects();
              if (possiblePickUpObjects.size() == 0) {
                out.append("Invalid option. There is no item to pick up in current location.\n");
                move1 = null;
                break;
              }
              out.append("What?\n");
              String pickUpObject = scan.nextLine();
              while (!possiblePickUpObjects.contains(pickUpObject.toLowerCase())) {
                out.append("Invalid pick up object: ").append(pickUpObject).append("\n");
                pickUpObject = scan.nextLine();
              }
              player.pickupObject(pickUpObject);
              out.append("You pick up a ").append(pickUpObject).append("\n");
              break;

            case "S":
              if (player.getNumberOfArrows() < 1) {
                out.append("Invalid choice. No arrow available to shoot.\n");
                move1 = null;
                break;
              }
              out.append("No. of caves (1-5)?");
              int shootDistance = 0;
              while (!(shootDistance >= 1 && shootDistance <= 5)) {
                try {
                  shootDistance = Integer.parseInt(scan.nextLine());
                } catch (NumberFormatException e) {
                  shootDistance = 0;
                }
                if (!(shootDistance >= 1 && shootDistance <= 5)) {
                  out.append("Invalid input for shoot distance.\n");
                }
              }
              out.append("Where to?\n");
              Direction shootDirection = null;
              while (shootDirection == null) {
                String dir = scan.nextLine();
                shootDirection = getDirectionByShortCode(dir);
                if (shootDirection == null) {
                  out.append("Invalid input for shoot direction.\n");
                }
              }
              boolean isSuccessfulShoot = player.shoot(dungeon, shootDirection, shootDistance);
              if (isSuccessfulShoot) {
                out.append("You hear a great howl in the distance\n");
              } else {
                out.append("You shoot an arrow into the darkness\n");
              }
              break;
            default:
              out.append("Invalid choice: ").append(move1).append("\n");
              move1 = null;
              break;
          }
        }
        out.append("\n\n");
      }
      if (dungeon.isPlayerEatenByMonster()) {
        out.append("Chomp, chomp, chomp, you are eaten by an Otyugh!\n");
        out.append("Better luck next time\n");
      }
      if (dungeon.hasPlayerWon()) {
        out.append("Yay! You won!\n");
      }
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }

  private Direction getDirectionByShortCode(String str) {
    switch (str.toUpperCase()) {
      case "N":
        return Direction.NORTH;
      case "S":
        return Direction.SOUTH;
      case "E":
        return Direction.EAST;
      case "W":
        return Direction.WEST;
      default:
        return null;
    }
  }
}

