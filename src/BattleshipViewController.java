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
   PlayerBoard p1off = new PlayerBoard(Board.PLAYER1_OFFENSIVE);
   PlayerBoard p1def = new PlayerBoard(Board.PLAYER1_DEFENSIVE);
   PlayerBoard p2off = new PlayerBoard(Board.PLAYER2_OFFENSIVE);
   PlayerBoard p2def = new PlayerBoard(Board.PLAYER2_DEFENSIVE);

//   Player p1 = new Player();
//   Player p2 = new Player("PLayer 2");


   public String displayBoard() {
      PlayerBoard off;
      PlayerBoard def;

      String player = "Player 1";

      if (player == "Player 1") {
         off = p1off;
         def = p1def;
      } else {
         off = p2off;
         def = p2def;
      }

      String out;
      //String offensive = "    =====OFFENSIVE BOARD - " + p.toString() + " =====";
      String offensive = "    =====OFFENSIVE BOARD - " + player + " =====";
      offensive += renderBoard(off);

      //String defensive = "    =====DEFENSIVE BOARD - " + p.toString() + " =====";
      String defensive = "    =====DEFENSIVE BOARD - " + player + " =====";
      defensive += renderBoard(def);

      out = offensive + defensive;
      return out;
   }

   //Refactored helper method for printing current board state
   private String renderBoard(PlayerBoard b) {
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
            for (int n = 0; n < 10; n++) {
               int row = i;
               int col = n;
               int cell = row * 10 + col;
               out += " " + b.getSquare(cell) + " |";
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



   public static void main(String[] args) {
      //GAME COMPONENTS
      //PURE TESTING HERE
      //BattleshipModel model = new BattleshipModel();
      //This works...
      //boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, model.getLoc(10, 'j'), model.getLoc(9, 'j'));
      BattleshipViewController bvc = new BattleshipViewController();
      //This works...
      //System.out.println(bvc.displayBoard());

      bvc.doSetup();


   }

   private void doSetup() {
      BattleshipModel model = new BattleshipModel();
      //boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, model.getLoc(10, 'j'), model.getLoc(9, 'j'));
      //System.out.println(result);
      BattleshipViewController bvc = new BattleshipViewController();
      //Create Player Objects
      Player player1 = model.whoseTurn();
      model.setPlayerTurn();
      Player player2 = model.whoseTurn();
      model.setPlayerTurn();
      Player currentPlayer = player1;

      promptPlayerSetup(currentPlayer, ShipType.BATTLESHIP, model);
      //

   }

   private void promptPlayerSetup(Player p, ShipType s, BattleshipModel m) {
      Scanner in = new Scanner(System.in);
      char col = 0;
      int  row = 0;
      boolean validStart = false;
      boolean validEnd = false;
      System.out.println(p + "'s Turn!  Place your " + s + "!");

      System.out.println("Choose a starting location!");
      while (!validStart) {
         while ((int)col < 65 || (int)col > 74) {
            System.out.println("Enter a valid column [A-J]: ");
            col = in.next().charAt(0);
            System.out.println(col);
         }

         while (row < 1 || row > 10) {
            System.out.println("Enter a valid row [1-10]: ");
            row = in.nextInt();
            System.out.println(row);
         }
      }
   }


   private void promptPlayer(Player p) {
      System.out.println(p + "'s Turn.  Make your move: ");
      Scanner in = new Scanner(System.in);
   }

}
