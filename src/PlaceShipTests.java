import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlaceShipTests {
    BattleshipModelInterface model;

    @Before
    public void BeforeEachTest() {
        model = new BattleshipModelNate();
    }

    @Test
    public void placeShip_whenPlacingDestoryer1Horizontally_ThenGetSquareReturnsShipAtTwoLocations() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER1, getLoc(1, 'a'), getLoc(1, 'b'));

        // Assert
        assertTrue(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(1, 'b'), Square.DESTROYER1);
    }

    @Test
    public void placeShip_whenPlacingDestoryer2_ThenGetSquareReturnsShipAtTwoLocations() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, getLoc(10, 'j'), getLoc(9, 'j'));

        // Assert
        assertTrue(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(9, 'j'), getLoc(10, 'j'), Square.DESTROYER2);
    }

    @Test
    public void placeShip_whenPlacingCrusierVertically_ThenGetSquareReturnsShipAtThreeLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.CRUISER, getLoc(1, 'a'), getLoc(1, 'c'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, getLoc(1, 'a'), getLoc(1, 'c'), Square.CRUISER);
    }

    @Test
    public void placeShip_whenPlacingCrusierBottomToTop_ThenGetSquareReturnsShipAtThreeLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.CRUISER, getLoc(1, 'c'), getLoc(1, 'a'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, getLoc(1, 'a'), getLoc(1, 'c'), Square.CRUISER);
    }

    @Test
    public void placeShip_whenPlacingBattleshipDiagonallyNWtoSE_ThenGetSquareReturnsShipAtFourLocations() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(1, 'a'), getLoc(4, 'd'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(4, 'd'), Square.BATTLESHIP);
    }

    @Test
    public void placeShip_whenPlacingAircraftCarrierDiagonallySWtoNE_ThenGetSquareReturnsShipAtFiveLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.AIRCRACT_CARRIER, getLoc(1, 'a'), getLoc(5, 'e'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, getLoc(5, 'e'), getLoc(1, 'a'), Square.AIRCRAFT_CARRIER);
    }

    @Test
    public void placeShip_whenPlacingAircraftCarrierDiagonallySEtoNW_ThenGetSquareReturnsShipAtFiveLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.AIRCRACT_CARRIER, getLoc(1, 'e'), getLoc(5, 'a'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, getLoc(5, 'a'), getLoc(1, 'e'), Square.AIRCRAFT_CARRIER);
    }

    @Test
    public void placeShip_whenPlacingAircraftCarrierDiagonallyNEtoSW_ThenGetSquareReturnsShipAtFiveLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.AIRCRACT_CARRIER, getLoc(1, 'e'), getLoc(5, 'a'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, getLoc(5, 'a'), getLoc(1, 'e'), Square.AIRCRAFT_CARRIER);
    }

    @Test
    public void placeShip_whenPlacingShipOffEdgeOfBoard_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result1 = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, getLoc(10, 'a'), getLoc(11, 'a'));
        boolean result2 = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, getLoc(1, 'j'), getLoc(1, 'k'));
        boolean result3 = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, getLoc(0, 'a'), getLoc(1, 'a'));

        // Assert
        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, getLoc(10, 'a')), Square.NOTHING);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, getLoc(1, 'j')), Square.NOTHING);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a')), Square.NOTHING);
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
    public void placeShip_whenDiagonalPlacementLongerThanShipType_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(4, 'b'), getLoc(7, 'e'));

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
    public void placeShip_whenPlacingShipAtNon45DegreeAngle_ThenPlacementNotAllowedAndLastShipNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(1, 'a'), getLoc(4, 'b'));

        // Assert
        assertFalse(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(4, 'a'), Square.NOTHING);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'b'), getLoc(4, 'b'), Square.NOTHING);
    }

    @Test
    public void placeShip_whenPlacingShipOverAnotherShip_ThenPlacementNotAllowedAndLastShipNotPlaced() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(1, 'a'), getLoc(3, 'a'));
        boolean result = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(3, 'a'), getLoc(6, 'a'));

        // Assert
        assertFalse(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(3, 'a'), Square.CRUISER);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(4, 'a'), getLoc(6, 'a'), Square.NOTHING);

    }

    @Test
    public void placeShip_whenPlacingTwoBattleships_ThenFirstShipLocationChangesToSecondPlacement() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(1, 'a'), getLoc(4, 'a'));
        boolean result = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(3, 'a'), getLoc(6, 'a'));

        // Assert
        assertTrue(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(2, 'a'), Square.NOTHING);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(3, 'a'), getLoc(6, 'a'), Square.BATTLESHIP);
    }

    @Test
    public void placeShip_whenCrossingShipsDiagonally_ThenSecondPlacementFailsAndShipsCannotBeCrossed() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.AIRCRACT_CARRIER, getLoc(1, 'a'), getLoc(5, 'e'));
        boolean notAllowed1 = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(3, 'f'), getLoc(6, 'c'));
        boolean notAllowed2 = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(6, 'c'), getLoc(3, 'f'));

        // Assert
        assertFalse(notAllowed1);
        assertFalse(notAllowed2);

        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(5, 'e'), Square.AIRCRAFT_CARRIER);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(3, 'f'), getLoc(6, 'c'), Square.NOTHING);
    }

    @Test
    public void placeShip_whenDiagonallyPlacedShipsAreClose_ThenPlacementAllowed() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.AIRCRACT_CARRIER, getLoc(1, 'a'), getLoc(5, 'e'));
        boolean allowed1 = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(4, 'g'), getLoc(7, 'd'));
        boolean allowed2 = model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(4, 'c'), getLoc(6, 'a'));
        boolean allowed3 = model.placeShip(Player.PLAYER1, ShipType.DESTROYER1, getLoc(2, 'c'), getLoc(3, 'd'));

        // Assert
        assertTrue(allowed1);
        assertTrue(allowed2);
        assertTrue(allowed3);

        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(5, 'e'), Square.AIRCRAFT_CARRIER);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(4, 'g'), getLoc(7, 'd'), Square.BATTLESHIP);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(4, 'c'), getLoc(6, 'a'), Square.CRUISER);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(2, 'c'), getLoc(3, 'd'), Square.DESTROYER1);

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
            assertEquals(square, model.getSquare(board, getLoc(columnRange[i], (char) rowRange[i])));
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

    private Location getLoc(int col, char row) {
        Location loc = new Location();
        loc.col = col;
        loc.row = row;
        return loc;
    }
}
