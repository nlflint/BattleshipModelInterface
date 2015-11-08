/**
 * The programatic interface for the Battleship Model class.
 * This interface supports communication with both the view
 * and controller classes in the Battleship application.
 *
 * @author Flopsy Rabbit
 * @author Mopsy Rabbit
 * @author Cottontail Rabbit
 * @author Cottontail Rabbit
 */
public interface BattleshipModelInterface {

    /**
     * Makes a shot during Play Mode.
     * @param loc The designator for the shot
     * @return The status of the shot. See the status constants
     * @throws IllegalStateException The game is not in Play Mode
     */
    Status markShot(Location loc);
    Boolean placeShip(ShipType ship, Location loc, Direction direction);
    Player whoseTurn();
    Square getSquare(Board board, Location loc);
}

//-----

/**
 * A helper class for location
 */
class Location {
    /**
     * The row
     */
    char row;
    /**
     * The column
     */
    int col;
}

/**
 * Helper enumerated type for return status. The SUNK_XXX
 * values indicate HIT. The current player's turn continues
 * until the return status is MISS.
 */
enum Status {
    // return status, a miss
    MISS,
    // return status, a hit, doesn't sink a ship
    HIT,
    // return status, a hit, sunk destroyer  
    SUNK_DESTROYER,
    // return status, a hit, sunk cruiser
    SUNK_CRUISER,
    // return status, a hit, sunk battleship
    SUNK_BATTLESHIP,
    // return status, a hit, sunk aircraft carrier
    SUNK_AIRCRAFT_CARRIER,
    // return status, location was already played or invalid
    DO_OVER
};

enum ShipType {
    AIRCRACT_CARRIER,
    BATTLESHIP,
    CRUISER,
    DESTROYER
}

enum Direction {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST
}

enum Player {
    PLAYER1,
    PLAYER2
}

enum Board {
    PLAYER1_OFFENSIVE,
    PLAYER1_DEFENSIVE,
    PLAYER2_OFFENSIVE,
    PLAYER2_DEFENSIVE
}

enum Square {
    NOTHING,
    HIT,
    MISS,
    AIRCRAFT_CARRIER,
    BATTLESHIP,
    CRUISER,
    DESTROYER
}