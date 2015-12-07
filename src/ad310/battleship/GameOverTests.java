package ad310.battleship;

import org.junit.*;


import static org.junit.Assert.*;

public class GameOverTests {
    BattleshipModelInterface model;

    @Before
    public void BeforeEachTest() {
        model = new BattleshipModel(new Config());
        putModelIntoGameOverMode();
    }


    @Test
    public void whenPlayersTriesToPlaceShip_thenPlaceShipReturnsFalse(){
        //act
        Boolean place1 = model.placeShip(Player.PLAYER2,ShipType.BATTLESHIP, getLoc(2, 'a'), getLoc(5, 'a'));
        Boolean place2 = model.placeShip(Player.PLAYER1,ShipType.BATTLESHIP, getLoc(6, 'a'), getLoc(9, 'a'));

        //assert
        assertEquals(false, place1);
        assertEquals(false, place2);
    }
    
    @Test
    public void whenPlayersTriesToMarkShot_thenStatusReturnsNotAllowed(){
        //act
        Status status1 = model.markShot(getLoc(1,'a'));
        //assert
        assertEquals(Status.NOT_ALLOWED, status1);
    }

    @Test
    public void whenGameOver_LoserCanSeeShipsOnOffensiveBoard(){
        //act
        Square result = model.getSquare(Board.PLAYER1_OFFENSIVE, getLoc(1, 'a'));

        //assert
        assertEquals(Square.BATTLESHIP, result);

    }

    @Test
    public void whenPlayerWins_getWinnerReturnsPlayerThatWon(){
        //act
        Player winner = model.getWinner();

        //assert
        assertEquals(Player.PLAYER2, winner);

    }
    private void setUpStandardGame() {

        model.placeShip(Player.PLAYER1,ShipType.BATTLESHIP, getLoc(5, 'a'), getLoc(8, 'a'));
        model.placeShip(Player.PLAYER1,ShipType.AIRCRAFT_CARRIER, getLoc(5, 'b'), getLoc(9, 'b'));
        model.placeShip(Player.PLAYER1,ShipType.CRUISER, getLoc(5, 'c'), getLoc(7, 'c'));
        model.placeShip(Player.PLAYER1,ShipType.DESTROYER1, getLoc(5, 'e'), getLoc(6, 'e'));
        model.placeShip(Player.PLAYER1,ShipType.DESTROYER2, getLoc(5, 'f'), getLoc(6, 'f'));

        model.placeShip(Player.PLAYER2,ShipType.BATTLESHIP, getLoc(1, 'a'), getLoc(4, 'a'));
        model.placeShip(Player.PLAYER2,ShipType.AIRCRAFT_CARRIER, getLoc(1, 'b'), getLoc(5, 'b'));
        model.placeShip(Player.PLAYER2,ShipType.CRUISER, getLoc(1, 'c'), getLoc(3, 'c'));
        model.placeShip(Player.PLAYER2,ShipType.DESTROYER1, getLoc(1, 'e'), getLoc(2, 'e'));
        model.placeShip(Player.PLAYER2,ShipType.DESTROYER2, getLoc(1, 'f'), getLoc(2, 'f'));

        model.startGame();

    }
    public void putModelIntoGameOverMode(){
        //arrange
        setUpStandardGame();

        //player one miss
        model.markShot(getLoc(7,'a'));
        //act - sink battleship
        model.markShot(getLoc(5, 'a'));
        model.markShot(getLoc(6, 'a'));
        model.markShot(getLoc(7, 'a'));
        model.markShot(getLoc(8, 'a'));
        //sink - aircraft carrier
        model.markShot(getLoc(5, 'b'));
        model.markShot(getLoc(6, 'b'));
        model.markShot(getLoc(7,'b'));
        model.markShot(getLoc(8, 'b'));
        model.markShot(getLoc(9, 'b'));
        //sink cruiser
        model.markShot(getLoc(5, 'c'));
        model.markShot(getLoc(6, 'c'));
        model.markShot(getLoc(7, 'c'));
        //sink destroyer 1
        model.markShot(getLoc(5, 'e'));
        model.markShot(getLoc(6, 'e'));
        //sink destroyer 2
        model.markShot(getLoc(5, 'f'));
        model.markShot(getLoc(6, 'f'));


    }
    private Location getLoc(int col, char row) {
        return new Location(col, row);
    }
}
