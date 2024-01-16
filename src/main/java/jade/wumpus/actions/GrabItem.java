package jade.wumpus.actions;

import jade.wumpus.world.AgentPosition;
import jade.wumpus.world.Room;
import jade.wumpus.world.RoomContent;
import jade.wumpus.world.WumpusCave;


public class GrabItem implements Action {
    private final WumpusCave cave;

    public GrabItem(WumpusCave cave) {
        this.cave = cave;
    }

    @Override
    public ActionResult act() {
        AgentPosition agentPosition = this.cave.getAgent().getPosition();

        Room room = this.cave.getRoomAt(agentPosition.getPosition());
        if (room.getContent().equals(RoomContent.GOLD)) {
            this.cave.getAgent().grabGold();
        }

        return ActionResult.OK;
    }
}
