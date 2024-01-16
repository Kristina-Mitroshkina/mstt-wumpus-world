package jade.wumpus.behaviours.world;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wumpus.actions.ActionResult;
import jade.wumpus.actions.ActionType;
import jade.wumpus.agents.WumpusWorldAgent;
import jade.wumpus.configs.MessagesConfig;

public class WumpusWorldCoreGameLoop extends CyclicBehaviour {

    public WumpusWorldCoreGameLoop(WumpusWorldAgent agent) {
        super(agent);
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPOSE);
        ACLMessage msg = myAgent.receive(mt);

        if (msg != null) {
            final WumpusWorldAgent agent = (WumpusWorldAgent) myAgent;
            String messageContent = msg.getContent();

            boolean sendTerminateMessage = false;
            boolean sendWinMessage = false;

            switch (messageContent) {
                case MessagesConfig.SPELEOLOGIST_TURN_LEFT -> agent.doAction(ActionType.TURN_LEFT);
                case MessagesConfig.SPELEOLOGIST_TURN_RIGHT -> agent.doAction(ActionType.TURN_RIGHT);
                case MessagesConfig.SPELEOLOGIST_MOVE_FORWARD -> {
                    ActionResult moveForwardResult = agent.doAction(ActionType.MOVE_FORWARD);
                    sendTerminateMessage = !agent.isAgentAlive();
                }
                case MessagesConfig.SPELEOLOGIST_GRAB -> agent.doAction(ActionType.GRAB);
                case MessagesConfig.SPELEOLOGIST_SHOOT -> agent.doAction(ActionType.SHOOT);
                case MessagesConfig.SPELEOLOGIST_CLIMB -> {
                    ActionResult climbResult = agent.doAction(ActionType.CLIMB);

                    if (climbResult.isSuccessful())
                        sendWinMessage = true;
                    else
                        sendTerminateMessage = true;
                }
                default -> System.out.println(myAgent.getName() + ": There is no action!");
            }

            ACLMessage reply = msg.createReply();
            reply.setPerformative(ACLMessage.INFORM);

            //noinspection ConstantValue
            if (sendWinMessage) {
                reset();
                reply.setContent(MessagesConfig.WIN_MESSAGE);
                myAgent.send(reply);
                return;
            }

            if (!sendTerminateMessage) {
                reply.setContent(MessagesConfig.OK_MESSAGE);
                myAgent.send(reply);
            } else {
                reset();
                reply.setContent(MessagesConfig.FAIL_MESSAGE);
                myAgent.send(reply);
            }
        } else {
            block();
        }
    }
}
