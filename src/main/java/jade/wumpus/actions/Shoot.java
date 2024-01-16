package jade.wumpus.actions;

import jade.wumpus.world.Agent;
import jade.wumpus.world.WumpusCave;


public class Shoot implements Action {
    private final WumpusCave cave;

    public Shoot(WumpusCave cave) {
        this.cave = cave;
    }

    @Override
    public ActionResult act() {
        final Agent agent = cave.getAgent();

        if (agent.isHasArrow() && cave.isFacingWumpus(agent.getPosition())) {
            agent.shoot();
            this.cave.shootWumpus();
        }


        return ActionResult.OK;
    }
}
