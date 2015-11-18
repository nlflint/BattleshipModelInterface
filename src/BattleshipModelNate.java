import javax.sound.sampled.Line;
import java.util.ArrayList;
import java.util.List;
//import java.util.stream.Collectors;

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

    private boolean isDiagonallyCrossingAnother(ArrayList<Ship> otherships, Ship newShip) {
        LineSegment newLine = getLineSegmentFromShip(newShip);

//        List<LineSegment> otherLines = otherships
//                .stream()
//                .map(this::getLineSegmentFromShip)
//                .collect(Collectors.toList());

//        for (LineSegment line : otherLines) {
//            if (areIntersecting(newLine, line))
//                return true;
//        }
        return false;
    }

    private boolean areIntersecting(LineSegment first, LineSegment second) {
        double slope1 = getSlope(first);
        double intercept1 = getIntercept(first, slope1);

        double slope2 = getSlope(second);
        double intercept2 = getIntercept(second, slope2);

        double intersectX = (intercept1 - intercept2) / (slope2 - slope1);
        double intersectY = (slope1 * intersectX) + intercept1;

        boolean xWithinFirst = isBetweenBounds(intersectX, first.Start.X, first.End.X);
        boolean yWithinFirst = isBetweenBounds(intersectY, first.Start.Y, first.End.Y);
        boolean xWithinSecond = isBetweenBounds(intersectX, second.Start.X, second.End.X);
        boolean yWithinSecond = isBetweenBounds(intersectY, second.Start.Y, second.End.Y);

        return  xWithinFirst && yWithinFirst && xWithinSecond && yWithinSecond;
    }

    private boolean isBetweenBounds(double intersect, double bound1, double bound2) {
        double lower = Math.min(bound1, bound2);
        double upper = Math.max(bound1, bound2);

        return (intersect >= lower) && (intersect <= upper);
    }

    private double getIntercept(LineSegment first, double slope1) {
        return first.Start.Y - (slope1 * first.Start.X);
    }

    private int getSlope(LineSegment first) {
        return (first.Start.Y - first.End.Y) / (first.Start.X - first.End.X);
    }

    private LineSegment getLineSegmentFromShip(Ship ship) {
        Point first = getPointFromShipLocation(ship.locations.get(0));
        int lastIndex = ship.locations.size() - 1;
        Point second =  getPointFromShipLocation(ship.locations.get(lastIndex));
        return new LineSegment(first, second);
    }

    private Point getPointFromShipLocation(ShipLocation shipLocation) {
        return new Point(shipLocation);
    }

    private ArrayList<Ship> filterOutShipsMatchingType(ShipType shipType, ArrayList<Ship> ships) {
        ArrayList<Ship> filteredShips = new ArrayList<Ship>();
        //ships.stream().filter(x -> !x.type.equals(shipType)).forEach(filteredShips::add);
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

    @Override public Location getLoc(int col, char row) {
        Location loc = new Location();
        loc.col = col;
        loc.row = row;
        return loc;
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

        public boolean ContainsAnyLocations(ArrayList<ShipLocation> locations) {
            for (ShipLocation location : locations)
                if (ContainsLocation(location))
                    return true;
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

    private class LineSegment {
        public final Point Start;
        public final Point End;

        public LineSegment(Point start, Point end) {
            Start = start;
            End = end;
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

    //@FunctionalInterface
    public interface WorkerInterface {

        public LineSegment doSomeWork(Ship ship);

    }
}
