package jade.wumpus.behaviours.navigator;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wumpus.agents.NavigatorAgent;
import jade.wumpus.configs.MessagesConfig;
import jade.wumpus.configs.NavigatorMessages;
import jade.wumpus.parsers.SpeleologistMessageParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NavigatorBehaviour extends CyclicBehaviour {
    private int time;
    final List<String> prefaceMessages = new ArrayList<>();

    public NavigatorBehaviour(NavigatorAgent agent) {
        super(agent);
        this.time = 1;

        this.prefaceMessages.add(MessagesConfig.RANDOM_PREFACE_V1);
        this.prefaceMessages.add(MessagesConfig.RANDOM_PREFACE_V2);
        this.prefaceMessages.add(MessagesConfig.RANDOM_PREFACE_V3);
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = myAgent.receive(mt);

        if (msg != null) {
            final String messageContent = msg.getContent();

            if (SpeleologistMessageParser.isAskingForHelpMessage(messageContent)) {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.REQUEST);
                reply.setContent(NavigatorMessages.INFORMATION_PROPOSAL_NAVIGATOR);

                System.out.println(myAgent.getName() + ": " + NavigatorMessages.INFORMATION_PROPOSAL_NAVIGATOR);
                myAgent.send(reply);
            } else if (SpeleologistMessageParser.isGivingInformationMessage(messageContent)) {
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.PROPOSE);

                String advice = getAdvice(messageContent);
                reply.setContent(advice);

                System.out.println(myAgent.getName() + ": " + advice);
                myAgent.send(reply);

            } else
                System.out.println(myAgent.getName() + ": Navigator received unexpected message.");
        } else {
            block();
        }
    }


    private String getAdvice(String content) {
        boolean stench = false;
        boolean breeze = false;
        boolean glitter = false;
        boolean scream = false;

        String advicedAction = MessagesConfig.MESSAGE_REST;

        for (Map.Entry<String, String> entry : NavigatorMessages.KEYWORDS.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            Pattern pattern = Pattern.compile("\\b" + value + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(content);

            if (matcher.find()) {
                switch (key) {
                    case "Stench" -> stench = true;
                    case "Breeze" -> breeze = true;
                    case "Glitter" -> glitter = true;
                    case "Scream" -> scream = true;
                }
            }
        }

        // Do some logic based on received information

        // A bit of random logic
        switch (time) {
            case 1, 3, 5, 8, 10 -> {
                advicedAction = MessagesConfig.MESSAGE_FORWARD;
                time++;
            }
            case 2, 6 -> {
                advicedAction = MessagesConfig.MESSAGE_RIGHT;
                time++;
            }
            case 4, 9 -> {
                advicedAction = MessagesConfig.MESSAGE_LEFT;
                time++;
            }
            case 7 -> {
                advicedAction = MessagesConfig.MESSAGE_SHOOT;
                time++;
            }
            case 11 -> {
                advicedAction = MessagesConfig.MESSAGE_GRAB;
                time++;
            }
            case 12 -> {
                advicedAction = MessagesConfig.MESSAGE_CLIMB;
                time++;
            }
        }


        Random random = new Random();
        int rand = random.nextInt(2) + 1;

        final String randomPreface = prefaceMessages.get(rand);
        StringBuilder builder = new StringBuilder();
        builder.append(randomPreface).append(" ").append(advicedAction);

        final String adviceMessage = builder.toString();
        return adviceMessage;
    }
}
