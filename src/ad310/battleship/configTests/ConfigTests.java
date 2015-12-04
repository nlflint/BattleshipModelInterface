package ad310.battleship.configTests;

import ad310.battleship.config.Config;
import ad310.battleship.config.ConfigShip;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;

/**
 /**
 * Created by NathanF on 12/4/2015.
 */
public class ConfigTests {
    @Test
    public void config_WhenConfigIsConvertedToXML_ThenAfterConvertingBackToObjectThePropertiesMatch() {
        // Arrange
        ConfigShip[] ships = new ConfigShip[] {
                new ConfigShip("Battleship", 5),
                new ConfigShip("Submarine", 1)
        };

        Config config = new Config(ships, 10, true, true);

        // Act
        String output = Config.writeConfigXml(config).toString();
        ByteArrayInputStream input = new ByteArrayInputStream(output.getBytes());
        Config deSerializedConfig = Config.readConfigXml(input);

        // Assert
        assertTrue(deSerializedConfig.isAllowDiagonalPlacement());
        assertTrue(deSerializedConfig.isFreeTurnAfterHit());
        assertArrayEquals(ships, deSerializedConfig.getShips());

    }

}