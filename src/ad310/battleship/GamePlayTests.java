package ad310.battleship;

import org.junit.*;


import static org.junit.Assert.*;


public class GamePlayTests {
    BattleshipModelInterface model;

    @Before
    public void BeforeEachTest() {
        model = new BattleshipModel(new Config());
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
    public void gamePlay_whenPlayer1ShotIsOutOfBoundsOn12x12Board_thenDoOverIsReturned_stillPlayer1sTurn(){
        //arrange
        Config config = new Config(12);
        setUpCustomGame(config);

        //act
        Status statusE = model.markShot(new Location(13, 'a'));
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
        assertEquals(Square.MISS, model.getSquare(Board.PLAYER1_OFFENSIVE, new Location(7,'a')));
        assertEquals(Square.MISS, model.getSquare(Board.PLAYER2_DEFENSIVE, new Location(7,'a')));
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
        assertEquals(Square.HIT, model.getSquare(Board.PLAYER2_DEFENSIVE, new Location (2,'a')));
        assertEquals(Square.HIT, model.getSquare(Board.PLAYER1_OFFENSIVE, new Location (2,'a')));
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
    public void gamePlay_whenPlayer2ShotsASpotWhereShipDoesNotExist_thenReturnMiss_Player1sTurn() {
        //arrange
        setUpStandardGame();

        //act
        model.markShot(new Location(7, 'a'));
        Status statusMiss = model.markShot(new Location(1, 'a'));

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
        Status status = model.markShot(new Location(5, 'a'));

        //assert
        assertEquals(Status.HIT, status);
        assertEquals(Player.PLAYER2, model.whoseTurn());

    }

    @Test
    public void gamePlay_whenPlayer1SinksAllPlayerTwosShips_thenPlayer1Wins(){
        //arrange
        setUpStandardGame();

        //act - sink battleship
        model.markShot(new Location(1, 'a'));
        model.markShot(new Location(2, 'a'));
        model.markShot(new Location(3, 'a'));
        model.markShot(new Location(4, 'a'));
        //sink - aircraft carrier
        model.markShot(new Location(1, 'b'));
        model.markShot(new Location(2, 'b'));
        model.markShot(new Location(3, 'b'));
        model.markShot(new Location(4, 'b'));
        model.markShot(new Location(5, 'b'));
        //sink cruiser
        model.markShot(new Location(1, 'c'));
        model.markShot(new Location(2, 'c'));
        model.markShot(new Location(3, 'c'));
        //sink destroyer 1
        model.markShot(new Location(1, 'e'));
        model.markShot(new Location(2, 'e'));
        //sink destroyer 2
        model.markShot(new Location(1, 'f'));
        Status status = model.markShot(new Location(2, 'f'));

        //assert
        assertEquals(Status.PLAYER1_WINS, status);
    }

    @Test
    public void gamePlay_whenPlayer2SinksAllPlayerOnesShips_thenPlayer2Wins(){
        //arrange
        setUpStandardGame();

        //player one miss
        model.markShot(new Location(7,'a'));
        //act - sink battleship
        model.markShot(new Location(5, 'a'));
        model.markShot(new Location(6, 'a'));
        model.markShot(new Location(7, 'a'));
        model.markShot(new Location(8, 'a'));
        //sink - aircraft carrier
        model.markShot(new Location(5, 'b'));
        model.markShot(new Location(6, 'b'));
        model.markShot(new Location(7,'b'));
        model.markShot(new Location(8, 'b'));
        model.markShot(new Location(9, 'b'));
        //sink cruiser
        model.markShot(new Location(5, 'c'));
        model.markShot(new Location(6, 'c'));
        model.markShot(new Location(7, 'c'));
        //sink destroyer 1
        model.markShot(new Location(5, 'e'));
        model.markShot(new Location(6, 'e'));
        //sink destroyer 2
        model.markShot(new Location(5, 'f'));
        Status status = model.markShot(new Location(6, 'f'));

        //assert
        assertEquals(Status.PLAYER2_WINS, status);
    }

    @Test
    public void gamePlay_whenPlayerLooksAtOffensiveBoard_theyShallNotSeePlayer1sOrPlayer2sShips(){
        //setup
        setUpStandardGame();

        //assert
        assertEquals(Square.NOTHING,model.getSquare(Board.PLAYER1_OFFENSIVE, new Location(5, 'a')));
        assertEquals(Square.NOTHING,model.getSquare(Board.PLAYER1_OFFENSIVE, new Location(1, 'c')));
        assertEquals(Square.NOTHING,model.getSquare(Board.PLAYER2_OFFENSIVE, new Location(1, 'c')));
        assertEquals(Square.NOTHING,model.getSquare(Board.PLAYER2_OFFENSIVE, new Location(5, 'a')));

    }

    @Test
    public void gamePlay_whenPlayerTriesToPlaceShip_returnFalse(){
        //setUp
        setUpStandardGame();

        //assert
        assertFalse(model.placeShip(Player.PLAYER1,ShipType.BATTLESHIP, new Location(6, 'a'), new Location(9, 'a')));
        assertFalse(model.placeShip(Player.PLAYER2,ShipType.BATTLESHIP, new Location(2, 'a'), new Location(5, 'a')));

    }

    private void setUpStandardGame() {

        model.placeShip(Player.PLAYER1,ShipType.BATTLESHIP, new Location(5, 'a'), new Location(8, 'a'));
        model.placeShip(Player.PLAYER1,ShipType.AIRCRAFT_CARRIER, new Location(5, 'b'), new Location(9, 'b'));
        model.placeShip(Player.PLAYER1,ShipType.CRUISER, new Location(5, 'c'), new Location(7, 'c'));
        model.placeShip(Player.PLAYER1,ShipType.DESTROYER1, new Location(5, 'e'), new Location(6, 'e'));
        model.placeShip(Player.PLAYER1,ShipType.DESTROYER2, new Location(5, 'f'), new Location(6, 'f'));

        model.placeShip(Player.PLAYER2,ShipType.BATTLESHIP, new Location(1, 'a'), new Location(4, 'a'));
        model.placeShip(Player.PLAYER2,ShipType.AIRCRAFT_CARRIER, new Location(1, 'b'), new Location(5, 'b'));
        model.placeShip(Player.PLAYER2,ShipType.CRUISER, new Location(1, 'c'), new Location(3, 'c'));
        model.placeShip(Player.PLAYER2,ShipType.DESTROYER1, new Location(1, 'e'), new Location(2, 'e'));
        model.placeShip(Player.PLAYER2,ShipType.DESTROYER2, new Location(1, 'f'), new Location(2, 'f'));

        model.startGame();

    }
    private void setUpCustomGame(Config config) {
        model = new BattleshipModel(config);
        setUpStandardGame();

    }
}
