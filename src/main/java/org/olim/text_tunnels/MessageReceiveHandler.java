package org.olim.text_tunnels;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.olim.text_tunnels.config.ConfigManager;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MessageReceiveHandler {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Int2ObjectMap<HashSet<Integer>> tunnels = new Int2ObjectArrayMap<>();
    private static List<Pattern> receivePrefixes;
    private static final List<Integer> peaking = new ArrayList<>();
    private static int currentTunnel;

    public static void init() {
        ClientReceiveMessageEvents.ALLOW_GAME.register(MessageReceiveHandler::addMessage);
    }


    public static boolean load(List<String> channelReceivePrefix) {
        try {
            receivePrefixes = channelReceivePrefix.stream().map(receivePrefix -> Pattern.compile("^" + receivePrefix)).toList();
        } catch (PatternSyntaxException e) {
            LOGGER.error("[TextTunnels] invalid receive prefix: {}", e.getMessage());
            if (CLIENT.player != null) {
                CLIENT.player.sendMessage(Text.translatable("text_tunnels.messageReceiveHandler.error", e.getPattern()).formatted(Formatting.RED), false);
            }
            return false;
        }
        tunnels.clear();
        for (int i = 0; i < channelReceivePrefix.size(); i++) {
            tunnels.put(i, new HashSet<>());
        }
        currentTunnel = -1;
        return true;
    }

    private static boolean addMessage(Text message, boolean overlay) {
        if (overlay) return true;
        String plainText = Formatting.strip(message.getString());
        boolean modMessage = plainText.startsWith("[TextTunnels]");
        for (int index : tunnels.keySet()) {
            Pattern pattern = receivePrefixes.get(index);
            Matcher match = pattern.matcher(plainText);
            if (match.find() || modMessage) {
                tunnels.get(index).add(CLIENT.inGameHud.getTicks());
                ButtonsHandler.addNotificationIndicator(index + 1);
                //send match data to message sender for if it needs it to send message
                if (!modMessage) {
                    Text_tunnels.updateLastMatch(index, match);
                }
            }
        }
        ButtonsHandler.addNotificationIndicator(0);
        //if tunnel can not be found do not add it to tunnel
        return true;
    }

    public static void clear() {
        tunnels.clear();
        peaking.clear();
    }

    protected static void updateTunnel(int newTunnel) {
        peaking.clear();
        if (tunnels.containsKey(newTunnel)) {
            currentTunnel = newTunnel;
            LOGGER.info("[TextTunnels] switching to tunnel \"{}\"", newTunnel);
            return;
        }
        //if trying to set to non-existent tunnel set to -1
        currentTunnel = -1;
        LOGGER.info("[TextTunnels] trying to show non existent tunnel \"{}\"", newTunnel);
    }

    /**
     * Enable peaking for given chanel index
     *
     * @param newPeaking channel to peak
     */
    protected static void updatePeaking(int newPeaking) {
        if (newPeaking == currentTunnel || !ConfigManager.get().mainConfig.peakingEnabled || newPeaking == -1 || currentTunnel == -1) return;
        if (peaking.contains(newPeaking)) {
            peaking.removeIf(i -> i == newPeaking);
            LOGGER.info("[TextTunnels] stop peaking \"{}\"", newPeaking);
        } else {
            peaking.add(newPeaking);
            LOGGER.info("[TextTunnels] peaking \"{}\"", newPeaking);
        }
    }

    protected static boolean isPeaking(int channelIndex) {
        return peaking.contains(channelIndex);
    }

    public static boolean shouldShow(int addedTime) {
        if (tunnels.containsKey(currentTunnel)) {
            if (tunnels.get(currentTunnel).contains(addedTime)) {
                return true;
            }
            //check peaking as well
            if (!ConfigManager.get().mainConfig.peakingEnabled) return false;
            for (int tunnel : peaking) {
                if (tunnels.get(tunnel).contains(addedTime)) {
                    return true;
                }
            }
            return false;

        }
        //if somehow key does not exist just show everything
        return true;
    }

    public static boolean isFilterInActive() {
        return currentTunnel == -1;
    }
}
