package ad310.battleship;

import java.io.*;
import java.util.*;

/**
 * Implements Battleship game controls and display.
 * @author Duri Balat
 * @author Brad Baumel
 * @author Jeremy Duke
 * @author Nathan Flint
 */

public class BattleshipViewController {
   //Fields
   private BattleshipModel model = new BattleshipModel();
   private boolean playerChanged = false;

   //Default Properties
   private int sideLength = 8;
   ShipType[] ships = {ShipType.AIRCRAFT_CARRIER, ShipType.BATTLESHIP, ShipType.CRUISER, ShipType.DESTROYER1, ShipType.DESTROYER2};

   /**
    * Skeletal main method that stands up a ViewController and walks through major phases of the game
    * @param args String args.  Unused.
    */
   public static void main(String[] args) {
      //Stand up MVC components
      BattleshipViewController bvc = new BattleshipViewController();

      //Welcome!
      bvc.printTitle();
      bvc.getConfigs();
      System.out.println("Finished processing configs");

      //Run through Ship Setup
      bvc.doSetup();
      bvc.printStartGame();

      //When setup is complete...
      bvc.play();
   }

   //Configs
   private void getConfigs() {
      Scanner in = new Scanner(System.in);
      boolean fileFound = false;
      BufferedReader reader;
      FileInputStream fs;
      Properties defaultProps = new Properties();
      Properties userProps;

      //Load default properties if they exist
      try {
         fs = new FileInputStream("default.props");
         defaultProps.load(fs);
         fs.close();
         System.out.println("Found default.props file");
      } catch (IOException e) {
         System.out.println("No default properties file found.  Moving on...\n\n");
      }

      System.out.println("Do you have a config file to load? [Y/N]: ");
      while (!in.hasNext("[ynYN]")) {
         System.out.println("Invalid response.  Please enter 'y' for yes or 'n' for no: ");
         in.next();
      }
      char response = in.next().charAt(0);

      if (response == 'y' || response == 'Y') {
         while (!fileFound) {
            System.out.println("Please enter the name of the config file (eg, config.props) or '0' to exit: ");
            String filename = in.next();
            if (filename.equals("0")) {
               break;
            } else {
               try {
                  userProps = new Properties(defaultProps);
                  fs = new FileInputStream(filename);
                  userProps.load(fs);
                  fs.close();
                  fileFound = true;
                  System.out.println("Found user.props");
               } catch (IOException e) {
                  System.out.println("File not found!");
               }
            }
         }
      }
      //TODO: Parse given json file
      //TODO: Create setters to override default config fields

      //TODO: Stretch goal -- allow user interactive property builder
//      try {
//         FileOutputStream out = new FileOutputStream("user.props");
//         userProps.store(out, "Created " + new Date().getTime());
//         out.close();
//      } catch (IOException e) {
//         System.out.println("There was an error writing user.props to disk.");
//      }

   }

   //Gameplay Methods
   /**
    * Allow the players to setup their board.  All ships are placed by Player 1 before Player 2 places any ships.
    */
   private void doSetup() {
      Player player1 = Player.PLAYER1;
      Player player2 = Player.PLAYER2;
      Board p1def = Board.PLAYER1_DEFENSIVE;
      Board p2def = Board.PLAYER2_DEFENSIVE;
      int time = 1500;

      //Player1 Setup
      printInterstitial(player1);
      //TODO: Loop through ArrayList of Ships and prompt player for each
      promptPlayerSetup(player1, ShipType.AIRCRAFT_CARRIER, p1def);
      promptPlayerSetup(player1, ShipType.BATTLESHIP, p1def);
      promptPlayerSetup(player1, ShipType.CRUISER, p1def);
      promptPlayerSetup(player1, ShipType.DESTROYER1, p1def);
      promptPlayerSetup(player1, ShipType.DESTROYER2, p1def);
      displayBoard(player1, p1def);
      try {
         Thread.sleep(time);
      } catch (InterruptedException e){
         e.printStackTrace();
      }

      //Player2 Setup
      printInterstitial(player2);
      //TODO: Loop through ArrayList of Ships and prompt player for each
      promptPlayerSetup(player2, ShipType.AIRCRAFT_CARRIER, p2def);
      promptPlayerSetup(player2, ShipType.BATTLESHIP, p2def);
      promptPlayerSetup(player2, ShipType.CRUISER, p2def);
      promptPlayerSetup(player2, ShipType.DESTROYER1, p2def);
      promptPlayerSetup(player2, ShipType.DESTROYER2, p2def);
      displayBoard(player2, p2def);
      sleep(time);
      playerChanged = true;
   }

