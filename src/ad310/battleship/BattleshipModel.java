package ad310.battleship;

import java.util.*;


/**
 * An implementation of the Battleship model.
 * @author Duri Balat
 * @author Brad Baumel
 * @author Jeremy Duke
 * @author Nathan Flint
 */

public class BattleshipModel implements BattleshipModelInterface {
   private final ArrayList<Ship> playerOneShips;
   private final ArrayList<Ship> playerTwoShips;
   private ArrayList<ShipLocation> playerOneShots;
   private ArrayList<ShipLocation> playerTwoShots;
   private Map<ShipType,Status> shipTypetoStatus;
   private boolean isPlayer1Turn;
   private GameMode mode;
   private int boardDimension;
   private boolean freeTurnAfterHit;
   private boolean diagonalIsAllowed;
   private Map<ShipType, Integer> shipLengths;
   private HashSet<ShipType> legalShips;


   /***
    * Initialized a new instance of Battleship ready for setup.
    */
   public BattleshipModel(Config config) {
      mode = GameMode.SETUP;
      playerOneShips = new ArrayList<
              >();
      playerTwoShips = new ArrayList<>();
      playerOneShots = new ArrayList<>();
      playerTwoShots = new ArrayList<>();
      initializeShipTypeToStatusMap();
      initializeConfig(config);
      initializeShipTypeToLength();
   }

   private void initializeShipTypeToLength() {
      shipLengths = new HashMap<>();
      shipLengths.put(ShipType.AIRCRAFT_CARRIER, 5);
      shipLengths.put(ShipType.BATTLESHIP, 4);
      shipLengths.put(ShipType.CRUISER, 3);
      shipLengths.put(ShipType.SUBMARINE, 3);
      shipLengths.put(ShipType.DESTROYER1, 2);
      shipLengths.put(ShipType.DESTROYER2, 2);
      shipLengths.put(ShipType.MINISUB1, 1);
      shipLengths.put(ShipType.MINISUB2, 1);
   }

   private void initializeConfig(Config config) {
      boardDimension = config.BoardDimension;
      freeTurnAfterHit = config.FreeTurnAfterHit;
      diagonalIsAllowed = config.DiagonalPlacementAllowed;
      legalShips = config.Ships;

   }

   // Sets up a map to convert from ShipType enum to Status enum.
   private void initializeShipTypeToStatusMap() {
      shipTypetoStatus = new HashMap<>();
      shipTypetoStatus.put(ShipType.AIRCRAFT_CARRIER,Status.SUNK_AIRCRAFT_CARRIER);
      shipTypetoStatus.put(ShipType.DESTROYER1,Status.SUNK_DESTROYER);
      shipTypetoStatus.put(ShipType.DESTROYER2,Status.SUNK_DESTROYER);
      shipTypetoStatus.put(ShipType.BATTLESHIP,Status.SUNK_BATTLESHIP);
      shipTypetoStatus.put(ShipType.CRUISER,Status.SUNK_CRUISER);
      shipTypetoStatus.put(ShipType.MINISUB1,Status.SUNK_MINISUB);
      shipTypetoStatus.put(ShipType.MINISUB2,Status.SUNK_MINISUB);
      shipTypetoStatus.put(ShipType.SUBMARINE,Status.SUNK_SUBMARINE);
   }

   /***
    * Places the given ship at the given location, for the given player. Returns true if
    * the ship was successfully places, and false if the ship location is invalid.
    *
    * Placing a ship twice will move the ship to the second location.
    *
    * Ships cannot be placed after starting the game.
    *
    * The following requirements must be true to place a ship:
    *    The ship must be located within the board bounds
    *    The ship must be the correct length.
    *    The ship must not overlap previously placed ships
    *    This ship must be placed at an angle that is amultiple of 45 degrees
    *
    * @param player The player who is placing the ship
    * @param shipType The type of ship being placed
    * @param start  The Location on the board to place the first square of the ship
    * @param end The Location on the board to place the last square of the ship
     * @return True if the placement was accepted. False if there was a problem, and the ship was not placed.
     */
   @Override
   public Boolean placeShip(Player player, ShipType shipType, Location start, Location end) {
      if(mode != GameMode.SETUP){
         return false;
      }
      ShipLocation shipStart = new ShipLocation(start);
      ShipLocation shipEnd = new ShipLocation(end);
      ArrayList<ShipLocation> locations = generateShipLocationsFromRange(shipStart, shipEnd);

      ArrayList<Ship> ships = getPlayerShips(player);
      ArrayList<Ship> otherships = filterOutShipsMatchingType(shipType, ships);

      Ship newShip = new Ship(shipType, locations);

      if (!areWithinBoardRange(locations) ||
            !isCorrectShipLength(locations, shipType) ||
            isOverlappingAnotherShip(locations, otherships) ||
            !shipAngleIs45Degrees(shipStart, shipEnd) ||
            isDiagonallyCrossingAnother(otherships, newShip) ||
              !isLegalShip(shipType))
         return false;

      ships.clear();
      ships.addAll(otherships);
      ships.add(newShip);

      return true;
   }

