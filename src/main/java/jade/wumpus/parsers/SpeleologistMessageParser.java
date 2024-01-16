package jade.wumpus.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpeleologistMessageParser {
    public static boolean isAskingForHelpMessage(String messageContent) {
        String regex = "\\bHelp\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(messageContent);

        if (matcher.find()) {
            String res = matcher.group();
            return res.length() > 0;
        }

        return false;
    }

    public static boolean isGivingInformationMessage(String messageContent) {
        String regex = "\\bGiving\\b";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(messageContent);

        if (matcher.find()) {
            String res = matcher.group();
            return res.length() > 0;
        }

        return false;
    }
}
