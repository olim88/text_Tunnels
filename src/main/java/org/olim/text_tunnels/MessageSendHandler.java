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

    private static final Map<Integer, Matcher> lastIncomingMatch = new HashMap<>();
    private static List<String> sendPrefixes;
    private static int currentIndex;


    public static void load(List<String> channelSendPrefix) {
        sendPrefixes = channelSendPrefix;
        currentIndex = -1;
    }

    public static void clear() {
        sendPrefixes.clear();
        lastIncomingMatch.clear();
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
            //see if there are patterns in the send string to be replaced with relevant data
            boolean hasData = lastIncomingMatch.containsKey(currentIndex);
            Matcher replacements = lastIncomingMatch.get(currentIndex);
            Matcher groupMatch = GROUP_PATTERN.matcher(prefix);

            while (groupMatch.find()) {
                String foundGroup = groupMatch.group();
                if (hasData) {
                    int index = Integer.parseInt(foundGroup.substring(1));
                    if (index > replacements.groupCount()) {
                        LOGGER.error("[TextTunnels] not enough groups in receive prefix to fill in send prefix");
                        LOGGER.error("[TextTunnels] can not get group {} out of {} groups",index, replacements.groupCount());

                    } else {
                        prefix = prefix.replace(foundGroup, replacements.group(index));
                        continue;
                    }
                }
                //if there is no data yet to go of just replace with empty string
                else {
                    LOGGER.info("[TextTunnels] Can not replace group with out existing matched message");
                }
                prefix = prefix.replace(foundGroup, "");//todo tell the user about this


            }

            return prefix;
        }
        LOGGER.info("[TextTunnels] sender prefix index to out of range ({})", currentIndex);
        return null;
    }

    public static void updateLastMatch(int index, Matcher match) {
        lastIncomingMatch.put(index, match);
    }
}