   private boolean isLegalShip(ShipType shipType) {
      return legalShips.contains(shipType);
   }

   private boolean isDiagonallyCrossingAnother(ArrayList<Ship> otherShips, Ship newShip) {
      LineSegment newLine = new LineSegment(newShip);

      List<LineSegment> otherLines = new ArrayList<>();

      for(int i = 0; i<=otherShips.size()-1; i++){
         LineSegment line = new LineSegment(otherShips.get(i));
         otherLines.add(line);
      }

      for (LineSegment line : otherLines) {
         if (newLine.isIntersecting(line))
            return true;
      }
      return false;
   }



   private ArrayList<Ship> filterOutShipsMatchingType(ShipType shipType, ArrayList<Ship> ships) {
      ArrayList<Ship> filteredShips = new ArrayList<>();

      for(int i = 0; i <= ships.size()-1 ; i++){
         Ship ship = ships.get(i);
         if(ship.type != shipType){
            filteredShips.add(ship);
         }
      }
      return filteredShips;
   }

   private boolean shipAngleIs45Degrees(ShipLocation start, ShipLocation end) {
      double rise = Math.abs(start.Row - end.Row);
      double run = Math.abs(start.Column - end.Column);
      return (run == 0) || (rise == 0) || (rise / run == 1 && diagonalIsAllowed);
   }

   private boolean isOverlappingAnotherShip(ArrayList<ShipLocation> locations, ArrayList<Ship> ships) {
      for (Ship ship : ships) {
         if (ship.ContainsAnyLocations(locations))
            return true;
      }
      return false;
   }

   private boolean isCorrectShipLength(ArrayList<ShipLocation> locations, ShipType shipType) {
      int expectedLength = shipLengths.get(shipType);
      return locations.size() == expectedLength;
   }


   private boolean areWithinBoardRange(ArrayList<ShipLocation> locations) {
      for (ShipLocation location : locations) {
         if (location.Column < 0 ||
               location.Column >= boardDimension ||
               location.Row < 0 ||
               location.Row >= boardDimension)
            return false;
      }
      return true;
   }

   private ArrayList<ShipLocation> generateShipLocationsFromRange(ShipLocation start, ShipLocation end) {
      ArrayList<ShipLocation> locations = new ArrayList<ShipLocation>();
      int longestRange = Math.max(
            getRangeLength(start.Column, end.Column),
            getRangeLength(start.Row, end.Row));

      int[] verticalRange = getRange(start.Row, end.Row, longestRange);
      int[] horizontalRange = getRange(start.Column, end.Column, longestRange);

      for (int i = 0; i < longestRange; i++) {
         int row = verticalRange[i];
         int column = horizontalRange[i];
         ShipLocation shipLocation = new ShipLocation(row, column);
         locations.add(shipLocation);
      }
      return locations;
   }

   private int getRangeLength(int start, int end) {
      int direction = (start >= end) ? 1 : -1;
      return Math.abs(start - end + direction);
   }

   private int[] getRange(int start, int end, int length) {
      int direction = (end >= start) ? 1 : -1;
      double delta = (end - start + direction) / ((double) length);

      int[] range = new int[length];
      for (int i = 0; i < length; i++) {
         double val = (start + (i * delta));
         range[i] = (int) (val);
      }

      return range;
   }

   private ArrayList<Ship> getPlayerShips(Player player) {
      return player.equals(Player.PLAYER1) ? playerOneShips : playerTwoShips;
   }

   /**
    * Returns the number of squares the given shiptype occupies.
    *
    * @param ship the ShipType of interest
    * @return number of spaces the ship type will occupy on the board
     */
   @Override
   public int numberOfSpacesPerShip(ShipType ship) {
      return shipLengths.get(ship);
   }

   /**
    * Starts the game. Returns true if the game was succesfully started. Otherwise returns false.
    * The game will not start unless all ships have been placed in valid locations.
    * @return true if the game started. False if the game did not start.
     */
   @Override
   public Boolean startGame() {

      if(playerOneShips.size() == legalShips.size() && playerTwoShips.size() == legalShips.size()){
         isPlayer1Turn = true;
         mode = GameMode.PLAY;
         return true;
      }
      return false;
   }