   /**
    * Prompts player for a row and a column.  Handles input validation and case conversion
    * @return a Location object at the requested coordinates
    */
   private Location promptForLocation() {
      Scanner in = new Scanner(System.in);
      boolean validLocation = false;
      char row;
      int  col = 0;
      Location loc = null;

      while (!validLocation) {
         System.out.println("Enter a valid row [A-J]: ");
         while (!in.hasNext("[abcdefghijABCDEFGHIJ]")) {
            System.out.println("Invalid row.  Please enter a valid row [A-J]: ");
            in.next();
         }
         row = in.next().charAt(0);
         //Handle upper casing
         if (row >= 'A' && row <= 'J') {
            row = (char) (row + 32);
         }
         while (col < 1 || col > 10) {
            System.out.println("Enter a valid column [1-10]: ");
            while (!in.hasNextInt()) {
               System.out.println("Invalid column.  Please enter a valid column [1-10]: ");
               in.next();
            }
            col = in.nextInt();
         }
         loc = new Location(col, row);
         validLocation = true;
      }
      return loc;
   }

   /**
    * Walks player through the setup of a ship.  Asks for starting and ending coordinates and checks with model before
    * confirming successful ship placement
    * @param player The current player
    * @param ship The ship to be placed
    * @param b The board on which to place the ship
    */
   private void promptPlayerSetup(Player player, ShipType ship, Board b) {
      boolean validStart = false;
      boolean validEnd   = false;
      Location start = null;
      Location end   = null;
      Square startSquare;
      Square endSquare;

      //Get start coordinate
      while (!validStart) {
         displayBoard(player, b);
         System.out.println(player + "'s Turn!  Place your " + ship + "!\n\n");
         System.out.println("Choose a starting location for your " + ship + "(" + model.numberOfSpacesPerShip(ship) + ")!");
         start = promptForLocation();
         if (start != null) {
            startSquare = model.getSquare(b, start);
            if (startSquare.equals(Square.NOTHING)) {
               validStart = !validStart;
            } else {
               System.out.println("Invalid starting location.  Choose another Location.");
            }
         }
      }
      //Get end coordinate
      while (!validEnd) {
         displayBoard(player, b);
         System.out.println("Choose an ending location for your " + ship + "(" + model.numberOfSpacesPerShip(ship) + ")!  Starting square: " + start);
         end = promptForLocation();
         if (end != null) {
            endSquare = model.getSquare(b, end);
            if (endSquare.equals(Square.NOTHING) && model.placeShip(player, ship, start, end)) {
               validEnd  = !validEnd;
            } else {
               System.out.println("Invalid ending location.  Choose another Location.");
            }
         }
      }
   }

   //Basic game flow logic. Checks with model for Game Over or current player
   private void play() {
      model.startGame();
      while (!model.isGameOver()) {
         Player currentPlayer = model.whoseTurn();
         if (playerChanged) {
            printInterstitial(currentPlayer);
            playerChanged = false;
         }
         promptPlayer(currentPlayer);
      }
   }

