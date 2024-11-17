package org.olim.text_tunnels;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.List;

public class MessageSendHandler {
    private static final Logger LOGGER = LogUtils.getLogger();



    private static List<String> sendPrefixes;
    private static int currentIndex;


    public static void load(List<String> channelSendPrefix) {
        sendPrefixes = channelSendPrefix;
        currentIndex = -1;
    }

    public static void clear() {
        sendPrefixes.clear();
    }

    public static void updateIndex(int newIndex) {
        currentIndex = newIndex;
    }

    public static String  getPrefix() {
        if (currentIndex == -1) {
            return "";
        }
        if (currentIndex < sendPrefixes.size()) {
            return sendPrefixes.get(currentIndex);
        }
        LOGGER.info("[TextTunnels] sender prefix index to out of range ({})", currentIndex);
        return null;
    }
}
