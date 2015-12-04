package ad310.battleship.battleshipModelInterface;

/**
 * The programmatic interface for the Battleship model class.
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
    boolean isGameOver();

    /**
     * Get winner of game
     * @return Winning Player
     * @throws IllegalStateException if !isGameOver()
     */
    Player getWinner() throws IllegalStateException;
}














