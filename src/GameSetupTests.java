import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameSetupTests {
    BattleshipModelInterface model;

    @Before
    public void BeforeEachTest() {
        model = new BattleshipModel();
    }

    @Test
    public void gameSetup_whenPlacingDestoryer1Horizontally_ThenGetSquareReturnsShipAtTwoLocations() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER1, new Location(1, 'a'), new Location(1, 'b'));

        // Assert
        assertTrue(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(1, 'a'), new Location(1, 'b'), Square.DESTROYER1);
    }

    @Test
    public void gameSetup_whenPlacingDestoryer2_ThenGetSquareReturnsShipAtTwoLocations() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, new Location(10, 'j'), new Location(9, 'j'));

        // Assert
        assertTrue(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(9, 'j'), new Location(10, 'j'), Square.DESTROYER2);
    }

    @Test
    public void gameSetup_whenPlacingCrusierVertically_ThenGetSquareReturnsShipAtThreeLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.CRUISER, new Location(1, 'a'), new Location(1, 'c'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, new Location(1, 'a'), new Location(1, 'c'), Square.CRUISER);
    }

    @Test
    public void gameSetup_whenPlacingCrusierBottomToTop_ThenGetSquareReturnsShipAtThreeLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.CRUISER, new Location(1, 'c'), new Location(1, 'a'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, new Location(1, 'a'), new Location(1, 'c'), Square.CRUISER);
    }

    @Test
    public void gameSetup_whenPlacingBattleshipDiagonallyNWtoSE_ThenGetSquareReturnsShipAtFourLocations() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, new Location(1, 'a'), new Location(4, 'd'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(1, 'a'), new Location(4, 'd'), Square.BATTLESHIP);
    }

    @Test
    public void gameSetup_whenPlacingAircraftCarrierDiagonallySWtoNE_ThenGetSquareReturnsShipAtFiveLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.AIRCRACT_CARRIER, new Location(1, 'a'), new Location(5, 'e'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, new Location(5, 'e'), new Location(1, 'a'), Square.AIRCRAFT_CARRIER);
    }

    @Test
    public void gameSetup_whenPlacingAircraftCarrierDiagonallySEtoNW_ThenGetSquareReturnsShipAtFiveLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.AIRCRACT_CARRIER, new Location(1, 'e'), new Location(5, 'a'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, new Location(5, 'a'), new Location(1, 'e'), Square.AIRCRAFT_CARRIER);
    }

    @Test
    public void gameSetup_whenPlacingAircraftCarrierDiagonallyNEtoSW_ThenGetSquareReturnsShipAtFiveLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.AIRCRACT_CARRIER, new Location(1, 'e'), new Location(5, 'a'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, new Location(5, 'a'), new Location(1, 'e'), Square.AIRCRAFT_CARRIER);
    }

    @Test
    public void gameSetup_whenPlacingShipOffEdgeOfBoard_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result1 = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, new Location(10, 'a'), new Location(11, 'a'));
        boolean result2 = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, new Location(1, 'j'), new Location(1, 'k'));
        boolean result3 = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, new Location(0, 'a'), new Location(1, 'a'));

        // Assert
        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, new Location(10, 'a')), Square.NOTHING);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, new Location(1, 'j')), Square.NOTHING);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, new Location(1, 'a')), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenLocationsAreShorterThanShip_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, new Location(1, 'a'), new Location(2, 'a'));

        // Assert
        assertFalse(result);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, new Location(1, 'a')), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenLocationsAreLongerThanShip_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, new Location(1, 'a'), new Location(7, 'a'));

        // Assert
        assertFalse(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(1, 'a'), new Location(7, 'a'), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenDiagonalPlacementLongerThanShipType_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, new Location(4, 'b'), new Location(7, 'e'));

        // Assert
        assertFalse(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(1, 'a'), new Location(7, 'a'), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenOnPlayer1Board_ThenAllBoardExceptPlayer1DefensiveShowNothing() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, new Location(1, 'a'), new Location(3, 'a'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, new Location(1, 'a'), new Location(3, 'a'), Square.NOTHING);
        assertSquareEqualsLocationRange(Board.PLAYER2_OFFENSIVE, new Location(1, 'a'), new Location(3, 'a'), Square.NOTHING);
        assertSquareEqualsLocationRange(Board.PLAYER1_OFFENSIVE, new Location(1, 'a'), new Location(3, 'a'), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenPlacingShipAtNon45DegreeAngle_ThenPlacementNotAllowedAndLastShipNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, new Location(1, 'a'), new Location(4, 'b'));

        // Assert
        assertFalse(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(1, 'a'), new Location(4, 'a'), Square.NOTHING);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(1, 'b'), new Location(4, 'b'), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenPlacingShipOverAnotherShip_ThenPlacementNotAllowedAndLastShipNotPlaced() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.CRUISER, new Location(1, 'a'), new Location(3, 'a'));
        boolean result = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, new Location(3, 'a'), new Location(6, 'a'));

        // Assert
        assertFalse(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(1, 'a'), new Location(3, 'a'), Square.CRUISER);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(4, 'a'), new Location(6, 'a'), Square.NOTHING);

    }

    @Test
    public void gameSetup_whenPlacingTwoBattleships_ThenFirstShipLocationChangesToSecondPlacement() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, new Location(1, 'a'), new Location(4, 'a'));
        boolean result = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, new Location(3, 'a'), new Location(6, 'a'));

        // Assert
        assertTrue(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(1, 'a'), new Location(2, 'a'), Square.NOTHING);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(3, 'a'), new Location(6, 'a'), Square.BATTLESHIP);
    }

    @Test
    public void gameSetup_whenCrossingShipsDiagonally_ThenSecondPlacementFailsAndShipsCannotBeCrossed() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.AIRCRACT_CARRIER, new Location(1, 'a'), new Location(5, 'e'));
        boolean notAllowed1 = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, new Location(3, 'f'), new Location(6, 'c'));
        boolean notAllowed2 = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, new Location(6, 'c'), new Location(3, 'f'));

        // Assert
        assertFalse(notAllowed1);
        assertFalse(notAllowed2);

        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(1, 'a'), new Location(5, 'e'), Square.AIRCRAFT_CARRIER);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(3, 'f'), new Location(6, 'c'), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenDiagonallyPlacedShipsAreClose_ThenPlacementAllowed() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.AIRCRACT_CARRIER, new Location(1, 'a'), new Location(5, 'e'));
        boolean allowed1 = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, new Location(4, 'g'), new Location(7, 'd'));
        boolean allowed2 = model.placeShip(Player.PLAYER1, ShipType.CRUISER, new Location(4, 'c'), new Location(6, 'a'));
        boolean allowed3 = model.placeShip(Player.PLAYER1, ShipType.DESTROYER1, new Location(2, 'c'), new Location(3, 'd'));

        // Assert
        assertTrue(allowed1);
        assertTrue(allowed2);
        assertTrue(allowed3);

        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(1, 'a'), new Location(5, 'e'), Square.AIRCRAFT_CARRIER);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(4, 'g'), new Location(7, 'd'), Square.BATTLESHIP);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(4, 'c'), new Location(6, 'a'), Square.CRUISER);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, new Location(2, 'c'), new Location(3, 'd'), Square.DESTROYER1);

    }

    private void assertSquareEqualsLocationRange(Board board, Location start, Location end, Square square) {
        int longestRange = Math.max(getRangeLength(start.col, end.col), getRangeLength(start.row, end.row));
        int[] columnRange = getRange(start.col, end.col, longestRange);
        int[] rowRange = getRange(start.row, end.row, longestRange);

        for (int i = 0; i < columnRange.length; i++)
            assertEquals(square, model.getSquare(board, new Location(columnRange[i], (char) rowRange[i])));
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

}
