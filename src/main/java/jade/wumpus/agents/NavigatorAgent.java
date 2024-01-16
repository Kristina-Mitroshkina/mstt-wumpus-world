package jade.wumpus.agents;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.wumpus.behaviours.navigator.NavigatorBehaviour;
import jade.wumpus.configs.WorldConfiguration;

public class NavigatorAgent extends Agent {

    private final String agentName;

    public NavigatorAgent() {
        this.agentName = WorldConfiguration.NAVIGATOR_AGENT_NAME;
    }

    @Override
    protected void setup() {
        System.out.println(agentName + ": Agent " + getAID().getName() + " is ready.");

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(WorldConfiguration.NAVIGATOR_AGENT_TYPE);
        sd.setName(WorldConfiguration.NAVIGATOR_SERVICE_DESCRIPTION);
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new NavigatorBehaviour(this));
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        System.out.println(agentName + ": Agent " + getAID().getName() + " shutting down.");
    }
}
