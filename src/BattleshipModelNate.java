import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by nate on 11/15/15.
 */
public class BattleshipModelNate implements BattleshipModelInterface {
    private final ArrayList<Ship> playerOneShips;
    private final ArrayList<Ship> playerTwoShips;

    public BattleshipModelNate() {
        playerOneShips = new ArrayList<Ship>();
        playerTwoShips = new ArrayList<Ship>();
    }

    @Override
    public Boolean placeShip(Player player, ShipType shipType, Location start, Location end) {
        ShipLocation shipStart = new ShipLocation(start);
        ShipLocation shipEnd = new ShipLocation(end);

        ArrayList<ShipLocation> locations = generateShipLocationsFromRange(shipStart, shipEnd);
        if (!areWithinBoardRange(locations) ||
                !isCorrectShipLength(locations, shipType))
            return false;

        Ship ship = new Ship(shipType, locations);
        ArrayList<Ship> ships = getPlayerShips(player);
        ships.add(ship);


        return true;
    }

    private boolean isCorrectShipLength(ArrayList<ShipLocation> locations, ShipType shipType) {
        int expectedLength = getExpectedShipLength(shipType);
        return locations.size() == expectedLength;
    }

    private int getExpectedShipLength(ShipType shipType) {
        switch (shipType) {
            case AIRCRACT_CARRIER:
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
            if (location.Column > 9 || location.Row > 9)
                return false;
        return true;
    }

    private ArrayList<ShipLocation> generateShipLocationsFromRange(ShipLocation start, ShipLocation end) {
        ArrayList<ShipLocation> locations = new ArrayList<>();
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
        return 0;
    }

    @Override
    public Boolean startGame() {
        return null;
    }

    @Override
    public Status markShot(Location loc) throws IllegalStateException {
        return null;
    }

    @Override
    public Player whoseTurn() {
        return null;
    }

    @Override
    public Square getSquare(Board board, Location loc) {
        ArrayList<Ship> ships = getBoardShips(board);
        ShipLocation location = new ShipLocation(loc);

        for(Ship ship : ships) {
            if (ship.ContainsLocation(location)) {
                return getSquareFromShipType(ship.type);
            }

        }
        return Square.NOTHING;
    }

    private Square getSquareFromShipType(ShipType type) {
        switch (type) {
            case AIRCRACT_CARRIER:
                return Square.AIRCRAFT_CARRIER;
            case BATTLESHIP:
                return Square.BATTLESHIP;
            case CRUISER:
                return Square.CRUISER;
            default:
                return Square.DESTROYER;
        }
    }

    private ArrayList<Ship> getBoardShips(Board board) {
        switch (board) {
            case PLAYER1_DEFENSIVE:
                return playerOneShips;
            default:
                return playerTwoShips;
        }
    }

    @Override
    public Boolean isGameOver() {
        return null;
    }

    @Override
    public Player getWinner() throws IllegalStateException {
        return null;
    }

    @Override
    public void resetBoard() {

    }

    private class Ship {
        ArrayList<ShipLocation> locations;
        ShipType type;

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
}
