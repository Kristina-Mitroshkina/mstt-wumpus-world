package jade.wumpus.behaviours.speleologist;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wumpus.actions.ActionType;
import jade.wumpus.agents.SpeleologistAgent;
import jade.wumpus.configs.MessagesConfig;
import jade.wumpus.configs.SpeleologistMessages;
import jade.wumpus.parsers.NavigatorMessageParser;

public class CommunicationWithNavigationAgent extends Behaviour {
    private int communicationStep = 0;
    private MessageTemplate mt;
    private ACLMessage reply;

    private final AID navigationAgent;

    public CommunicationWithNavigationAgent(SpeleologistAgent agent, AID navigationAgent) {
        super(agent);
        this.navigationAgent = navigationAgent;
    }

    @Override
    public void action() {
        SpeleologistAgent agent = ((SpeleologistAgent) myAgent);

        switch (communicationStep) {
            case 0 -> {
                // Need help from navigator
                ACLMessage inform = new ACLMessage(ACLMessage.INFORM);
                inform.addReceiver(navigationAgent);
                inform.setContent(SpeleologistMessages.ADVICE_PROPOSAL);
                inform.setConversationId(SpeleologistMessages.NAVIGATOR_CONVERSATION_ID);
                inform.setReplyWith("inform" + System.currentTimeMillis());

                System.out.println(myAgent.getName() + ": " + SpeleologistMessages.ADVICE_PROPOSAL);

                myAgent.send(inform);

                mt = MessageTemplate.and(
                        MessageTemplate.MatchConversationId(SpeleologistMessages.NAVIGATOR_CONVERSATION_ID),
                        MessageTemplate.MatchInReplyTo(inform.getReplyWith()));
                communicationStep++;
            }
            case 1 -> {
                // Waiting for reply from navigator
                reply = myAgent.receive(mt);

                if (reply != null) {
                    if (reply.getPerformative() == ACLMessage.REQUEST) {

                        if (NavigatorMessageParser.isAskingForMoreInformationMessage(reply.getContent())) {
                            // Navigator asking for more info, let's get it from the world agent
                            ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
                            req.addReceiver(agent.getWumpusWorld());
                            req.setContent(MessagesConfig.WORLD_INFORMATION);
                            req.setConversationId(SpeleologistMessages.WUMPUS_WORLD_CONVERSATION_ID);
                            req.setReplyWith("req" + System.currentTimeMillis());

                            myAgent.send(req);
                            mt = MessageTemplate.and(
                                    MessageTemplate.MatchConversationId(SpeleologistMessages.WUMPUS_WORLD_CONVERSATION_ID),
                                    MessageTemplate.MatchInReplyTo(req.getReplyWith()));
                            communicationStep++;
                        } else
                            System.out.println(myAgent.getName() + ": There are no requests from navigator.");
                    }
                } else {
                    block();
                }
            }
            case 2 -> {
                // Waiting for more info from the world agent
                reply = myAgent.receive(mt);

                if (reply != null) {

                    if (reply.getPerformative() == ACLMessage.INFORM) {
                        // Received reply from the world agent with current state perception
                        String rep = reply.getContent();
                        ACLMessage mes = new ACLMessage(ACLMessage.INFORM);
                        mes.addReceiver(navigationAgent);
                        mes.setContent(SpeleologistMessages.INFORMATION_PROPOSAL_SPELEOLOGIST + rep);
                        mes.setConversationId(SpeleologistMessages.NAVIGATOR_CONVERSATION_ID);
                        mes.setReplyWith("mes" + System.currentTimeMillis());

                        System.out.println(myAgent.getName() + ": "
                                + SpeleologistMessages.INFORMATION_PROPOSAL_SPELEOLOGIST + rep);

                        myAgent.send(mes);
                        mt = MessageTemplate.and(
                                MessageTemplate.MatchConversationId(SpeleologistMessages.NAVIGATOR_CONVERSATION_ID),
                                MessageTemplate.MatchInReplyTo(mes.getReplyWith()));
                        communicationStep++;
                    }
                } else
                    block();
            }
            case 3 -> {
                // Waiting for the advice
                reply = myAgent.receive(mt);

                if (reply != null) {
                    if (reply.getPerformative() == ACLMessage.PROPOSE) {
                        String message = reply.getContent();

                        ActionType action = NavigatorMessageParser.parseNavigatorMessageProposal(message);
                        String content = "";

                        switch (action) {
                            case TURN_LEFT -> content = MessagesConfig.SPELEOLOGIST_TURN_LEFT;
                            case TURN_RIGHT -> content = MessagesConfig.SPELEOLOGIST_TURN_RIGHT;
                            case MOVE_FORWARD -> content = MessagesConfig.SPELEOLOGIST_MOVE_FORWARD;
                            case GRAB -> content = MessagesConfig.SPELEOLOGIST_GRAB;
                            case SHOOT -> content = MessagesConfig.SPELEOLOGIST_SHOOT;
                            case CLIMB -> content = MessagesConfig.SPELEOLOGIST_CLIMB;
                        }

                        // Sending info regarding current action
                        ACLMessage prop = new ACLMessage(ACLMessage.PROPOSE);
                        prop.addReceiver(agent.getWumpusWorld());
                        prop.setContent(content);
                        prop.setConversationId(SpeleologistMessages.WUMPUS_WORLD_CONVERSATION_ID);
                        prop.setReplyWith("prop" + System.currentTimeMillis());

                        myAgent.send(prop);
                        mt = MessageTemplate.and(
                                MessageTemplate.MatchConversationId(SpeleologistMessages.WUMPUS_WORLD_CONVERSATION_ID),
                                MessageTemplate.MatchInReplyTo(prop.getReplyWith()));
                        communicationStep++;
                    }
                } else {
                    block();
                }
            }
            case 4 -> {
                // Waiting for the action result from the world agent
                reply = myAgent.receive(mt);

                if (reply != null) {
                    String answer = reply.getContent();

                    switch (answer) {
                        case MessagesConfig.FAIL_MESSAGE -> {
                            System.out.println(myAgent.getName() + ": You failed!");
                            communicationStep++;
                            myAgent.doDelete();
                        }
                        case MessagesConfig.OK_MESSAGE -> {
                            System.out.println(myAgent.getName() + ": Wumpus world returns OK.");
                            communicationStep = 0;
                        }
                        case MessagesConfig.WIN_MESSAGE -> {
                            System.out.println(myAgent.getName() + ": The speleologist won!");
                            communicationStep++;
                            myAgent.doDelete();
                        }
                    }
                } else {
                    block();
                }
            }
        }
    }

    @Override
    public boolean done() {
        return communicationStep == 5;
    }


}
