package jade.wumpus.actions;

public enum ActionType {
    NONE("none"),
    TURN_LEFT("left"),
    TURN_RIGHT("right"),
    MOVE_FORWARD("forward"),
    CLIMB("climb"),
    GRAB("grab"),
    SHOOT("shoot");

    final String actionMessage;

    ActionType(String actionMessage){
        this.actionMessage = actionMessage;
    }

    public String getActionMessage() {
        return actionMessage;
    }
}
