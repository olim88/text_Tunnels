package org.olim.text_tunnels;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import org.olim.text_tunnels.config.configManager;
import org.olim.text_tunnels.config.configs.serverConfig;
import org.slf4j.Logger;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Text_tunnels implements ClientModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitializeClient() {
        //load config
        configManager.init();
        System.out.println("Text Tunnels Mod Initialized!");
        getServerList();
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
        Map<String,String> usersSevers = new HashMap<>();
        for (int i = 0; i < serverList.size(); i++) {
            ServerInfo serverInfo = serverList.get(i);
            usersSevers.put(serverInfo.address,serverInfo.name);
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
        if (!usersSevers.isEmpty()){
            LOGGER.info("[TextTunnels] saved new servers to config");
            configManager.save();
        }
    }
}