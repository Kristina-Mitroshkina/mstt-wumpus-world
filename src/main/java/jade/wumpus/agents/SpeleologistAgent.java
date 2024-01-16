package jade.wumpus.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.wumpus.behaviours.speleologist.ConnectWithWumpusWorld;
import jade.wumpus.configs.WorldConfiguration;

public class SpeleologistAgent extends Agent {
    private final String agentName;
    private AID wumpusWorld;

    public SpeleologistAgent() {
        this.agentName = WorldConfiguration.SPELEOLOGIST_AGENT_NAME;
    }

    @Override
    protected void setup() {
        System.out.println(agentName + ": Agent " + getAID().getName() + " is ready.");

        SpeleologistAgent agent = this;
        addBehaviour(new TickerBehaviour(this, 5000) {
            @Override
            protected void onTick() {
                if (wumpusWorld != null) {
                    stop();
                    return;
                }
                myAgent.addBehaviour(new ConnectWithWumpusWorld(agent));
            }
        });
    }

    public AID getWumpusWorld() {
        return wumpusWorld;
    }

    public void connectWithWumpusWorldAgent(AID wumpusWorld) {
        this.wumpusWorld = wumpusWorld;
    }

    @Override
    protected void takeDown() {
        System.out.println(agentName + ": Agent " + getAID().getName() + " shutting down.");
    }
}
