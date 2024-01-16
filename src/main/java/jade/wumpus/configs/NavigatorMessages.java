package jade.wumpus.configs;

import java.util.LinkedHashMap;
import java.util.Map;

public class NavigatorMessages {
    // Navigator messages
    public static final String INFORMATION_PROPOSAL_NAVIGATOR = "Give me information regarding your current status.";
    public final static Map<String, String> KEYWORDS = new LinkedHashMap<String, String>() {{
        put("Stench", "stinks");
        put("Breeze", "breeze");
        put("Glitter", "shining");
        put("Scream", "hear");
    }};
}
