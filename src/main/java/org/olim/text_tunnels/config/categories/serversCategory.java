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
                    .option(LabelOption.create(Text.translatable("text_tunnels.config.serverConfig.ip",serverConfig.ip)))
                    .name(Text.literal(serverConfig.name))
                    .tooltip(Text.translatable("text_tunnels.config.serverConfig.configFor", serverConfig.name))
                    .option(Option.<Boolean>createBuilder()
                            .name(Text.translatable("text_tunnels.config.serverConfig.enabled"))
                            .description(OptionDescription.of(Text.translatable("text_tunnels.config.serverConfig.enabled.@Tooltip")))
                            .binding(true, () -> serverConfig.enabled, newVal -> serverConfig.enabled = newVal)
                            .controller(TickBoxControllerBuilder::create)
                            .build())

                    .option(ButtonOption.createBuilder()
                            .name(Text.translatable("text_tunnels.config.serverConfig.tunnelConfig"))
                            .text(Text.translatable("text_tunnels.config.serverConfig.open"))
                            .action((screen, opt) -> MinecraftClient.getInstance().setScreen(new ConfigScreen(screen, serverConfig)))
                            .build())
                    .build();
            categories.add(cat);
        }
        return categories;
    }

}
