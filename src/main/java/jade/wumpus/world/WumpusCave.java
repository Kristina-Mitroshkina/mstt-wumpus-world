package jade.wumpus.world;

import java.util.HashMap;
import java.util.Map;

public class WumpusCave {
    private final Map<Position, Room> map;
    private final Agent agent;
    private boolean wumpusAlive = true;

    public WumpusCave() {

        AgentPosition startPosition = new AgentPosition(new Position(1, 1), Orientation.NORTH);
        this.agent = new Agent(startPosition);

        this.map = new HashMap<>();

        this.map.put(startPosition.getPosition(), new Room(RoomContent.EMTPY));
        this.map.put(new Position(1, 2), new Room(RoomContent.BREEZE));
        this.map.put(new Position(1, 3), new Room(RoomContent.PIT));
        this.map.put(new Position(1, 4), new Room(RoomContent.BREEZE));

        this.map.put(new Position(2, 1), new Room(RoomContent.STENCH));
        this.map.put(new Position(2, 2), new Room(RoomContent.EMTPY));
        this.map.put(new Position(2, 3), new Room(RoomContent.BREEZE));
        this.map.put(new Position(2, 4), new Room(RoomContent.EMTPY));

        this.map.put(new Position(3, 1), new Room(RoomContent.EMTPY));
        this.map.put(new Position(3, 2), new Room(RoomContent.STENCH));
        this.map.put(new Position(3, 3), new Room(RoomContent.WUMPUS));
        this.map.put(new Position(3, 4), new Room(RoomContent.BREEZE));

        this.map.put(new Position(4, 1), new Room(RoomContent.STENCH));
        this.map.put(new Position(4, 2), new Room(RoomContent.EMTPY));
        this.map.put(new Position(4, 3), new Room(RoomContent.GOLD));
        this.map.put(new Position(4, 4), new Room(RoomContent.PIT));
    }

    public Agent getAgent() {
        return agent;
    }

    public Room getRoomAt(Position position) {
        return this.map.get(position);
    }

    private Position getWumpusPosition() {
        for (Map.Entry<Position, Room> mapEntry : this.map.entrySet()) {
            if (mapEntry.getValue().getContent().equals(RoomContent.WUMPUS)) {
                return mapEntry.getKey();
            }
        }

        return null;
    }

    public boolean isFacingWumpus(AgentPosition agentPosition) {
        Position pos = agentPosition.getPosition();
        Position wumpusPosition = getWumpusPosition();

        if (wumpusPosition != null) {
            return switch (agentPosition.getOrientation()) {
                case NORTH -> pos.getX() == wumpusPosition.getX() && pos.getY() < wumpusPosition.getY();
                case SOUTH -> pos.getX() == wumpusPosition.getX() && pos.getY() > wumpusPosition.getY();
                case EAST -> pos.getY() == wumpusPosition.getY() && pos.getX() < wumpusPosition.getX();
                case WEST -> pos.getY() == wumpusPosition.getY() && pos.getX() > wumpusPosition.getX();
            };
        }

        return false;
    }

    public void shootWumpus() {
        this.wumpusAlive = false;
    }

    public boolean isWumpusAlive() {
        return wumpusAlive;
    }
}
