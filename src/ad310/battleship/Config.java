package ad310.battleship;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by baumelbi on 12/6/15.
 */
public class Config {
    public final int BoardDimension;
    public final boolean FreeTurnAfterHit;
    public final boolean DiagonalPlacementAllowed;
    public final HashSet<ShipType> Ships;


    public Config(int boardDimension, boolean freeTurnAfterHit, boolean diagonalPlacementAllowed, HashSet<ShipType> ships) {
        BoardDimension = boardDimension;
        FreeTurnAfterHit = freeTurnAfterHit;
        DiagonalPlacementAllowed = diagonalPlacementAllowed;
        Ships = ships;
    }
    public Config (){
        BoardDimension = 10;
        FreeTurnAfterHit = true;
        DiagonalPlacementAllowed = true;
        Ships = new HashSet<>();
        Ships.add(ShipType.AIRCRAFT_CARRIER);
        Ships.add(ShipType.BATTLESHIP);
        Ships.add(ShipType.CRUISER);
        Ships.add(ShipType.DESTROYER1);
        Ships.add(ShipType.DESTROYER2);

    }

}
