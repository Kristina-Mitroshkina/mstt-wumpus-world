package jade.wumpus.behaviours.world;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wumpus.agents.WumpusWorldAgent;
import jade.wumpus.configs.MessagesConfig;
import jade.wumpus.configs.SpeleologistMessages;

import java.util.Objects;

public class WorldToSpeleologistCommunication extends CyclicBehaviour {

    public WorldToSpeleologistCommunication(WumpusWorldAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = myAgent.receive(mt);

        if (msg != null) {
            String message = msg.getContent();

            if (Objects.equals(message, SpeleologistMessages.ENTER_CAVE)) {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.CONFIRM);
                reply.setContent(MessagesConfig.OK_MESSAGE);
                myAgent.send(reply);
            }
        } else {
            block();
        }
    }
}
