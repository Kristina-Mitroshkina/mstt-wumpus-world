package jade.wumpus.world;

public class AgentPosition {
    private final Position position;
    private final Orientation orientation;

    public AgentPosition(Position position, Orientation orientation) {
        this.position = position;
        this.orientation = orientation;
    }

    public Position getPosition() {
        return position;
    }

    public Orientation getOrientation() {
        return orientation;
    }

}

