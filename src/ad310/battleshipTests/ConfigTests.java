package ad310.battleshipTests;

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
    public void serialization_WhenConfigIsConvertedToXMLAndBackToObject_ThenPropertiesMatch() {
        // Arrange
        ConfigShip[] ships = new ConfigShip[] {
                new ConfigShip("Battleship", 5),
                new ConfigShip("Submarine", 1)
        };

        Config config = new Config(ships, 10, true, true);

        // Act
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        config.writeConfigXml(output);

        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        Config deSerializedConfig = Config.readConfigXml(input);

        // Assert
        assertTrue(deSerializedConfig.isAllowDiagonalPlacement());
        assertTrue(deSerializedConfig.isFreeTurnAfterHit());
        assertArrayEquals(ships, deSerializedConfig.getShips());

    }

    @Test //Commented out so it doesn't run everytime tests are executed
    public void WriteXML() {
        ConfigShip[] ships = new ConfigShip[] {
                new ConfigShip("Battleship", 5),
                new ConfigShip("Submarine", 1)
        };

        Config config = new Config(ships, 10, true, true);


        try {
            FileOutputStream file = new FileOutputStream("testConfig.xml");
            config.writeConfigXml(file);
        }
        catch (FileNotFoundException ex) {

        }

    }

}