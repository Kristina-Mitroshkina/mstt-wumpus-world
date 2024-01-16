package jade.wumpus.behaviours.speleologist;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.wumpus.agents.SpeleologistAgent;
import jade.wumpus.configs.WorldConfiguration;

public class ConnectWithWumpusWorld extends Behaviour {

    private boolean isDone = false;

    public ConnectWithWumpusWorld(SpeleologistAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        System.out.println(myAgent.getName() + ": Start finding a Wumpus world");

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(WorldConfiguration.WUMPUS_WORLD_TYPE);
        template.addServices(sd);

        try {
            DFAgentDescription[] result = DFService.search(myAgent, template);

            if (result != null && result.length > 0) {
                final AID wumpusWorldAID = result[0].getName();
                ((SpeleologistAgent) myAgent).connectWithWumpusWorldAgent(wumpusWorldAID);
                myAgent.addBehaviour(new CommunicationWithWumpusWorldAgent((SpeleologistAgent) myAgent));
                isDone = true;
            } else {
                System.out.println(myAgent.getName() + ": no WumpusWorld agents are available.");
                block();
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean done() {
        return isDone;
    }
}
