package ad310.battleship;

/**
 * Created by baumelbi on 12/6/15.
 */
public class Config {
    public final int BoardDimension;
    public final boolean FreeTurnAfterHit;
    public final boolean DiagonalPlacementAllowed;


    public Config(int boardDimension, boolean freeTurnAfterHit, boolean diagonalPlacementAllowed) {
        BoardDimension = boardDimension;
        FreeTurnAfterHit = freeTurnAfterHit;
        DiagonalPlacementAllowed = diagonalPlacementAllowed;
    }
    public Config (){
        BoardDimension = 10;
        FreeTurnAfterHit = true;
        DiagonalPlacementAllowed = true;
    }

}
