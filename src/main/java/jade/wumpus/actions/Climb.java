package jade.wumpus.actions;

import jade.wumpus.world.WumpusCave;

public class Climb implements Action {
    private final WumpusCave cave;

    public Climb(WumpusCave cave) {
        this.cave = cave;
    }

    @Override
    public ActionResult act() {
        return ActionResult.OK;
    }
}
