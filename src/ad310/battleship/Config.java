package ad310.battleship;

/**
 * Created by baumelbi on 12/6/15.
 */
public class Config {
    public final int BoardDimension;
    public final boolean FreeTurnAfterHit;

    public Config(int boardDimension, boolean freeTurnAfterHit) {
        BoardDimension = boardDimension;
        FreeTurnAfterHit = freeTurnAfterHit;
    }
    public Config (){
        BoardDimension = 10;
        FreeTurnAfterHit = true;
    }

}
