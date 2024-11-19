package org.olim.text_tunnels.config.categories;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.olim.text_tunnels.config.categories.tunnelManager.ConfigScreen;
import org.olim.text_tunnels.config.ConfigManager;
import org.olim.text_tunnels.config.configs.ServersConfig;
import org.olim.text_tunnels.config.configs.TextTunnelsConfig;

import java.util.ArrayList;
import java.util.List;

public class serversCategory {

    public static List<ConfigCategory> create(TextTunnelsConfig defaults, TextTunnelsConfig config) {
        //add each server as a category
        List<ServersConfig> allServers = config.serversConfigs;
        List<ConfigCategory> categories = new ArrayList<>(allServers.size());
        for (ServersConfig serverConfig : allServers) {
            ConfigCategory cat = ConfigCategory.createBuilder()
                    .option(LabelOption.create(Text.literal("Server IP: "+serverConfig.ip)))
                    .name(Text.literal(serverConfig.name))
                    .tooltip(Text.literal("config for " + serverConfig.name))
                    .option(Option.<Boolean>createBuilder()
                            .name(Text.literal("Enable"))
                            .description(OptionDescription.of(Text.literal("Toggle Text Tunnels for this server")))
                            .binding(true, () -> serverConfig.enabled, newVal -> serverConfig.enabled = newVal)
                            .controller(TickBoxControllerBuilder::create)
                            .build())

                    .option(ButtonOption.createBuilder()
                            .name(Text.literal("Tunnels Config"))
                            .text(Text.literal("Open"))
                            .action((screen, opt) -> MinecraftClient.getInstance().setScreen(new ConfigScreen(screen, serverConfig)))
                            .build())
                    .build();
            categories.add(cat);
        }
        return categories;
    }

}
