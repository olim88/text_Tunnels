package org.olim.text_tunnels;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.olim.text_tunnels.config.ConfigManager;
import org.olim.text_tunnels.config.configs.ServersConfig;
import org.olim.text_tunnels.config.configs.TunnelConfig;
import org.slf4j.Logger;

import java.net.SocketAddress;
import java.util.*;
import java.util.regex.Matcher;

public class Text_tunnels implements ClientModInitializer {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final Logger LOGGER = LogUtils.getLogger();


    private static ServersConfig currentConfig;

    public static void updateTunnel(int index) {
        //finds the regex linked and send to message handler
        MessageReceiveHandler.updateTunnel(index);
        MessageSendHandler.updateIndex(index);
        //make sure scrolling is reset
        CLIENT.inGameHud.getChatHud().scroll(0);
    }

    public static void updateLastMatch(int index, Matcher match) {
        MessageSendHandler.updateLastMatch(index, match);
    }


    @Override
    public void onInitializeClient() {
        //load config
        ConfigManager.init();
        LOGGER.info("[TextTunnels] Text Tunnels Mod Initialized!");

    }

    public static void loadForServer(String serverAddress) {
        if (!ConfigManager.get().mainConfig.enabled) {
            clear();
            return;
        }
        if (serverAddress.contains("/")) {
            serverAddress = Arrays.stream(serverAddress.split("/")).findFirst().get();
        }
        //if the server has a config load that config
        for (ServersConfig server : ConfigManager.get().serversConfigs) {
            //if (server.ip.equals(serverAddress) && server.enabled) { todo remove
                LOGGER.info("[TextTunnels] loaded config for \"{}\"", serverAddress);
                currentConfig = server;
                //clear everything if there are no channels
                if (server.tunnelConfigs.isEmpty()) {
                    LOGGER.info("[TextTunnels] no tunnels available to load");
                    clear();
                    return;
                }
                List<String> names = new ArrayList<>();
                List<String> receivePrefixes = new ArrayList<>();
                List<String> sendPrefixes = new ArrayList<>();

                for (TunnelConfig tunnel : server.tunnelConfigs) {
                    if (!tunnel.enabled) {
                        continue;
                    }
                    names.add(tunnel.name);
                    receivePrefixes.add(tunnel.receivePrefix);
                    sendPrefixes.add(tunnel.sendPrefix);
                }
                //get this list off channel names and update

                ButtonsHandler.load(names);
                MessageReceiveHandler.load(receivePrefixes);
                MessageSendHandler.load(sendPrefixes);
                return;
            //}
        }
        LOGGER.info("[TextTunnels] could not find config for \"{}\"", serverAddress);
    }

    public static void clear() {
        ButtonsHandler.clear();
        MessageReceiveHandler.clear();
        MessageSendHandler.clear();
    }


    public static void configUpdated() {

        //when the config is updated check if the player is on a sever and then reload
        ClientPlayNetworkHandler networkHandler = CLIENT.getNetworkHandler();
        if (networkHandler != null) {
            SocketAddress address = networkHandler.getConnection().getAddress();
            if (address != null) {
                loadForServer(address.toString());
            }
        }
    }


}