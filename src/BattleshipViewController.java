import java.io.IOException;
import java.util.Scanner;

/**
 * Implements Battleship game controls and display
 * @author Duri Balat
 * @author Brad Baumel
 * @author Jeremy Duke
 * @author Nathan Flynt
 */

public class BattleshipViewController {
   //Fields
   BattleshipModel model = new BattleshipModel();
   //TODO: do we need any fields?

   public static void main(String[] args) {
      //Stand up MVC components
      BattleshipViewController bvc = new BattleshipViewController();

      //Welcome!
      bvc.printTitle();

      //Run through Ship Setup
      bvc.doSetup();
      bvc.printStartGame();

      //When setup is complete...

      bvc.play();
   }

   //Gameplay Methods
   private void doSetup() {
      //ENUMS ARE EASY
      Player player1 = Player.PLAYER1;
      Player player2 = Player.PLAYER2;
      Board p1def = Board.PLAYER1_DEFENSIVE;
      Board p2def = Board.PLAYER2_DEFENSIVE;

      //Player1 Setup
      printInterstitial(player1);
      promptPlayerSetup(player1, ShipType.AIRCRAFT_CARRIER, p1def);
      promptPlayerSetup(player1, ShipType.BATTLESHIP, p1def);
      promptPlayerSetup(player1, ShipType.CRUISER, p1def);
      promptPlayerSetup(player1, ShipType.DESTROYER1, p1def);
      promptPlayerSetup(player1, ShipType.DESTROYER2, p1def);
      System.out.println(displayBoard(player1, p1def));
      try {
         Thread.sleep(1500);
      } catch (InterruptedException e){
         e.printStackTrace();
      }
      printInterstitial(player2);

      //Player2 Setup
      promptPlayerSetup(player2, ShipType.AIRCRAFT_CARRIER, p2def);
      promptPlayerSetup(player2, ShipType.BATTLESHIP, p2def);
      promptPlayerSetup(player2, ShipType.CRUISER, p2def);
      promptPlayerSetup(player2, ShipType.DESTROYER1, p2def);
      promptPlayerSetup(player2, ShipType.DESTROYER2, p2def);
      System.out.println(displayBoard(player2, p2def));
      try {
         Thread.sleep(1500);
      } catch (InterruptedException e){
         e.printStackTrace();
      }
   }

   private void promptPlayerSetup(Player player, ShipType ship, Board b) {
      Scanner in = new Scanner(System.in);
      int  col = 0;
      char row = 0;
      boolean validStart = false;
      boolean validEnd = false;
      Location start = null;
      Location end = null;

      while (!validStart) {
         System.out.println(displayBoard(player, b));
         System.out.println(player + "'s Turn!  Place your " + ship + "!\n\n");
         System.out.println("Choose a starting location for your " + ship + "(" + model.numberOfSpacesPerShip(ship) + ")!");
         while ((row < 'a' || row > 'j')) {
            System.out.println("Enter a valid row [A-J]: ");
            row = in.next().charAt(0);
            //Handle upper casing
            if (row >= 'A' && row <= 'J') {
               row = (char) (row + 32);
            }
         }

         while (col < 1 || col > 10) {
            System.out.println("Enter a valid column [1-10]: ");
            col = in.nextInt();
         }
         start = new Location(col, row);
         Square startSquare = model.getSquare(b, start);
         if (startSquare.equals(Square.NOTHING)) {
            validStart = !validStart;
            row = 0;
            col = 0;
         }
      }
      while (!validEnd) {
         System.out.println(displayBoard(player, b));
         System.out.println("Choose an ending location for your " + ship + "(" + model.numberOfSpacesPerShip(ship) + ")!  Starting square: " + start);
         while (row < 'a' || row > 'j') {
            System.out.println("Enter a valid row [A-J]: ");
            row = in.next().charAt(0);
            //Handle upper casing
            if (row >= 'A' && row <= 'J') {
               row = (char) (row + 32);
            }
            System.out.println(row);
         }
         while (col < 1 || col > 10) {
            System.out.println("Enter a valid column [1-10]: ");
            col = in.nextInt();
            System.out.println(col);
         }
         end = new Location(col, row);
         Square square = model.getSquare(b, end);
         if (square.equals(Square.NOTHING) && model.placeShip(player, ship, start, end)) {
            validEnd  = !validEnd;
         } else {
            System.out.println("Invalid ending location.  Choose another Ending Location.");
            row = 0;
            col = 0;
         }
      }
   }

   private void play() {
      model.startGame();
      Player currentPlayer = model.whoseTurn();
      while (!model.isGameOver()) {
         System.out.println(currentPlayer);
         printInterstitial(currentPlayer);
         promptPlayer(currentPlayer);
         if (model.isGameOver()) {
            return;              //This move might have been a winning move.  End the game immediately.
         }
      }
   }

   private void promptPlayer(Player p) {
      System.out.println(p + "'s Turn.  Make your move: ");
      Scanner in = new Scanner(System.in);
   }

   //DISPLAY METHODS
   public String displayBoard(Player p, Board b) {
      String board;
      String title;
      if (b == Board.PLAYER1_OFFENSIVE || b == Board.PLAYER2_OFFENSIVE) {
         title = "OFFENSIVE";
      } else {
         title = "DEFENSIVE";
      }
      board  = "    ======= " + title + " - " + p.toString() + " =======";
      board += renderBoard(b);

      return board;
   }

   private String renderBoard(Board b) {
      String out = "";
      //Rows
      for (int i = 0; i <= 10; i++) {
         out += "\n   -";
         //FORMATTING - Horizontal lines
         for (int j = 1; j <= 10; j++) {
            out += "---+";
         }
         if (i != 10) {
            out += "\n" + (char)(i + 65) + "  |";
            for (int n = 1; n <= 10; n++) {
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
               out += " " + val + " |";
            }
         }
      }

      out += "\n     ";
      for (int j = 1; j <= 10; j++) {
         out += j + "   ";
      }
      out += "\n\n";
      return out;
   }

   private void printInterstitial(Player p) {
      // "Clear" the Screen
      for (int i = 0; i < 100; i++) {
         System.out.println("\n");
      }
      System.out.println("\n\nIT IS  " + p + "'S TURN.  PLEASE PASS THE DEVICE TO " + p + "\n\n");
      System.out.println(printPlayer(p));
      System.out.println(p + ": PRESS <ENTER> TO START YOUR TURN");
      pressEnter();
   }

   private String printPlayer(Player p) {
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
      return out;
   }

   private void printTitle() {
      String s;
      s = "______  ___ _____ _____ _      _____ _____ _   _ ___________ \n"
            + "| ___ \\/ _ \\_   _|_   _| |    |  ___/  ___| | | |_   _| ___ \\\n"
            + "| |_/ / /_\\ \\| |   | | | |    | |__ \\ `--.| |_| | | | | |_/ /\n"
            + "| ___ \\  _  || |   | | | |    |  __| `--. \\  _  | | | |  __/ \n"
            + "| |_/ / | | || |   | | | |____| |___/\\__/ / | | |_| |_| |    \n"
            + "\\____/\\_| |_/\\_/   \\_/ \\_____/\\____/\\____/\\_| |_/\\___/\\_|    ";

      s += "\n --The Commanders\n\n";

      System.out.println(s);
      pressEnter();
   }

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

   private void pressEnter() {
      System.out.println("Press Enter to Begin!");

      try {
         System.in.read();    //Implements "PRESS ENTER"
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

}
