package ad310.battleship.battleshipModelInterface;

/**
 * Represents squares on the playing boards.
 * Used by the view to draw the current state of the board.
 */
public enum Square {
    NOTHING,
    HIT,
    MISS,
    AIRCRAFT_CARRIER,
    BATTLESHIP,
    CRUISER,
    DESTROYER1,
    DESTROYER2
}