   /**
    * Used during play to make shots on the board. The playing making shots can be determined by calling
    * whoseTurn(). Returns the result of the shot.
    *
    * @param loc The designator for the shot
    * @return the result of the shot
    * @throws IllegalStateException
     */
   @Override
   public Status markShot(Location loc) throws IllegalStateException{
      if(mode != GameMode.PLAY){
         return Status.NOT_ALLOWED;
      }

      ShipLocation shotLocation = new ShipLocation(loc);

      if(!shotLocationisValid(shotLocation)){
         return Status.DO_OVER;
      }
      ArrayList<ShipLocation> playerShots = isPlayer1Turn ? playerOneShots : playerTwoShots;
      ArrayList<Ship> playerShips = isPlayer1Turn ? playerTwoShips : playerOneShips;

      playerShots.add(shotLocation);
      Ship ship = getShipAtLocation(playerShips, shotLocation);
      if (ship == null) {
         togglePlayerTurn();
         return Status.MISS;
      }

      ship.hit();
      if(!freeTurnAfterHit)
         togglePlayerTurn();

      if (ship.isSunk()) {
         if(allShipsSunk(playerShips)){
            mode = GameMode.GAMEOVER;
            return isPlayer1Turn && freeTurnAfterHit ? Status.PLAYER1_WINS : Status.PLAYER2_WINS;
         }
         return shipTypetoStatus.get(ship.type);
      }
      return Status.HIT;
  }

   private boolean allShipsSunk(ArrayList<Ship> playerShips) {
      for(int i = 0; i< playerShips.size(); i++){
         if(!playerShips.get(i).isSunk()) {
            return false;
         }
      }
      return true;
   }

   private Ship getShipAtLocation(ArrayList<Ship> Ships, ShipLocation shotLocation) {
      for(int i =0; i<Ships.size(); i++){
         if(Ships.get(i).ContainsLocation(shotLocation)){
            return Ships.get(i);
         }
      }
      return null;
   }

   private void togglePlayerTurn() {
      isPlayer1Turn = !isPlayer1Turn;
   }

   private boolean shotLocationisValid(ShipLocation shotLocation) {
      ArrayList<ShipLocation> playerShots = isPlayer1Turn ? playerOneShots : playerTwoShots;
      if( playerShots.contains(shotLocation)){
         return false;
      }
      return shotLocation.Row>=0 &&
              shotLocation.Row<=9 &&
              shotLocation.Column>=0 &&
              shotLocation.Column<=9;
   }

   private boolean isShipHit(ArrayList<Ship> Ships, ShipLocation shotLocation) {
      for(int i = 0; i< Ships.size(); i++){
         ArrayList<ShipLocation> shipLocations = Ships.get(i).locations;
         for (int j = 0; j< shipLocations.size(); j++){
            if(shipLocations.get(j).equals(shotLocation)){
               return true;
            }
         }
      }
      return false;
   }

   /**
    * Returns whose turn it is to make a shot.
    * @return a player whose turn it is to make a shot.
     */
   @Override
   public Player whoseTurn() {

      return isPlayer1Turn ? Player.PLAYER1: Player.PLAYER2;
   }

   /**
    * Shows what is located at the given location and on the given board. For offensive boards, ships are not shown
    * during play. Only hits and misses. Ships, hits, and misses are all shown on a defensive board.
    * @param board Board to evaluate
    * @param loc   Location on Board of interest
    * @return What is located at the given location
     */
   @Override
   public Square getSquare(Board board, Location loc) {
      ArrayList<Ship> ships = getBoardShips(board);
      ShipLocation location = new ShipLocation(loc);
      ArrayList<ShipLocation> shots = getShotsFromBoard(board);

      boolean locationIsShot = shotsContains(location, shots);
      boolean showShip = isDefensiveBoard(board) || isGameOver();

      for(Ship ship : ships) {
         if (ship.ContainsLocation(location)) {
            if(!locationIsShot) {
               return showShip ? getSquareFromShipType(ship.type) : Square.NOTHING ;
            } else {
               if (!isDefensiveBoard(board) && ship.isSunk()) {
                  return getSquareFromShipType(ship.type);
               }
               return Square.HIT;
            }
         }
      }
      if (locationIsShot) return Square.MISS;
      return Square.NOTHING;
   }

   private boolean isDefensiveBoard(Board board) {
      switch (board){
         case PLAYER1_DEFENSIVE:
            return true;
         case PLAYER2_DEFENSIVE:
            return true;
         default:
            return false;
      }
   }

   private boolean shotsContains(ShipLocation location, ArrayList<ShipLocation> shots) {
      for(ShipLocation shot : shots){
         if(shot.equals(location)){
            return true;
         }
      }
      return false;
   }

   private ArrayList<ShipLocation> getShotsFromBoard(Board board) {
      switch (board){
         case PLAYER1_OFFENSIVE:
            return playerOneShots;
         case PLAYER2_DEFENSIVE:
            return playerOneShots;
         default:
            return playerTwoShots;
      }
   }

