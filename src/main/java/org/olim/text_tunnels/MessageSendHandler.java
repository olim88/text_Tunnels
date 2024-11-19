package org.olim.text_tunnels;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageSendHandler {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Pattern GROUP_PATTERN = Pattern.compile("\\$\\d+");

    private static final Map<Integer, Matcher> lastMatches = new HashMap<>();
    private static List<String> sendPrefixes;
    private static int currentIndex;


    public static void load(List<String> channelSendPrefix) {
        sendPrefixes = channelSendPrefix;
        currentIndex = -1;
    }

    public static void clear() {
        sendPrefixes.clear();
        lastMatches.clear();
    }

    public static void updateIndex(int newIndex) {
        currentIndex = newIndex;
    }

    public static String getPrefix() {
        //don't edit message if there is not a selected channel
        if (currentIndex == -1 || sendPrefixes == null) {
            return "";
        }

        if (currentIndex < sendPrefixes.size()) {
            String prefix = sendPrefixes.get(currentIndex);
            //see if there are patterns in the send string to be replaced with relivent data
            boolean hasData = lastMatches.containsKey(currentIndex);
            Matcher groupMatch = GROUP_PATTERN.matcher(prefix);
            //todo make sure there are enough groups in last matches for this to work
            while (groupMatch.find()) {
                String foundGroup = groupMatch.group();
                if (hasData) {
                    int index = Integer.parseInt(foundGroup.substring(1));
                    prefix = prefix.replace(foundGroup, lastMatches.get(currentIndex).group(index));
                    continue;
                }
                //if there is no data yet to go of just replace with empty string
                LOGGER.info("[TextTunnels] Can not replace group with out existing matched message");
                prefix = prefix.replace(foundGroup, "");


            }

            return prefix;
        }
        LOGGER.info("[TextTunnels] sender prefix index to out of range ({})", currentIndex);
        return null;
    }

    public static void updateLastMatch(int index, Matcher match) {
        lastMatches.put(index, match);
    }
}
