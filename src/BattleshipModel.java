/**
 * Created by jeremy on 11/14/15.
 */

public class BattleshipModel implements BattleshipModelInterface {
   @Override public Boolean placeShip(Player player, ShipType ship, Location start, Location end) {
      return null;
   }

   @Override public int numberOfSpacesPerShip(ShipType ship) {
      return 0;
   }

   @Override public Boolean startGame() {
      return null;
   }

   @Override public Status markShot(Location loc) throws IllegalStateException {
      return null;
   }

   @Override public Player whoseTurn() {
      return null;
   }

   @Override public Square getSquare(Board board, Location loc) {
      return null;
   }

   @Override public Boolean isGameOver() {
      return null;
   }

   @Override public Player getWinner() throws IllegalStateException {
      return null;
   }

   @Override public void resetBoard() {

   }

//   public enum Player {
//      PLAYER1,
//      PLAYER2;
//
//      private String name = "";
//      Player(String name) {
//         this.name = name;
//      }
//
//   }

}
