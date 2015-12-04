package ad310.battleship.configTests;

import ad310.battleship.config.Config;
import ad310.battleship.config.ConfigShip;
import org.junit.Test;

/**
 /**
 * Created by NathanF on 12/4/2015.
 */
public class ConfigTests {
    @Test
    public void config_WhenConfigObjectIsWritten_ThenStreamContainsValuesFromObject() {
        // Arrange
        ConfigShip[] ships = new ConfigShip[] {

        };

        ConfigShip asdf = new ConfigShip("asd", 13);
        Config config = new Config(ships, 10, true, true);
    }

}