   private Square getSquareFromShipType(ShipType type) {
      switch (type) {
         case AIRCRAFT_CARRIER:
            return Square.AIRCRAFT_CARRIER;
         case BATTLESHIP:
            return Square.BATTLESHIP;
         case CRUISER:
            return Square.CRUISER;
         case SUBMARINE:
            return Square.SUBMARINE;
         case DESTROYER1:
            return Square.DESTROYER1;
         case DESTROYER2:
            return Square.DESTROYER2;
         case MINISUB1:
            return Square.MINISUB1;
         default:
            return Square.MINISUB2;
      }
   }

   private ArrayList<Ship> getBoardShips(Board board) {
      switch (board) {
         case PLAYER1_DEFENSIVE:
            return playerOneShips;
         case PLAYER2_OFFENSIVE:
            return playerOneShips;
         default:
            return playerTwoShips;
      }
   }

   /**
    * Identifies if the game is over. That is, if a player has won.
    *
    * @return true if the game is over. False otherwise.
     */
   @Override
   public boolean isGameOver() {
      return mode == GameMode.GAMEOVER;
   }

    /**
     * Identifies who won the game.
     * @return
     * @throws IllegalStateException
     */
   @Override
   public Player getWinner() throws IllegalStateException {
      return whoseTurn();
   }

   private class Ship {
      ArrayList<ShipLocation> locations;
      ShipType type;
      int hitCount;

      public Ship(ShipType shipType, ArrayList<ShipLocation> locations) {
         type = shipType;
         this.locations = locations;
      }

      public boolean ContainsLocation(ShipLocation location) {
         for (ShipLocation loc : locations) {
            if (loc.equals(location))
               return true;
         }
         return false;
      }

      public boolean ContainsAnyLocations(ArrayList<ShipLocation> locations) {
         for (ShipLocation location : locations)
            if (ContainsLocation(location))
               return true;
         return false;
      }

      public boolean isSunk(){
         switch(type){
            case AIRCRAFT_CARRIER:
               return hitCount>=5;
            case BATTLESHIP:
               return hitCount>=4;
            case CRUISER:
               return hitCount>=3;
            case SUBMARINE:
               return hitCount>=3;
            case DESTROYER1:
               return hitCount>=2;
            case DESTROYER2:
               return hitCount>=2;
            case MINISUB1:
               return hitCount>=1;
            case MINISUB2:
               return hitCount>=1;
         }
         return hitCount>=1;
      }
      public void hit(){
         hitCount++;
      }
   }

   private class ShipLocation {
      public final int Row;
      public final int Column;

      public ShipLocation(Location location) {
         Row = (int) (location.row - 'a');
         Column = location.col - 1;
      }

      public ShipLocation(int row, int column) {
         Row = row;
         Column = column;
      }

      @Override
      public boolean equals(Object object) {
         ShipLocation location = (ShipLocation) object;
         return Row == location.Row && Column == location.Column;
      }
   }

   private class LineSegment {
      public final Point Start;
      public final Point End;

      public LineSegment(Ship ship) {
         Start = new Point(ship.locations.get(0));
         int lastIndex = ship.locations.size() - 1;
         End =  new Point(ship.locations.get(lastIndex));
      }

      public boolean isIntersecting(LineSegment other) {
         if (this.isVertical() || other.isVertical()) {
            return false;
         }
         double slope1 = this.getSlope();
         double intercept1 = this.getIntercept();

         double slope2 = other.getSlope();
         double intercept2 = other.getIntercept();

         double intersectX = (intercept1 - intercept2) / (slope2 - slope1);
         double intersectY = (slope1 * intersectX) + intercept1;

         boolean xWithinFirst = this.isWithinXBounds(intersectX);
         boolean yWithinFirst = this.isWithinYBounds(intersectY);
         boolean xWithinSecond = other.isWithinXBounds(intersectX);
         boolean yWithinSecond = other.isWithinYBounds(intersectY);

         return xWithinFirst && yWithinFirst && xWithinSecond && yWithinSecond;
      }

      private boolean isWithinYBounds(double intersect) {
         double lower = Math.min(Start.Y, End.Y);
         double upper = Math.max(Start.Y, End.Y);

         return (intersect >= lower) && (intersect <= upper);
      }

      private boolean isWithinXBounds(double intersect) {
         double lower = Math.min(Start.X, End.X);
         double upper = Math.max(Start.X, End.X);

         return (intersect >= lower) && (intersect <= upper);
      }

      public double getSlope() {
         return (Start.Y - End.Y) / (Start.X - End.X);
      }

      public boolean isVertical() {
         return End.X == Start.X;
      }

      public double getIntercept() {
         return Start.Y - (getSlope() * Start.X);
      }
   }

   private class Point {
      public final int X;
      public final int Y;

      public Point(ShipLocation shipLocation) {
         X = shipLocation.Column;
         Y = shipLocation.Row;
      }
   }
   enum GameMode{
      SETUP,
      PLAY,
      GAMEOVER
   }

}

