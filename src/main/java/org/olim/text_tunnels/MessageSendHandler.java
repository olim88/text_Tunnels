package org.olim.text_tunnels;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageSendHandler {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Pattern GROUP_PATTERN = Pattern.compile("\\$\\d+");
    private static final Int2ObjectMap<Matcher> lastIncomingMatch = new Int2ObjectArrayMap<>();
    private static List<String> sendPrefixes;
    private static int currentIndex;


    public static void load(List<String> channelSendPrefix) {
        sendPrefixes = channelSendPrefix;
        currentIndex = -1;
    }

    public static void clear() {
        if (sendPrefixes != null) {
            sendPrefixes.clear();
            lastIncomingMatch.clear();
        }
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
                        LOGGER.error("[TextTunnels] can not get group {} out of {} groups", index, replacements.groupCount());
                        if (CLIENT.player != null) {
                            CLIENT.player.sendMessage(Text.translatable("text_tunnels.MessageSendHandler.error", index, replacements.groupCount()).formatted(Formatting.RED), false);
                        }

                    } else {
                        prefix = prefix.replace(foundGroup, replacements.group(index));
                        continue;
                    }
                }
                //if there is no data yet to go of just replace with empty string
                else {
                    LOGGER.info("[TextTunnels] Can not replace group with out existing matched message");
                    if (CLIENT.player != null) {
                        CLIENT.player.sendMessage(Text.translatable("text_tunnels.MessageSendHandler.noExistingError").formatted(Formatting.YELLOW), false);
                    }
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