   /**
    * Asks player for a move (a square on opponent's defensive board) and returns the result.
    * @param p The player to prompt
    */
   private void promptPlayer(Player p) {
      Board off;
      int time = 1000;
      boolean validMove = false;
      Location shot;

      if (p.equals(Player.PLAYER1)) {
         off = Board.PLAYER1_OFFENSIVE;
      } else {
         off = Board.PLAYER2_OFFENSIVE;
      }

      while (!validMove) {
         displayBoard(p, off);
         System.out.println(p + "'s Turn.  Make your move!");
         shot = promptForLocation();
         Status status = model.markShot(shot);

         switch (status) {
            case MISS:
               System.out.println("SPLOOOSH!  You missed!");
               validMove = !validMove;
               playerChanged = true;
               displayBoard(p, off);
               sleep(time);
               break;
            case SUNK_AIRCRAFT_CARRIER:
               System.out.println("KABOOOM!  You sunk their AIRCRAFT CARRIER!");
               validMove = !validMove;
               sleep(time);
               break;
            case SUNK_BATTLESHIP:
               System.out.println("KABOOOM!  You sunk their BATTLESHIP!");
               validMove = !validMove;
               sleep(time);
               break;
            case SUNK_DESTROYER:
               System.out.println("KABOOOM!  You sunk their DESTROYER!");
               validMove = !validMove;
               sleep(time);
               break;
            case SUNK_CRUISER:
               System.out.println("KABOOOM!  You sunk their CRUISER!");
               validMove = !validMove;
               sleep(time);
               break;
            case HIT:
               System.out.println("KABOOOM!  You scored a hit!  Go again!");
               validMove = !validMove;
               sleep(time);
               break;
            case DO_OVER:
            case NOT_ALLOWED:
               System.out.println("Invalid Move.  Go Again!");
               sleep(time);
               break;
            case PLAYER1_WINS:
            case PLAYER2_WINS:
               printPlayer(p);
               System.out.println("THE WINNER IS YOU!");
               sleep(time * 2);
               validMove = !validMove;
               displayBoard(p, off);
               break;
            default:
               break;
         }
      }
   }

   //DISPLAY METHODS
   /**
    * Renders the current state of the requested board.
    * @param p The current player
    * @param b That player's board
    */
   public void displayBoard(Player p, Board b) {
      String board;
      String title;
      if (b == Board.PLAYER1_OFFENSIVE || b == Board.PLAYER2_OFFENSIVE) {
         title = "OFFENSIVE";
      } else {
         title = "DEFENSIVE";
      }
      board  = "    ======= " + title + " - " + p.toString() + " =======";
      //Rows
      for (int i = 0; i <= sideLength; i++) {
         board += "\n   -";
         //FORMATTING - Horizontal lines
         for (int j = 1; j <= sideLength; j++) {
            board += "---+";
         }
         if (i != sideLength) {
            board += "\n" + (char)(i + 65) + "  |";
            for (int n = 1; n <= sideLength; n++) {
               char row = (char)(i + 97);
               int col = n;
               Location cell = new Location(col, row);
               char val;
               switch (model.getSquare(b, cell)) {
                  case NOTHING:
                     val = ' ';
                     break;
                  case HIT:
                     val = 'X';
                     break;
                  case MISS:
                     val = '0';
                     break;
                  case AIRCRAFT_CARRIER:
                     val = 'A';
                     break;
                  case BATTLESHIP:
                     val = 'B';
                     break;
                  case CRUISER:
                     val = 'C';
                     break;
                  case DESTROYER1:
                     val = 'D';
                     break;
                  case DESTROYER2:
                     val = 'd';
                     break;
                  default:
                     val = ' ';
                     break;
               }
               board += " " + val + " |";
            }
         }
      }

      board += "\n     ";
      for (int j = 1; j <= sideLength; j++) {
         board += j + "   ";
      }
      board += "\n\n";
      System.out.println(board);
   }

   /**
    * Print an interstitial prompting player to pass the device.
    * @param p The *next* Player's turn
    */
   private void printInterstitial(Player p) {
      // "Clear" the Screen
      for (int i = 0; i < 100; i++) {
         System.out.println("\n");
      }
      System.out.println("\n\nIT IS  " + p + "'S TURN.  PLEASE PASS THE DEVICE TO " + p + "\n\n");
      printPlayer(p);
      System.out.println(p + ": PRESS <ENTER> TO START YOUR TURN");
      pressEnter();
   }

