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
        Status statusMiss = model.markShot(new Location(1, 'a'));

        //assert
        assertEquals(Status.MISS, statusMiss);
        assertEquals(Player.PLAYER2, model.whoseTurn());
    }

    private void setUpStandardGame() {
        model.startGame();
    }
}
