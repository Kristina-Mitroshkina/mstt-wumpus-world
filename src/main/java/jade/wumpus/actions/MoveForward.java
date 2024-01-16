package jade.wumpus.actions;

import jade.wumpus.world.AgentPosition;
import jade.wumpus.world.Position;
import jade.wumpus.world.Room;
import jade.wumpus.world.RoomContent;
import jade.wumpus.world.WumpusCave;

public class MoveForward implements Action {
    private final WumpusCave cave;

    public MoveForward(WumpusCave cave) {
        this.cave = cave;
    }

    @Override
    public ActionResult act() {
        AgentPosition agentPosition = this.cave.getAgent().getPosition();
        int x = agentPosition.getPosition().getX();
        int y = agentPosition.getPosition().getY();

        switch (agentPosition.getOrientation()) {
            case NORTH -> y++;
            case SOUTH -> y--;
            case EAST -> x++;
            case WEST -> x--;
        }

        Room room = this.cave.getRoomAt(new Position(x, y));
        AgentPosition newPosition;

        if (room != null) {
            newPosition = new AgentPosition(new Position(x, y), agentPosition.getOrientation());
        } else {
            newPosition = agentPosition;
        }

        cave.getAgent().setPosition(newPosition);
        final Room newRoom = cave.getRoomAt(newPosition.getPosition());

        if ((newRoom.getContent().equals(RoomContent.WUMPUS) && cave.isWumpusAlive())
                || newRoom.getContent().equals(RoomContent.PIT)) {
            this.cave.getAgent().die();
            return ActionResult.FAILED;
        }

        return ActionResult.OK;
    }
}
