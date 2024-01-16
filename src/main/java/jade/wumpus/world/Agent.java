package jade.wumpus.world;

public class Agent {
    private AgentPosition position;
    private boolean hasArrow = true;
    private boolean isAlive = true;
    private boolean goldGrabbed = false;

    public Agent(AgentPosition position) {
        this.position = position;
    }

    public AgentPosition getPosition() {
        return position;
    }

    public void setPosition(AgentPosition position) {
        this.position = position;
    }

    public boolean isHasArrow() {
        return hasArrow;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public boolean isGoldGrabbed() {
        return goldGrabbed;
    }

    public void shoot() {
        this.hasArrow = false;
    }

    public void die() {
        this.isAlive = false;
    }

    public void grabGold() {
        this.goldGrabbed = true;
    }
}
