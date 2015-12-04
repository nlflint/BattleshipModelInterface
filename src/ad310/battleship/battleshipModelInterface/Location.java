package ad310.battleship.battleshipModelInterface;

/**
 * A helper class for location
 */
public class Location {
    /**
     * The row
     */
    public final char row;
    /**
     * The column
     */
    public final int col;

    public Location(int col, char row){
        this.row = row;
        this.col = col;
    }
    public String toString() {
        String out = "[" + (char)(row - 32) + ", " + col + "]";
        return out;
    }
}
