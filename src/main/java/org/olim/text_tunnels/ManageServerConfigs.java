package org.olim.text_tunnels;

import com.mojang.logging.LogUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import org.olim.text_tunnels.config.ConfigManager;
import org.olim.text_tunnels.config.configs.serverConfig;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageServerConfigs {
    private static final Logger LOGGER = LogUtils.getLogger();


    private static final Map<String, List<serverConfig.TunnelConfig>> DEFAULT_CONFIGS = Map.ofEntries(
            Map.entry("mc.hypixel.net", List.of(
                    new serverConfig.TunnelConfig("Guild", "Guild", "/guild"),
                    new serverConfig.TunnelConfig("Party", "Party", "/pc")
            ))
    );
    private static final List<serverConfig.TunnelConfig> my = List.of(new serverConfig.TunnelConfig("Guild", "Guild", "/guild"));

    protected static void updateSeverList() { //todo need to make sure config exists
        // Get the current Minecraft client instance
        MinecraftClient client = MinecraftClient.getInstance();

        // Create a new ServerList instance using the client's options
        ServerList serverList = new ServerList(client);

        // Load the servers from the servers.dat file
        serverList.loadFile();
        LOGGER.info("[TextTunnels] found {} serves", serverList.size());

        //get a map of ips and names of the found servers
        Map<String, String> usersSevers = new HashMap<>();
        for (int i = 0; i < serverList.size(); i++) {
            ServerInfo serverInfo = serverList.get(i);
            usersSevers.put(serverInfo.address, serverInfo.name);
        }

        //update config if there are new servers
        for (serverConfig.ServersConfig existingConfig : ConfigManager.get().serversConfig.serversConfigs) {
            //if config for ip do not need to keep it to add
            usersSevers.remove(existingConfig.ip);
        }

        //any ips left will be new and need to be added to settings
        for (Map.Entry<String, String> server : usersSevers.entrySet()) {
            LOGGER.info("[TextTunnels] found new server with ip: {}", server.getKey());
            serverConfig.ServersConfig newConfig = new serverConfig.ServersConfig();
            newConfig.ip = server.getKey();
            newConfig.name = server.getValue();
            if (DEFAULT_CONFIGS.containsKey(newConfig.ip)) {
                newConfig.tunnelConfigs = DEFAULT_CONFIGS.get(newConfig.ip);
            }
            ConfigManager.get().serversConfig.serversConfigs.add(newConfig);
        }
        if (!usersSevers.isEmpty()) {
            LOGGER.info("[TextTunnels] saved new servers to config");
            ConfigManager.save();
        }
    }
}