package org.olim.text_tunnels.config.categories;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import org.olim.text_tunnels.config.categories.tunnelManager.ConfigScreen;
import org.olim.text_tunnels.config.configs.ServersConfig;
import org.olim.text_tunnels.config.configs.TextTunnelsConfig;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class serversCategory {

    public static List<ConfigCategory> create(TextTunnelsConfig defaults, TextTunnelsConfig config) {
        //add each server as a category
        List<ServersConfig> allServers = config.serversConfigs;
        List<ConfigCategory> categories = new ArrayList<>(allServers.size());
        for (ServersConfig serverConfig : allServers) {
            ConfigCategory cat = ConfigCategory.createBuilder()
                    .option(LabelOption.create(Component.translatable("text_tunnels.config.serverConfig.ip", serverConfig.ip)))
                    .name(Component.literal(serverConfig.name))
                    .tooltip(Component.translatable("text_tunnels.config.serverConfig.configFor", serverConfig.name))
                    .option(Option.<Boolean>createBuilder()
                            .name(Component.translatable("text_tunnels.config.serverConfig.enabled"))
                            .description(OptionDescription.of(Component.translatable("text_tunnels.config.serverConfig.enabled.@Tooltip")))
                            .binding(true, () -> serverConfig.enabled, newVal -> serverConfig.enabled = newVal)
                            .controller(TickBoxControllerBuilder::create)
                            .build())

                    .option(ButtonOption.createBuilder()
                            .name(Component.translatable("text_tunnels.config.serverConfig.tunnelConfig"))
                            .text(Component.translatable("text_tunnels.config.serverConfig.open"))
                            .action((screen, opt) -> Minecraft.getInstance().setScreenAndShow(new ConfigScreen(screen, serverConfig)))
                            .build())
                    .build();
            categories.add(cat);
        }
        return categories;
    }

}
