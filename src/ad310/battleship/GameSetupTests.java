package ad310.battleship;

import org.junit.*;


import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;

public class GameSetupTests {
    BattleshipModelInterface model;
    private final Board player1 = Board.PLAYER1_DEFENSIVE;
    private final Board player2 = Board.PLAYER2_DEFENSIVE;

    @Before
    public void BeforeEachTest() {
        model = new BattleshipModel(new Config());
    }

    @Test
    public void gameSetup_whenPlacingDestoryer1Horizontally_ThenGetSquareReturnsShipAtTwoLocations() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER1, getLoc(1, 'a'), getLoc(1, 'b'));

        // Assert
        assertTrue(result);
        assertEquals(model.getSquare(player1, getLoc(1, 'a')), Square.DESTROYER1);
        assertEquals(model.getSquare(player1, getLoc(1, 'b')), Square.DESTROYER1);
    }


    @Test
    public void gameSetup_whenPlacingDestoryer2_ThenGetSquareReturnsShipAtTwoLocations() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, getLoc(10, 'j'), getLoc(9, 'j'));

        // Assert
        assertTrue(result);
        assertEquals(model.getSquare(player1, getLoc(9, 'j')), Square.DESTROYER2);
        assertEquals(model.getSquare(player1, getLoc(10, 'j')), Square.DESTROYER2);
    }

    @Test
    public void gameSetup_whenShipTypeIsNotLegal_ThenShipCannotBePlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.SUBMARINE, getLoc(10, 'j'), getLoc(8, 'j'));

        // Assert
        assertFalse(result);
        assertEquals(Square.NOTHING, model.getSquare(player1, getLoc(8, 'j')));
        assertEquals(Square.NOTHING, model.getSquare(player1, getLoc(9, 'j')));
        assertEquals(Square.NOTHING, model.getSquare(player1, getLoc(10, 'j')));
    }
    @Test
    public void gameSetup_whenMiniSubIsLegal_ThenShipCanBePlaced() {
        // Arrange
        HashSet<ShipType> ships = new HashSet<>();
        ships.add(ShipType.MINISUB1);
        Config config = new Config(10, true, true, ships);
        model = new BattleshipModel(config);
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.MINISUB1, getLoc(10, 'j'), getLoc(10, 'j'));

        // Assert
        assertTrue(result);
        assertEquals(Square.MINISUB1, model.getSquare(player1, getLoc(10, 'j')));

    }

    @Test
    public void gameSetup_whenPlacingCrusierVertically_ThenGetSquareReturnsShipAtThreeLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.CRUISER, getLoc(1, 'a'), getLoc(1, 'c'));

        // Assert
        assertEquals(model.getSquare(player2, getLoc(1, 'a')), Square.CRUISER);
        assertEquals(model.getSquare(player2, getLoc(1, 'b')), Square.CRUISER);
        assertEquals(model.getSquare(player2, getLoc(1, 'c')), Square.CRUISER);
    }

    @Test
    public void gameSetup_whenPlacingCrusierBottomToTop_ThenGetSquareReturnsShipAtThreeLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.CRUISER, getLoc(1, 'c'), getLoc(1, 'a'));

        // Assert
        assertEquals(model.getSquare(player2, getLoc(1, 'a')), Square.CRUISER);
        assertEquals(model.getSquare(player2, getLoc(1, 'b')), Square.CRUISER);
        assertEquals(model.getSquare(player2, getLoc(1, 'c')), Square.CRUISER);
    }

    @Test
    public void gameSetup_whenPlacingBattleshipDiagonallyNWtoSE_ThenGetSquareReturnsShipAtFourLocations() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(1, 'a'), getLoc(4, 'd'));

        // Assert
        assertEquals(model.getSquare(player1, getLoc(1, 'a')), Square.BATTLESHIP);
        assertEquals(model.getSquare(player1, getLoc(2, 'b')), Square.BATTLESHIP);
        assertEquals(model.getSquare(player1, getLoc(3, 'c')), Square.BATTLESHIP);
        assertEquals(model.getSquare(player1, getLoc(4, 'd')), Square.BATTLESHIP);
    }

    @Test
    public void gameSetup_whenPlacingAircraftCarrierDiagonalySWtoNE_ThenGetSquareReturnsShipAtFiveLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.AIRCRAFT_CARRIER, getLoc(1, 'a'), getLoc(5, 'e'));

        // Assert
        assertEquals(model.getSquare(player2, getLoc(1, 'a')), Square.AIRCRAFT_CARRIER);
        assertEquals(model.getSquare(player2, getLoc(2, 'b')), Square.AIRCRAFT_CARRIER);
        assertEquals(model.getSquare(player2, getLoc(3, 'c')), Square.AIRCRAFT_CARRIER);
        assertEquals(model.getSquare(player2, getLoc(4, 'd')), Square.AIRCRAFT_CARRIER);
        assertEquals(model.getSquare(player2, getLoc(5, 'e')), Square.AIRCRAFT_CARRIER);
    }

    @Test
    public void gameSetup_whenPlacingAircraftCarrierDiagonallySEtoNW_ThenGetSquareReturnsShipAtFiveLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.AIRCRAFT_CARRIER, getLoc(1, 'e'), getLoc(5, 'a'));

        // Assert
        assertEquals(model.getSquare(player2, getLoc(1, 'e')), Square.AIRCRAFT_CARRIER);
        assertEquals(model.getSquare(player2, getLoc(2, 'd')), Square.AIRCRAFT_CARRIER);
        assertEquals(model.getSquare(player2, getLoc(3, 'c')), Square.AIRCRAFT_CARRIER);
        assertEquals(model.getSquare(player2, getLoc(4, 'b')), Square.AIRCRAFT_CARRIER);
        assertEquals(model.getSquare(player2, getLoc(5, 'a')), Square.AIRCRAFT_CARRIER);
    }
    @Test
    public void gameSetup_whenDiagonalPLacementIsDisabledAndPlacingShipDiagonally_ThenAPlacementNotAllowed() {
        // Arrange
        Config config = new Config(10, true, false, createDefaultShipConfig());
        model = new BattleshipModel(config);
        // Act
        boolean result = model.placeShip(Player.PLAYER2, ShipType.AIRCRAFT_CARRIER, getLoc(1, 'e'), getLoc(5, 'a'));

        // Assert
        assertFalse(result);
        assertEquals(model.getSquare(player2, getLoc(1, 'e')), Square.NOTHING);
        assertEquals(model.getSquare(player2, getLoc(2, 'd')), Square.NOTHING);
        assertEquals(model.getSquare(player2, getLoc(3, 'c')), Square.NOTHING);
        assertEquals(model.getSquare(player2, getLoc(4, 'b')), Square.NOTHING);
        assertEquals(model.getSquare(player2, getLoc(5, 'a')), Square.NOTHING);
    }

    private HashSet<ShipType> createDefaultShipConfig() {
        HashSet<ShipType> ships = new HashSet<>();
        ships.add(ShipType.AIRCRAFT_CARRIER);
        ships.add(ShipType.BATTLESHIP);
        ships.add(ShipType.CRUISER);
        ships.add(ShipType.DESTROYER1);
        ships.add(ShipType.DESTROYER2);
        return ships;
    }

    @Test
    public void gameSetup_whenPlacingAircraftCarrierDiagonallyNEtoSW_ThenGetSquareReturnsShipAtFiveLocations() {
        // Act
        model.placeShip(Player.PLAYER2, ShipType.AIRCRAFT_CARRIER, getLoc(5, 'a'), getLoc(1, 'e'));

        // Assert
        assertEquals(model.getSquare(player2, getLoc(1, 'e')), Square.AIRCRAFT_CARRIER);
        assertEquals(model.getSquare(player2, getLoc(2, 'd')), Square.AIRCRAFT_CARRIER);
        assertEquals(model.getSquare(player2, getLoc(3, 'c')), Square.AIRCRAFT_CARRIER);
        assertEquals(model.getSquare(player2, getLoc(4, 'b')), Square.AIRCRAFT_CARRIER);
        assertEquals(model.getSquare(player2, getLoc(5, 'a')), Square.AIRCRAFT_CARRIER);
    }

    @Test
    public void gameSetup_whenPlacingShipOffEdgeOfBoard_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
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
    public void gameSetup_whenBoardIs8x8_ThenPlacingShipOutsideBoundsIsNotAllowed() {
        // Arrange
        Config config = new Config(8, true, true, createDefaultShipConfig());
        model = new BattleshipModel(config);
        // Act
        boolean result1 = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, getLoc(8, 'a'), getLoc(9, 'a'));
        boolean result2 = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, getLoc(1, 'h'), getLoc(1, 'j'));
        boolean result3 = model.placeShip(Player.PLAYER1, ShipType.DESTROYER2, getLoc(8, 'h'), getLoc(9, 'j'));

        // Assert
        assertFalse(result1);
        assertFalse(result2);
        assertFalse(result3);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, getLoc(8, 'a')), Square.NOTHING);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, getLoc(1, 'h')), Square.NOTHING);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, getLoc(8, 'h')), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenLocationsAreShorterThanShip_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(1, 'a'), getLoc(2, 'a'));

        // Assert
        assertFalse(result);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a')), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenLocationsAreLongerThanShip_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(1, 'a'), getLoc(7, 'a'));

        // Assert
        assertFalse(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(7, 'a'), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenDiagonalPlacementLongerThanShipType_ThenPlaceShipReturnsFalseAndShipIsNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(4, 'b'), getLoc(7, 'e'));

        // Assert
        assertFalse(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(7, 'a'), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenOnPlayer1Board_ThenAllBoardExceptPlayer1DefensiveShowNothing() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(1, 'a'), getLoc(3, 'a'));

        // Assert
        assertSquareEqualsLocationRange(Board.PLAYER2_DEFENSIVE, getLoc(1, 'a'), getLoc(3, 'a'), Square.NOTHING);
        assertSquareEqualsLocationRange(Board.PLAYER2_OFFENSIVE, getLoc(1, 'a'), getLoc(3, 'a'), Square.NOTHING);
        assertSquareEqualsLocationRange(Board.PLAYER1_OFFENSIVE, getLoc(1, 'a'), getLoc(3, 'a'), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenPlacingShipAtNon45DegreeAngle_ThenPlacementNotAllowedAndLastShipNotPlaced() {
        // Act
        boolean result = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(1, 'a'), getLoc(4, 'b'));

        // Assert
        assertFalse(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(4, 'a'), Square.NOTHING);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'b'), getLoc(4, 'b'), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenPlacingShipOverAnotherShip_ThenPlacementNotAllowedAndLastShipNotPlaced() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.CRUISER, getLoc(1, 'a'), getLoc(3, 'a'));
        boolean result = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(3, 'a'), getLoc(6, 'a'));

        // Assert
        assertFalse(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(3, 'a'), Square.CRUISER);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(4, 'a'), getLoc(6, 'a'), Square.NOTHING);

    }

    @Test
    public void gameSetup_whenPlacingTwoBattleships_ThenFirstShipLocationChangesToSecondPlacement() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(1, 'a'), getLoc(4, 'a'));
        boolean result = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(3, 'a'), getLoc(6, 'a'));

        // Assert
        assertTrue(result);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(2, 'a'), Square.NOTHING);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(3, 'a'), getLoc(6, 'a'), Square.BATTLESHIP);
    }

    @Test
    public void gameSetup_whenCrossingShipsDiagonally_ThenSecondPlacementFailsAndShipsCannotBeCrossed() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.AIRCRAFT_CARRIER, getLoc(1, 'a'), getLoc(5, 'e'));
        boolean notAllowed1 = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(3, 'f'), getLoc(6, 'c'));
        boolean notAllowed2 = model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(6, 'c'), getLoc(3, 'f'));

        // Assert
        assertFalse(notAllowed1);
        assertFalse(notAllowed2);

        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(1, 'a'), getLoc(5, 'e'), Square.AIRCRAFT_CARRIER);
        assertSquareEqualsLocationRange(Board.PLAYER1_DEFENSIVE, getLoc(3, 'f'), getLoc(6, 'c'), Square.NOTHING);
    }

    @Test
    public void gameSetup_whenDiagonallyPlacedShipsAreClose_ThenPlacementAllowed() {
        // Act
        model.placeShip(Player.PLAYER1, ShipType.AIRCRAFT_CARRIER, getLoc(1, 'a'), getLoc(5, 'e'));
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
    public void gameSetup_whenPlayerTriesToMarkShot_thenStatusNotAllowed(){
        //act
        Status status1 = model.markShot(getLoc(1,'a'));

        //assert
        assertEquals(Status.NOT_ALLOWED, status1);


    }
    @Test
    public void divideByZero(){
        // Act
        model.placeShip(Player.PLAYER1, ShipType.AIRCRAFT_CARRIER, getLoc(10, 'a'), getLoc(10, 'e'));
        model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(1, 'a'), getLoc(1, 'd'));


    }
    @Test
    public void whenStartingGameWhenNotAllShipsArePlaced_ThenStartGameReturnsFalse_andPlayerCanPlaceAnotherShip(){
        //act
        model.placeShip(Player.PLAYER1, ShipType.AIRCRAFT_CARRIER, getLoc(10, 'a'), getLoc(10, 'e'));
        Boolean result = model.startGame();
        model.placeShip(Player.PLAYER1, ShipType.BATTLESHIP, getLoc(1, 'a'), getLoc(1, 'd'));


        //assert
        assertEquals(false,result);
        assertEquals(Square.BATTLESHIP, model.getSquare(Board.PLAYER1_DEFENSIVE, getLoc(1,'a')));

    }

    @Test
    public void whenStartingGameWith1Shiptypes_GameStartsAfterShipHasBeenPlaced(){
        // Arrange
        HashSet<ShipType> ships = new HashSet<>();
        ships.add(ShipType.MINISUB2);
        Config config = new Config(10, true, true, ships);
        model = new BattleshipModel(config);

        //act
        model.placeShip(Player.PLAYER1, ShipType.MINISUB2, getLoc(10, 'a'), getLoc(10, 'a'));
        model.placeShip(Player.PLAYER2, ShipType.MINISUB2, getLoc(1, 'a'), getLoc(1, 'a'));
        boolean result = model.startGame();


        //assert
        assertTrue(result);

    }

    private Location getLoc(int col, char row) {
        return new Location(col, row);
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

}
