package org.olim.text_tunnels;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import org.olim.text_tunnels.config.configManager;
import org.olim.text_tunnels.config.configs.serverConfig;
import org.slf4j.Logger;

import java.net.SocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Text_tunnels implements ClientModInitializer {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();
    private static final Logger LOGGER = LogUtils.getLogger();


    private static serverConfig.ServersConfig currentConfig;

    public static void updateTunnel(int index) {
        //finds the regex linked and send to message handler
        if (index != -1) {
            MessageReceiveHandler.updateTunnel(currentConfig.channelConfigs.get(index).recivePrefix);
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
        configManager.init();
        System.out.println("Text Tunnels Mod Initialized!");
        getServerList();
    }

    public static void loadForServer(String serverAddress) {
        //if the server has a config load that config
        for (serverConfig.ServersConfig server : configManager.get().serversConfig.serversConfigs) {
            //if (server.ip.equals(serverAddress)) { todo THIS IS VERY TEMP DEFAULTS TO FIRST SERVER FOR TESTING
            LOGGER.info("[TextTunnels] loaded config for \"{}\"", serverAddress);
            currentConfig = server;
            //get this list off channel names and update
            List<String> names = server.channelConfigs.stream().map(channelConfig -> channelConfig.name).toList();
            List<String> receivePrefixes = server.channelConfigs.stream().map(channelConfig -> channelConfig.recivePrefix).toList();
            List<String> sendPrefixes = server.channelConfigs.stream().map(channelConfig -> channelConfig.sendPrefix).toList();
            ButtonsHandler.load(names);
            MessageReceiveHandler.load(receivePrefixes);
            MessageSendHandler.load(sendPrefixes);
            break;
            //}
        }
        LOGGER.info("[TextTunnels] could not find config for \"{}\"", serverAddress);
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

    public void getServerList() {
        // Get the current Minecraft client instance
        MinecraftClient client = MinecraftClient.getInstance();

        // Create a new ServerList instance using the client's options
        ServerList serverList = new ServerList(client);

        // Load the servers from the servers.dat file
        serverList.loadFile();
        LOGGER.info("[TextTunnels] found {} serves", serverList.size());

        //get a ditnary of ips and names of the found servers
        Map<String, String> usersSevers = new HashMap<>();
        for (int i = 0; i < serverList.size(); i++) {
            ServerInfo serverInfo = serverList.get(i);
            usersSevers.put(serverInfo.address, serverInfo.name);
        }

        //update config if there are new servers
        for (serverConfig.ServersConfig existingConfig : configManager.get().serversConfig.serversConfigs) {
            //if config for ip do not need to keep it to add
            usersSevers.remove(existingConfig.ip);
        }

        //any ips left will be new and need to be added to settings
        for (Map.Entry<String, String> server : usersSevers.entrySet()) {
            LOGGER.info("[TextTunnels] found new server with ip: {}", server.getKey());
            serverConfig.ServersConfig newConfig = new serverConfig.ServersConfig();
            newConfig.ip = server.getKey();
            newConfig.name = server.getValue();
            configManager.get().serversConfig.serversConfigs.add(newConfig);
        }
        if (!usersSevers.isEmpty()) {
            LOGGER.info("[TextTunnels] saved new servers to config");
            configManager.save();
        }
    }

    private static void loadTunnels() {

    }
}