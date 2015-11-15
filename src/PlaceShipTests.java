import org.junit.Before;
import org.junit.Test;

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
        model.placeShip(Player.PLAYER1, ShipType.DESTROYER, getLoc(1, 'a'), getLoc(1, 'b'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(1, 'b'), Square.DESTROYER);
    }

    @Test
    public void placeShip_whenPlacingCrusierVertically_ThenGetSquareReturnsShipAtThreeLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.CRUISER, getLoc(1, 'a'), getLoc(3, 'a'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, getLoc(1, 'a'), getLoc(3, 'a'), Square.CRUISER);
    }

    @Test
    public void placeShip_whenPlacingBattleshipDiagonally_ThenGetSquareReturnsShipAtFourLocations() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(1, 'a'), getLoc(4, 'd'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(4, 'd'), Square.BATTLESHIP);
    }

    @Test
    public void placeShip_whenPlacingAircraftCarrierRightToLeft_ThenGetSquareReturnsShipAtFiveLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.CRUISER, getLoc(5, 'a'), getLoc(1, 'a'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, getLoc(5, 'a'), getLoc(1, 'a'), Square.AIRCRAFT_CARRIER);
    }

    @Test
    public void placeShip_whenPlacingToValidLocation_ThenPlaceShipReturnsTrue() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER, getLoc(1, 'a'), getLoc(1, 'b'));

        // Assert
        assertTrue(result);
    }

    @Test
    public void placeShip_whenPlacingShipOffEdgeOfBoard_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER, getLoc(10, 'a'), getLoc(11, 'a'));

        // Assert
        assertFalse(result);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, getLoc(10, 'a')), Square.NOTHING);
    }

    @Test
    public void placeShip_whenLocationsAreShorterThanShip_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(1, 'a'), getLoc(2, 'a'));

        // Assert
        assertFalse(result);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a')), Square.NOTHING);
    }

    @Test
    public void placeShip_whenLocationsAreLongerThanShip_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(1, 'a'), getLoc(7, 'a'));

        // Assert
        assertFalse(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(7, 'a'), Square.NOTHING);
    }

    @Test
    public void placeShip_whenOnPlayer1Board_ThenAllBoardExceptPlayer1DefensiveShowNothing() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(1, 'a'), getLoc(3, 'a'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, getLoc(1, 'a'), getLoc(3, 'a'), Square.NOTHING);
        assertSquareEqualsLocationRange(Board.PLAYER2_OFFENSIVE, getLoc(1, 'a'), getLoc(3, 'a'), Square.NOTHING);
        assertSquareEqualsLocationRange(Board.PLAYER1_OFFENSIVE, getLoc(1, 'a'), getLoc(3, 'a'), Square.NOTHING);
    }

    @Test
    public void placeShip_whenPlacingShipOverAnotherShip_ThenPlacementNotAllowedAndLastShipNotPlaced() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(1, 'a'), getLoc(3, 'a'));
        boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER, getLoc(2, 'a'), getLoc(3, 'a'));

        // Assert
        assertFalse(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_OFFENSIVE, getLoc(1, 'a'), getLoc(3, 'a'), Square.CRUISER);
    }

    @Test
    public void placeShip_whenPlacingTwoBattleships_ThenPlacementNotAllowedAndLastShipNotPlaced() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(1, 'a'), getLoc(3, 'a'));
        boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER, getLoc(2, 'a'), getLoc(3, 'a'));

        // Assert
        assertFalse(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_OFFENSIVE, getLoc(1, 'a'), getLoc(3, 'a'), Square.CRUISER);
    }

    @Test
    public void range_Given1To5_Returns12345() {
        // Act & Assert
        assertArrayEquals(getRange(1, 5, 5), new int[] {1,2,3,4,5});
    }

    @Test
    public void range_Given5To1_Returns54321() {
        // Act & Assert
        assertArrayEquals(new int[] {5, 4, 3, 2, 1}, getRange(5, 1, 5));
    }

    @Test
    public void range_Given3To3_Returns3() {
        // Act & Assert
        assertArrayEquals(getRange(3, 3, 3), new int[] {3, 3, 3});
    }

    @Test
    public void rowRange_GivenAToE_ReturnsABCDE() {
        // Act & Assert
        assertArrayEquals(getRange('A', 'E', 5), new int[] {'A','B','C','D','E'});
    }

    @Test
    public void rowRange_GivenDToB_ReturnsDCB() {
        // Act & Assert
        assertArrayEquals(getRange('D', 'B', 3), new int[] {'D','C','B'});
    }

    @Test
    public void rowRange_GivenXToX_ReturnsX() {
        // Act & Assert
        assertArrayEquals(getRange('X', 'X', 5), new int[] {'X','X','X','X','X'});
    }

    private void assertSquareEqualsLocationRange(Board board, Location start, Location end, Square square) {
        int longestRange = Math.max(getRangeLength(start.col, end.col), getRangeLength(start.row, end.row));
        int[] columnRange = getRange(start.col, end.col, longestRange);
        int[] rowRange = getRange(start.row, end.row, longestRange);

        for (int i = 0; i < columnRange.length; i++)
            assertEquals(model.getSquare(board, getLoc(columnRange[i], (char) rowRange[i])), square);
    }

    private int getRangeLength(int start, int end) {
        return Math.abs(start - end);
    }

    private int[] getRange(int start, int end, int length) {
        int direction = end >= start ? 1 : -1;
        double delta = (end - start + direction) / ((double) length);

        int[] range = new int[length];
        for (int i = 0; i < length; i++) {
            double val = (start + (i * delta));
            range[i] = (int) (val);
        }

        return range;
    }

    private Location getLoc(int col, char row) {
        Location loc = new Location();
        loc.col = col;
        loc.row = row;
        return loc;
    }
}
