package ad310.battleship.config;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

/**
 * Created by NathanF on 12/4/2015.
 */
public class Config implements Serializable {
    private ConfigShip[] ships;
    private int boardLength;
    private boolean allowDiagonalPlacement;
    private boolean freeTurnAfterHit;

    public int getBoardLength() {
        return boardLength;
    }

    public void setBoardLength(int length) {
        boardLength = length;
    }

    public Config(){

    }

    public Config(ConfigShip[] ships, int boardLength, boolean allowDiagonalPlacement, boolean freeTurnAfterHit) {
        this.ships = ships;
        this.boardLength = boardLength;
        this.allowDiagonalPlacement = allowDiagonalPlacement;
        this.freeTurnAfterHit = freeTurnAfterHit;
    }

    public ConfigShip[] getShips() {
        return ships;
    }

    public void setShips(ConfigShip[] ships) {
        this.ships = ships;
    }

    public boolean isAllowDiagonalPlacement() {
        return allowDiagonalPlacement;
    }

    public void setAllowDiagonalPlacement(boolean allowDiagonalPlacement) {
        this.allowDiagonalPlacement = allowDiagonalPlacement;
    }

    public boolean isFreeTurnAfterHit() {
        return freeTurnAfterHit;
    }

    public void setFreeTurnAfterHit(boolean freeTurnAfterHit) {
        this.freeTurnAfterHit = freeTurnAfterHit;
    }

    public static Config readConfigXml(InputStream input) {
        XMLDecoder decoder = new XMLDecoder(input);
        return (Config) decoder.readObject();
    }

    public void writeConfigXml(OutputStream output) {
        XMLEncoder encoder = new XMLEncoder(output);
        encoder.writeObject(this);
        encoder.flush();
    }
}
