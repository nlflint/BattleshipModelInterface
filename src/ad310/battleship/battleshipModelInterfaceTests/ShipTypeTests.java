package ad310.battleship.battleshipModelInterfaceTests;

import ad310.battleship.battleshipModelInterface.ShipType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by NathanF on 12/4/2015.
 */
public class ShipTypeTests {
    @Test
    public void isMember_WhenShipTypeMatchesEnum_ThenReturnsTrue() {
        // Act
        boolean result = ShipType.isMember("BATTLESHIP");

        // Assert
        assertTrue(result);
    }

    @Test
    public void isMember_WhenShipTypeHasDifferentCasing_ThenReturnsFalse() {
        // Act
        boolean result = ShipType.isMember("BattleShip");

        // Assert
        assertFalse(result);
    }

    @Test
    public void isMember_WhenShipTypeIsTotallyDifferent_ThenReturnsFalse() {
        // Act
        boolean result = ShipType.isMember("Super Duper Different");

        // Assert
        assertFalse(result);
    }

}
