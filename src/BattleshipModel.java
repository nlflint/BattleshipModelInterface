import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.stream.Collectors;

/**
 * Created by nate on 11/15/15.
 */
public class BattleshipModel implements BattleshipModelInterface {
   private final ArrayList<Ship> playerOneShips;
   private final ArrayList<Ship> playerTwoShips;
   private ArrayList<ShipLocation> playerOneShots;
   private ArrayList<ShipLocation> playerTwoShots;
   private Map<ShipType,Status> shipTypetoStatus;
   private boolean isPlayer1Turn;
   private GameMode mode;


   public BattleshipModel() {
      mode = GameMode.SETUP;
      playerOneShips = new ArrayList<>();
      playerTwoShips = new ArrayList<>();
      playerOneShots = new ArrayList<>();
      playerTwoShots = new ArrayList<>();
      initializeShipTypeToStatusMap();
   }

   private void initializeShipTypeToStatusMap() {
      shipTypetoStatus = new HashMap<>();
      shipTypetoStatus.put(ShipType.AIRCRAFT_CARRIER,Status.SUNK_AIRCRAFT_CARRIER);
      shipTypetoStatus.put(ShipType.DESTROYER1,Status.SUNK_DESTROYER);
      shipTypetoStatus.put(ShipType.DESTROYER2,Status.SUNK_DESTROYER);
      shipTypetoStatus.put(ShipType.BATTLESHIP,Status.SUNK_BATTLESHIP);
      shipTypetoStatus.put(ShipType.CRUISER,Status.SUNK_CRUISER);
   }

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
            isDiagonallyCrossingAnother(otherships, newShip))
         return false;

      ships.clear();
      ships.addAll(otherships);
      ships.add(newShip);

      return true;
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

      return (run == 0) || (rise == 0) || (rise / run == 1);
   }

   private boolean isOverlappingAnotherShip(ArrayList<ShipLocation> locations, ArrayList<Ship> ships) {
      for (Ship ship : ships) {
         if (ship.ContainsAnyLocations(locations))
            return true;
      }
      return false;
   }

   private boolean isCorrectShipLength(ArrayList<ShipLocation> locations, ShipType shipType) {
      int expectedLength = getExpectedShipLength(shipType);
      return locations.size() == expectedLength;
   }

   private int getExpectedShipLength(ShipType shipType) {
      switch (shipType) {
         case AIRCRAFT_CARRIER:
            return 5;
         case BATTLESHIP:
            return 4;
         case CRUISER:
            return 3;
         default:
            return 2;
      }
   }

   private boolean areWithinBoardRange(ArrayList<ShipLocation> locations) {
      for (ShipLocation location : locations)
         if (location.Column < 0 ||
               location.Column > 9 ||
               location.Row < 0 ||
               location.Row > 9)
            return false;
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

   @Override
   public int numberOfSpacesPerShip(ShipType ship) {
      return getExpectedShipLength(ship);
   }

   @Override
   public Boolean startGame() {

      if(playerOneShips.size() == 5 && playerTwoShips.size() == 5){
         isPlayer1Turn = true;
         mode = GameMode.PLAY;
         return true;
      }
      return false;
   }

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
      if (ship.isSunk()) {
         if(allShipsSunk(playerShips)){
            mode = GameMode.GAMEOVER;
            return isPlayer1Turn ? Status.PLAYER1_WINS : Status.PLAYER2_WINS;
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

   @Override
   public Player whoseTurn() {

      return isPlayer1Turn ? Player.PLAYER1: Player.PLAYER2;
   }

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
         case DESTROYER1:
            return Square.DESTROYER1;
         default:
            return Square.DESTROYER2;
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

   @Override
   public boolean isGameOver() {
      return mode == GameMode.GAMEOVER;
   }

   @Override
   public Player getWinner() throws IllegalStateException {
      return whoseTurn();
   }

   @Override
   public void resetBoard() {

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
            default:
         }
         return hitCount>=2;
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

