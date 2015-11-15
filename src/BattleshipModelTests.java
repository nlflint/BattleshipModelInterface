import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BattleshipModelTests {
    BattleshipModelInterface model;

    @Before
    public void BeforeEachTest() {
        //model = new BattleshipModel();
    }

    @Test
    public void placeShip_whenPlacingValidLocation_ThenGetSquareReturnsShipAtLocaitons() {
        model.placeShip(Player.PLAYER1, ShipType.DESTROYER, location(1, 'a'), location(1,'b'));

        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, location(1, 'a')), Square.DESTROYER);
        assertEquals(model.getSquare(Board.PLAYER1_DEFENSIVE, location(1, 'b')), Square.DESTROYER);
    }

    private Location location(int col, char row) {
        Location loc = new Location();
        loc.col = col;
        loc.row = row;
        return loc;
    }
}
