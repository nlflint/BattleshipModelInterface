import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class GamePlayTests {
    BattleshipModelInterface model;

    @Before
    public void BeforeEachTest() {
        model = new BattleshipModel();
    }


    @Test
    public void gamePlay_whenPlayer1ShotIsOutOfBounds_thenDoOverIsReturned_stillPlayer1sTurn(){
        //arrange
        setUpStandardGame();

        //act
        Status statusE = model.markShot(new Location(11, 'a'));
        Status statusW = model.markShot(new Location(-1, 'd'));
        Status statusS = model.markShot(new Location(5, 'm'));

        //assert
        assertEquals(Status.DO_OVER, statusE);
        assertEquals(Status.DO_OVER, statusW);
        assertEquals(Status.DO_OVER, statusS);
        assertEquals(Player.PLAYER1, model.whoseTurn());
    }

    @Test
    public void gamePlay_whenPlayer1HitsASpotWhereShipDoesNotExist_thenReturnMiss_Player2sTurn() {
        //arrange
        setUpStandardGame();

        //act
        Status statusMiss = model.markShot(new Location(7, 'a'));

        //assert
        assertEquals(Status.MISS, statusMiss);
        assertEquals(Player.PLAYER2, model.whoseTurn());
    }

    @Test
    public void gamePlay_whenPlayer1MarkShotsWhereShipExists_thenReturnHit_Player1sTurn(){
        //arrange
        setUpStandardGame();

        //act
        Status statusMiss = model.markShot(new Location(2, 'a'));

        //assert
        assertEquals(Status.HIT, statusMiss);
        assertEquals(Player.PLAYER1, model.whoseTurn());


    }

    @Test
    public void gamePlay_whenPlayer1HitsAllSpotsOnAShip_thenReturnSunkenShip_Player1sTurn(){
        //arrange
        setUpStandardGame();

        //act
        model.markShot(new Location(2, 'a'));
        model.markShot(new Location(1, 'a'));
        model.markShot(new Location(3, 'a'));
        Status status = model.markShot(new Location(4, 'a'));

        //assert
        assertEquals(Status.SUNK_BATTLESHIP, status);
        assertEquals(Player.PLAYER1, model.whoseTurn());

    }

    @Test
    public void gamePlay_whenPlayer1HitsSameLocationTwice_thenReturnDoOver_Player1sTurn(){
        //arrange
        setUpStandardGame();

        //act
        model.markShot(new Location(2, 'a'));
        Status status = model.markShot(new Location(2, 'a'));

        //assert
        assertEquals(Status.DO_OVER, status);
        assertEquals(Player.PLAYER1, model.whoseTurn());

    }

    @Test
    public void gamePlay_whenPlayer2ShotIsOutOfBounds_thenDoOverIsReturned_stillPlayer2sTurn(){
        //arrange
        setUpStandardGame();

        //act
        model.markShot(new Location(9, 'b'));
        Status statusE = model.markShot(new Location(11, 'a'));
        Status statusW = model.markShot(new Location(-1, 'd'));
        Status statusS = model.markShot(new Location(5, 'm'));

        //assert
        assertEquals(Status.DO_OVER, statusE);
        assertEquals(Status.DO_OVER, statusW);
        assertEquals(Status.DO_OVER, statusS);
        assertEquals(Player.PLAYER2, model.whoseTurn());
    }

    @Test
    public void gamePlay_whenPlayer2HitsASpotWhereShipDoesNotExist_thenReturnMiss_Player1sTurn() {
        //arrange
        setUpStandardGame();

        //act
        model.markShot(new Location(7, 'a'));
        Status statusMiss = model.markShot(new Location(7, 'a'));

        //assert
        assertEquals(Status.MISS, statusMiss);
        assertEquals(Player.PLAYER1, model.whoseTurn());
    }

    @Test
    public void gamePlay_whenPlayer2MarkShotsWhereShipExists_thenReturnHit_Player2sTurn(){
        //arrange
        setUpStandardGame();

        //act
        model.markShot(new Location(7, 'a'));
        Status status = model.markShot(new Location(3, 'a'));

        //assert
        assertEquals(Status.HIT, status);
        assertEquals(Player.PLAYER2, model.whoseTurn());

    }

    @Test
    public void gamePlay_whenPlayer1SinksAllPlayerTwosShips_thenReturnGameOver_Player1Wins(){
        //arrange
        setUpStandardGame();


    }



    private void setUpStandardGame() {

        model.placeShip(Player.PLAYER1,ShipType.BATTLESHIP, new Location(1, 'a'), new Location(4, 'a'));
        model.placeShip(Player.PLAYER1,ShipType.AIRCRACT_CARRIER, new Location(1, 'b'), new Location(5, 'b'));
        model.placeShip(Player.PLAYER1,ShipType.CRUISER, new Location(1, 'c'), new Location(3, 'c'));
        model.placeShip(Player.PLAYER1,ShipType.DESTROYER1, new Location(1, 'e'), new Location(2, 'e'));
        model.placeShip(Player.PLAYER1,ShipType.DESTROYER2, new Location(1, 'f'), new Location(2, 'f'));

        model.placeShip(Player.PLAYER2,ShipType.BATTLESHIP, new Location(1, 'a'), new Location(4, 'a'));
        model.placeShip(Player.PLAYER2,ShipType.AIRCRACT_CARRIER, new Location(1, 'b'), new Location(5, 'b'));
        model.placeShip(Player.PLAYER2,ShipType.CRUISER, new Location(1, 'c'), new Location(3, 'c'));
        model.placeShip(Player.PLAYER2,ShipType.DESTROYER1, new Location(1, 'e'), new Location(2, 'e'));
        model.placeShip(Player.PLAYER2,ShipType.DESTROYER2, new Location(1, 'f'), new Location(2, 'f'));

        model.startGame();

    }
}
