package ad310.battleship.model;

import ad310.battleship.battleshipModelInterface.Location;

public class ShipLocation {
    public final int Row;
    public final int Column;

    public ShipLocation(Location location) {
        Row = (int) (location.row - 'a');
        Column = location.col - 1;
    }

    public ShipLocation(int row, int column) {
        Row = row;
        Column = column;
    }

    @Override
    public boolean equals(Object object) {
        ShipLocation location = (ShipLocation) object;
        return Row == location.Row && Column == location.Column;
    }
}
