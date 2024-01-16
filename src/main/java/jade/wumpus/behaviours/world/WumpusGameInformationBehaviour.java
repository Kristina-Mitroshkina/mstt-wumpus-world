package jade.wumpus.behaviours.world;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wumpus.agents.WumpusWorldAgent;
import jade.wumpus.configs.MessagesConfig;
import jade.wumpus.world.Percept;

public class WumpusGameInformationBehaviour extends CyclicBehaviour {

    public WumpusGameInformationBehaviour(WumpusWorldAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage msg = myAgent.receive(mt);
        
        if (msg != null) {
            String message = msg.getContent();

            if (message.equals(MessagesConfig.WORLD_INFORMATION)) {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.INFORM);

                Percept percept = ((WumpusWorldAgent) myAgent).percept();
                String g = percept.perceptionToMessage();
                reply.setContent(g);
                myAgent.send(reply);
            }
        } else {
            block();
        }
    }
}
