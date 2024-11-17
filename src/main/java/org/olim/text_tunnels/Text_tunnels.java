package org.olim.text_tunnels;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.olim.text_tunnels.config.ConfigManager;
import org.olim.text_tunnels.config.configs.serverConfig;
import org.slf4j.Logger;

import java.net.SocketAddress;
import java.util.*;

public class Text_tunnels implements ClientModInitializer {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final Logger LOGGER = LogUtils.getLogger();


    private static serverConfig.ServersConfig currentConfig;

    public static void updateTunnel(int index) {
        //finds the regex linked and send to message handler
        if (index != -1) {
            MessageReceiveHandler.updateTunnel(currentConfig.tunnelConfigs.get(index).receivePrefix);
        } else {
            MessageReceiveHandler.updateTunnel(null);
        }
        MessageSendHandler.updateIndex(index);
        //make sure scrolling is reset
        CLIENT.inGameHud.getChatHud().scroll(0);
    }


    @Override
    public void onInitializeClient() {
        //load config
        ConfigManager.init();
        LOGGER.info("[TextTunnels] Text Tunnels Mod Initialized!");
        ManageServerConfigs.updateSeverList();
    }

    public static void loadForServer(String serverAddress) { //todo this breaks if no config file
        if (!ConfigManager.get().mainConfig.enabled) {
            clear();
            return;
        }
        if (serverAddress.contains("/")) {
            serverAddress = Arrays.stream(serverAddress.split("/")).findFirst().get();
        }
        //if the server has a config load that config
        for (serverConfig.ServersConfig server : ConfigManager.get().serversConfig.serversConfigs) {
            if (server.ip.equals(serverAddress) && server.enabled) {
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

                for (serverConfig.TunnelConfig tunnel : server.tunnelConfigs) {
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
            }
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