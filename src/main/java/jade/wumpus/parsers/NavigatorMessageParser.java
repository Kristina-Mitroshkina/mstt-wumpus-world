package jade.wumpus.parsers;

import jade.wumpus.actions.ActionType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NavigatorMessageParser {
    public static boolean isAskingForMoreInformationMessage(String messageContent) {
        String regex = "\\binformation\\b";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(messageContent);

        if (matcher.find()) {
            String res = matcher.group();
            return res.length() > 0;
        }

        return false;
    }

    public static ActionType parseNavigatorMessageProposal(String messageContent) {
        final ActionType[] allActions = ActionType.values();

        for (ActionType action : allActions) {
            String actionMessage = action.getActionMessage();
            Pattern pattern = Pattern.compile("\\b" + actionMessage + "\\b", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(messageContent);

            if (matcher.find()) {
                return action;
            }
        }

        return ActionType.NONE;
    }
}
