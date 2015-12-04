package ad310.battleship.model;

import ad310.battleship.battleshipModelInterface.ShipType;

import java.util.ArrayList;

public class Ship {
    ArrayList<ShipLocation> locations;
    ShipType type;
    int hitCount;

    public Ship(ShipType shipType, ArrayList<ShipLocation> locations) {
        type = shipType;
        this.locations = locations;
    }

    public boolean ContainsLocation(ShipLocation location) {
        for (ShipLocation loc : locations) {
            if (loc.equals(location))
                return true;
        }
        return false;
    }

    public boolean ContainsAnyLocations(ArrayList<ShipLocation> locations) {
        for (ShipLocation location : locations)
            if (ContainsLocation(location))
                return true;
        return false;
    }

    public boolean isSunk(){
        switch(type){
            case AIRCRAFT_CARRIER:
                return hitCount>=5;
            case BATTLESHIP:
                return hitCount>=4;
            case CRUISER:
                return hitCount>=3;
            default:
        }
        return hitCount>=2;
    }
    public void hit(){
        hitCount++;
    }
}
