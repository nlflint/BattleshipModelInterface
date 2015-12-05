package ad310.battleship.battleshipModelInterface;

/**
 * All ship types. Used to place ships.
 */
public enum ShipType {
    AIRCRAFT_CARRIER,
    BATTLESHIP,
    CRUISER,
    DESTROYER1,
    DESTROYER2;

    public static boolean isMember(String givenShipType) {
        ShipType[] shipTypes = ShipType.values();
        for (ShipType shipType : shipTypes)
            if (shipType.name().equals(givenShipType))
                return true;
        return false;
    }
}
