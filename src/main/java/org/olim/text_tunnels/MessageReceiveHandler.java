package org.olim.text_tunnels;

import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageReceiveHandler {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<Integer, List<Integer>> tunnels = new HashMap<>();
    private static List<String> receivePrefixs;
    private static int currentTunnel;

    public static void init() {
        ClientReceiveMessageEvents.ALLOW_GAME.register(MessageReceiveHandler::addMessage);
    }

    private static boolean addMessage(Text message, boolean b) {
        String plainText = Formatting.strip(message.getString());
        for (int index : tunnels.keySet()) {
            Pattern pattern = Pattern.compile("^" + receivePrefixs.get(index)); //todo have this precompiled and makesure impropper regex is hanndelded
            Matcher match = pattern.matcher(plainText);
            if (match.find()) {
                tunnels.get(index).add(CLIENT.inGameHud.getTicks());
                //send match data to message sender for if it needs it to send message
                Text_tunnels.updateLastMatch(index, match);

            }
        }
        //if tunnel can not be found do not add it to tunnel
        return true;
    }


    public static void load(List<String> channelReceivePrefix) {
        MessageReceiveHandler.receivePrefixs = channelReceivePrefix;
        for (int i = 0; i < channelReceivePrefix.size(); i++) {
            tunnels.putIfAbsent(i, new ArrayList<>());
        }
        currentTunnel = -1;
    }

    public static void clear() {
        tunnels.clear();
    }

    protected static void updateTunnel(int newTunnel) {
        if (tunnels.containsKey(newTunnel)) {
            currentTunnel = newTunnel;
            LOGGER.info("[TextTunnels] switching to tunnel \"{}\"", newTunnel);
            return;
        }
        //if trying to set to non-existent tunnel set to -1
        currentTunnel = -1;
        LOGGER.info("[TextTunnels] trying to show non existent tunnel \"{}\"", newTunnel);
    }

    public static boolean shouldShow(int addedTime) {
        if (tunnels.containsKey(currentTunnel)) {
            return tunnels.get(currentTunnel).contains(addedTime);

        }
        //if somehow key does not exist just show everything
        return true;
    }

    public static boolean isFilterInActive() {
        return currentTunnel == -1;
    }
}
