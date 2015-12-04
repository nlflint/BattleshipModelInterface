package ad310.battleship.model;

/**
 * Created by NathanF on 12/4/2015.
 */
public class Point {
    public final int X;
    public final int Y;

    public Point(ShipLocation shipLocation) {
        X = shipLocation.Column;
        Y = shipLocation.Row;
    }
}