   /**
    * Print a large graphical player name.  Graphical ASCII text generated at http://www.network-science.de/ascii/
    * @param p The Player to print
    */
   private void printPlayer(Player p) {
      String out;
      if (p.equals(Player.PLAYER1)) {
         out = "______ _       _____   _____________   __  \n"
               + "| ___ \\ |     / _ \\ \\ / /  ___| ___ \\ /  | \n"
               + "| |_/ / |    / /_\\ \\ V /| |__ | |_/ / `| | \n"
               + "|  __/| |    |  _  |\\ / |  __||    /   | | \n"
               + "| |   | |____| | | || | | |___| |\\ \\  _| |_\n"
               + "\\_|   \\_____/\\_| |_/\\_/ \\____/\\_| \\_| \\___/\n"
               + "                                           ";
      }
      else {
         out = "______ _       _____   _____________   _____ \n"
               + "| ___ \\ |     / _ \\ \\ / /  ___| ___ \\ / __  \\\n"
               + "| |_/ / |    / /_\\ \\ V /| |__ | |_/ / `' / /'\n"
               + "|  __/| |    |  _  |\\ / |  __||    /    / /  \n"
               + "| |   | |____| | | || | | |___| |\\ \\  ./ /___\n"
               + "\\_|   \\_____/\\_| |_/\\_/ \\____/\\_| \\_| \\_____/\n"
               + "                                             ";
      }
      System.out.println(out);
   }

   /**
    * Print the title screen.
    * ASCII battleship art found at http://www.chris.com/ascii/index.php?art=transportation/nautical
    * Graphical ASCII text generated at http://www.network-science.de/ascii/
    */
   private void printTitle() {
      String s;
      s =  "\n"
            + "\n"
            + "                                     # #  ( )\n"
            + "                                  ___#_#___|__\n"
            + "                              _  |____________|  _\n"
            + "                       _=====| | |            | | |==== _\n"
            + "                 =====| |.---------------------------. | |====\n"
            + "   <--------------------'   .  .  .  .  .  .  .  .   '--------------/\n"
            + "     \\                                                             /\n"
            + "      \\_______________________________________________WWS_________/\n"
            + "  wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww\n"
            + "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww\n\n";
      s +=    "    ______  ___ _____ _____ _      _____ _____ _   _ ___________ \n"
            + "    | ___ \\/ _ \\_   _|_   _| |    |  ___/  ___| | | |_   _| ___ \\\n"
            + "    | |_/ / /_\\ \\| |   | | | |    | |__ \\ `--.| |_| | | | | |_/ /\n"
            + "    | ___ \\  _  || |   | | | |    |  __| `--. \\  _  | | | |  __/ \n"
            + "    | |_/ / | | || |   | | | |____| |___/\\__/ / | | |_| |_| |    \n"
            + "    \\____/\\_| |_/\\_/   \\_/ \\_____/\\____/\\____/\\_| |_/\\___/\\_|    ";
      s += "\n     --The Commanders\n\n";

      System.out.println(s);
      pressEnter();
   }

   /**
    * Print the start Game Message.
    * Graphical ASCII text generated at http://www.network-science.de/ascii/
    */
   private void printStartGame() {
      String s;
      s = "______  ___ _____ _____ _      _____ _ _ \n"
            + "| ___ \\/ _ \\_   _|_   _| |    |  ___| | |\n"
            + "| |_/ / /_\\ \\| |   | | | |    | |__ | | |\n"
            + "| ___ \\  _  || |   | | | |    |  __|| | |\n"
            + "| |_/ / | | || |   | | | |____| |___|_|_|\n"
            + "\\____/\\_| |_/\\_/   \\_/ \\_____/\\____/(_|_)\n"
            + "                                         ";
      System.out.println(s);
      pressEnter();
   }

   //press enter to continue helper method
   private void pressEnter() {
      System.out.println("Press Enter to Begin!");
      try {
         System.in.read();    //Implements "PRESS ENTER"
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   //sleep helper method
   private void sleep(int s) {
      try {
         Thread.sleep(s);
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
   }
}
