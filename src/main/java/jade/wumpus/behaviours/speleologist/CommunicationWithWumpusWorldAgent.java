package jade.wumpus.behaviours.speleologist;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wumpus.agents.SpeleologistAgent;
import jade.wumpus.configs.SpeleologistMessages;

public class CommunicationWithWumpusWorldAgent extends Behaviour {

    private MessageTemplate mt;
    private int step = 0;

    public CommunicationWithWumpusWorldAgent(SpeleologistAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        final SpeleologistAgent agent = (SpeleologistAgent) myAgent;

        switch (step) {
            case 0 -> {
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);

                cfp.addReceiver(agent.getWumpusWorld());
                cfp.setContent(SpeleologistMessages.ENTER_CAVE);
                cfp.setConversationId(SpeleologistMessages.WUMPUS_WORLD_CONVERSATION_ID);
                cfp.setReplyWith("cfp" + System.currentTimeMillis());

                myAgent.send(cfp);
                mt = MessageTemplate.and(
                        MessageTemplate.MatchConversationId(SpeleologistMessages.WUMPUS_WORLD_CONVERSATION_ID),
                        MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                step = 1;
            }
            case 1 -> {
                ACLMessage reply = myAgent.receive(mt);

                if (reply != null) {
                    if (reply.getPerformative() == ACLMessage.CONFIRM) {
                        System.out.println(myAgent.getName() + ": Found a Wumpus World.");
                        super.myAgent.addBehaviour(new TickerBehaviour(super.myAgent, 5000) {
                            @Override
                            protected void onTick() {

                                myAgent.addBehaviour(new ConnectWithNavigator(agent));
                            }
                        });
                        step++;
                    }
                } else {
                    block();
                }
            }
        }
    }

    @Override
    public boolean done() {
        return step == 2;
    }
}
