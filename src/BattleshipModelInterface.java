/**
 * The programmatic interface for the Battleship Model class.
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
     * Places a ship with a starting location and a particular orientation (direction)
     * @param ship The ShipType object to place
     * @param loc  The Location on the board to place the first square of the ship
     * @param direction The orientation of the ship in relation to Location
     * @return true if ship was placed successfully, false otherwise
     */
    Boolean placeShip(ShipType ship, Location loc, Direction direction);

    /**
     * Get the size of a ship based on ShipType
     * @param ship the ShipType of interest
     * @return size of the ship
     */
    int numberOfSpacesPerShip(ShipType ship);

    /**
     * Determine if setup has been completed and game can begin
     * @return true if setup is completed, false otherwise
     */
    Boolean gameReady();

    /**
     * Makes a shot during Play Mode.
     * @param loc The designator for the shot
     * @return The status of the shot. See the status constants
     * @throws IllegalStateException The game is not in Play Mode
     */
    Status markShot(Location loc) throws IllegalStateException;

    /**
     * Returns the player whose turn it is
     * @return the player whose turn it is
     */
    Player whoseTurn();

    /**
     * Return the status of the requested square on the board
     * @param board Board object to evaluate
     * @param loc   Location on Board of interest
     * @return status of requested square as an enum type
     */
    Square getSquare(Board board, Location loc);


    //We should probably move board representation to the view
    String printDefBoard(Player player);
    String printOffBoard(Player player);

    //methods for when game is over
    /**
     * Determine if game is in progress or not
     * @return true if game has a winner, false otherwise
     */
    Boolean isGameOver();

    /**
     * Get winner of game
     * @return Winning Player
     * @throws IllegalStateException if !isGameOver()
     */
    Player getWinner() throws IllegalStateException;

    /**
     * Reset the board
     */
    void resetBoard();
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
}

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
