package org.olim.text_tunnels.config;

import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageHandler {


    private static Map<String,List<Integer>> tunnels = new HashMap<>();

    public static void init() { //todo take a list of channels and add key pair value for each
        tunnels.putIfAbsent("testKey",new ArrayList<>());
        tunnels.putIfAbsent("newKey",new ArrayList<>());
    }

    public static void addMessage(ChatHudLine message) {

        tunnels.get("testKey").add(message.creationTick());
    }

    public static boolean shouldShow(int addedTime) {
        return tunnels.get("testKey").contains(addedTime);
    }

}
