package org.olim.text_tunnels;

import com.mojang.logging.LogUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageHandler {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<String,List<Integer>> tunnels = new HashMap<>();
    private static String currentTunnel;

    public static void load(List<String> channelReceivePrefix) {
        for (String name : channelReceivePrefix) {
            tunnels.putIfAbsent(name,new ArrayList<>());
        }
        currentTunnel = channelReceivePrefix.getFirst();
    }

    protected static void updateTunnel(String newTunnel) {
        if (tunnels.containsKey(newTunnel)) {
            currentTunnel = newTunnel;
            LOGGER.info("[TextTunnels] switching to tunnel \"{}\"", newTunnel);
            return;
        }
        //if trying to set to non-existent tunnel set to null
        newTunnel = null;
        LOGGER.info("[TextTunnels] trying to show non existent tunnel \"{}\"", newTunnel);
    }

    public static void addMessage(Text message) {
        for (String receivePrefix : tunnels.keySet()) {
            if (message.getString().matches("^"+receivePrefix+".*")) {
                tunnels.get(currentTunnel).add(CLIENT.inGameHud.getTicks());

            }
        }
        //if tunnel can not be found do not add it to tunnel


    }

    public static boolean shouldShow(int addedTime) {
        if (tunnels.containsKey(currentTunnel)) {
            return tunnels.get(currentTunnel).contains(addedTime);

        }
        //if somehow key does not exist just show everything
        return true;
    }

}
