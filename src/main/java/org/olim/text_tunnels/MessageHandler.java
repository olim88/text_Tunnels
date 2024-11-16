package org.olim.text_tunnels;

import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<String,List<Integer>> tunnels = new HashMap<>();
    private static String currentTunnel;

    public static void load(List<String> channelNames) {
        tunnels.clear();
        for (String name : channelNames) {
            tunnels.putIfAbsent(name,new ArrayList<>());
        }
        currentTunnel = channelNames.getFirst();
    }

    protected static void updateTunnel(String newTunnel) {
        if (tunnels.containsKey(newTunnel)) {
            currentTunnel = newTunnel;
            LOGGER.info("[TextTunnels] switching to tunnel \"{}\"", newTunnel);
            return;
        }
        //if trying to set to non existent tunnel set to null
        newTunnel = null;
        LOGGER.info("[TextTunnels] trying to show non existent tunnel \"{}\"", newTunnel);
    }

    public static void addMessage(ChatHudLine message) {
        if (currentTunnel != null && tunnels.containsKey(currentTunnel)) {
            tunnels.get(currentTunnel).add(message.creationTick());
        }

    }

    public static boolean shouldShow(int addedTime) {
        if (tunnels.containsKey(currentTunnel)) {
            return tunnels.get(currentTunnel).contains(addedTime);

        }
        //if somehow key does not exist just show everything
        return true;
    }

}
