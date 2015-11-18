/**
 * The programmatic interface for the Battleship Model class.
 * This interface supports communication with both the view
 * and controller classes in the Battleship application.
 *
 * @author Duri Balat
 * @author Jeremy Duke
 * @author Nathan Flint
 * @author Bradley Baumel
 */
public interface BattleshipModelInterface {
    /**
     * Places a ship with a starting location and a particular orientation (direction)
     * @param player The player who is placing the ship
     * @param ship The ShipType object to place
     * @param start  The Location on the board to place the first square of the ship
     * @param end The Location on the board to place the last square of the ship
     * @return true if ship was placed successfully, false otherwise
     */
    Boolean placeShip(Player player, ShipType ship, Location start, Location end);

    /**
     * Get the size of a ship based on ShipType
     * @param ship the ShipType of interest
     * @return size of the ship
     */
    int numberOfSpacesPerShip(ShipType ship);

    /**
     * Determine if setup has been completed and starts the game if so.
     * If the game is not setup, the game will remain in setup mode.
     * @return true if setup is completed, false otherwise
     */
    Boolean startGame();

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

/**
 * All ship types. Used to place ships.
 */
enum ShipType {
    AIRCRACT_CARRIER,
    BATTLESHIP,
    CRUISER,
    DESTROYER1,
    DESTROYER2
}

/**
 * The players.
 * Identifies whose turn it is, and which player is placing a ship during setup.
 */
enum Player {
    PLAYER1,
    PLAYER2
}

/**
 * Identifies the playing boards. Used when requesting the state game boards.
 */
enum Board {
    PLAYER1_OFFENSIVE,
    PLAYER1_DEFENSIVE,
    PLAYER2_OFFENSIVE,
    PLAYER2_DEFENSIVE
}

/**
 * Represents squares on the playing boards.
 * Used by the view to draw the current state of the board.
 */
enum Square {
    NOTHING,
    HIT,
    MISS,
    AIRCRAFT_CARRIER,
    BATTLESHIP,
    CRUISER,
    DESTROYER1,
    DESTROYER2
}
