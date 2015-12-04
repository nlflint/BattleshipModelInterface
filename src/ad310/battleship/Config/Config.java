package ad310.battleship.Config;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by NathanF on 12/4/2015.
 */
public class Config {
    public final ConfigShip[] Ships;
    public final int BoardLength;
    public final boolean AllowDiagonalPlacement;
    public final boolean FreeTurnAfterHit;

    public Config(ConfigShip[] ships, int boardLength, boolean allowDiagonalPlacement, boolean freeTurnAfterHit) {
        Ships = ships;
        BoardLength = boardLength;
        AllowDiagonalPlacement = allowDiagonalPlacement;
        FreeTurnAfterHit = freeTurnAfterHit;
    }

    public static Config readConfigXml(InputStream input) {
        return null;
    }

    public static Config writeConfigXml(OutputStream output) {
        return null;
    }
}
