import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class PlaceShipTests {
    BattleshipModelInterface model;

    @Before
    public void BeforeEachTest() {
        //model = new BattleshipModel();
    }

    @Test
    public void placeShip_whenPlacingDestoryerHorizontally_ThenGetSquareReturnsShipAtTwoLocations() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.DESTROYER, location(1, 'a'), location(1,'b'));

        // Assert
        locaitonRangeMatchesSquare(Board.PLAYER1_DEFENSIVE, location(1, 'a'), location(1,'b'), Square.DESTROYER);
    }

    @Test
    public void placeShip_whenPlacingCrusierVertically_ThenGetSquareReturnsShipAtThreeLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.CRUISER, location(1, 'a'), location(3,'a'));

        // Assert
        locaitonRangeMatchesSquare(Board.PLAYER2_DEFENSIVE, location(1, 'a'), location(3,'a'), Square.CRUISER);
    }

    @Test
    public void placeShip_whenPlacingBattleshipDiagonally_ThenGetSquareReturnsShipAtFourLocations() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.CRUISER, location(1, 'a'), location(4,'d'));

        // Assert
        locaitonRangeMatchesSquare(Board.PLAYER1_DEFENSIVE, location(1, 'a'), location(4,'d'), Square.BATTLESHIP);
    }

    @Test
    public void placeShip_whenPlacingAircraftCarrierRightToLeft_ThenGetSquareReturnsShipAtFiveLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.CRUISER, location(5, 'a'), location(1,'a'));

        // Assert
        locaitonRangeMatchesSquare(Board.PLAYER2_DEFENSIVE, location(5, 'a'), location(1,'a'), Square.AIRCRAFT_CARRIER);
    }

    @Test
    public void placeShip_whenPlacingToValidLocation_ThenPlaceShipReturnsTrue() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER, location(1, 'a'), location(1,'b'));

        // Assert
        assertTrue(result);
    }

    @Test
    public void placeShip_whenPlacingShipOffEdgeOfBoard_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER, location(10, 'a'), location(11,'a'));

        // Assert
        assertFalse(result);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, location(10, 'a')), Square.NOTHING);
    }

    @Test
    public void placeShip_whenLocationsAreShorterThanShip_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, location(1, 'a'), location(2,'a'));

        // Assert
        assertFalse(result);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, location(1, 'a')), Square.NOTHING);
    }

    @Test
    public void placeShip_whenLocationsAreLongerThanShip_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, location(1, 'a'), location(7,'a'));

        // Assert
        assertFalse(result);
        locaitonRangeMatchesSquare(Board.PLAYER1_DEFENSIVE, location(1, 'a'), location(7, 'a'), Square.NOTHING);
    }

    @Test
    public void placeShip_whenOnPlayer1Board_ThenAllBoardExceptPlayer1DefensiveShowNothing() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, location(1, 'a'), location(3,'a'));

        // Assert
        locaitonRangeMatchesSquare(Board.PLAYER2_DEFENSIVE, location(1, 'a'), location(3, 'a'), Square.NOTHING);
        locaitonRangeMatchesSquare(Board.PLAYER2_OFFENSIVE, location(1, 'a'), location(3, 'a'), Square.NOTHING);
        locaitonRangeMatchesSquare(Board.PLAYER1_OFFENSIVE, location(1, 'a'), location(3, 'a'), Square.NOTHING);
    }

    @Test
    public void range_Given1To5_Returns12345() {
        // Act & Assert
        assertArrayEquals(getColumnRange(1,5), new int[] {1,2,3,4,5});
    }

    @Test
    public void range_Given5To1_Returns54321() {
        // Act & Assert
        assertArrayEquals(getColumnRange(5,1), new int[] {5,4,3,2,1});
    }

    @Test
    public void range_Given3To3_Returns3() {
        // Act & Assert
        assertArrayEquals(getColumnRange(3,3), new int[] {3});
    }

    @Test
    public void rowRange_GivenAToE_ReturnsABCDE() {
        // Act & Assert
        assertArrayEquals(getRowRange('A','E'), new char[] {'A','B','C','D','E'});
    }

    @Test
    public void rowRange_GivenDToB_ReturnsDCB() {
        // Act & Assert
        assertArrayEquals(getRowRange('D','B'), new char[] {'D','C','B'});
    }

    @Test
    public void rowRange_GivenXToX_ReturnsX() {
        // Act & Assert
        assertArrayEquals(getRowRange('X','X'), new char[] {'X'});
    }

    private void locaitonRangeMatchesSquare(Board board, Location start, Location end, Square square) {
        int[] columnRange = getColumnRange(start.col, end.col);
        char[] rowRange = getRowRange(start.row, end.row);

        for (int i = 0; i < columnRange.length; i++) {
            assertEquals(model.getSquare(board, location(columnRange[i], rowRange[i])), square);
        }
    }

    private char[] getRowRange(char start, char end) {
        int direction = start < end ? 1 : -1;
        int rangeLength = Math.abs(start - end) + 1;
        char[] range = new char[rangeLength];

        for (int i = 0; i < rangeLength; i++) {
            range[i] = (char) (start + (i * direction));
        }

        return range;
    }

    private int[] getColumnRange(int start, int end) {
        int direction = start < end ? 1 : -1;
        int rangeLength = Math.abs(start - end) + 1;
        int[] range = new int[rangeLength];

        for (int i = 0; i < rangeLength; i++) {
            range[i] = start + (i * direction);
        }

        return range;
    }


    private Location location(int col, char row) {
        Location loc = new Location();
        loc.col = col;
        loc.row = row;
        return loc;
    }
}
