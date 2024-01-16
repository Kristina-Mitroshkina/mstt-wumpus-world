package jade.wumpus.actions;

import jade.wumpus.world.AgentPosition;
import jade.wumpus.world.Orientation;
import jade.wumpus.world.WumpusCave;

public class TurnRight implements Action {
    private final WumpusCave cave;

    public TurnRight(WumpusCave cave) {
        this.cave = cave;
    }

    @Override
    public ActionResult act() {
        final AgentPosition agentPosition = cave.getAgent().getPosition();
        Orientation orientation = switch (agentPosition.getOrientation()) {
            case NORTH -> Orientation.EAST;
            case SOUTH -> Orientation.WEST;
            case EAST -> Orientation.SOUTH;
            case WEST -> Orientation.NORTH;
        };

        AgentPosition newPosition = new AgentPosition(agentPosition.getPosition(), orientation);
        cave.getAgent().setPosition(newPosition);

        return ActionResult.OK;
    }
}
