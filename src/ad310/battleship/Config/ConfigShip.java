package ad310.battleship.config;

import java.io.Serializable;

/**
 * Created by NathanF on 12/4/2015.
 */
public class ConfigShip implements Serializable {
    private String name;
    private int length;

    public ConfigShip()
    {

    }

    public ConfigShip(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object other) {
        ConfigShip otherConfigShip = (ConfigShip) other;
        return this.name.equals(otherConfigShip.name)
                && this.length == otherConfigShip.length;
    }
}
