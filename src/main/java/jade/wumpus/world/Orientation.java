package jade.wumpus.world;

public enum Orientation {
    NORTH("North"),
    WEST("West"),
    SOUTH("South"),
    EAST("East");

    private final String symbol;

    Orientation(String sym) {
        symbol = sym;
    }

    public String getSymbol() {
        return symbol;
    }
}
