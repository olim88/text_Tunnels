package org.olim.text_tunnels.config.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;
import org.olim.text_tunnels.config.configs.TextTunnelsConfig;
import org.olim.text_tunnels.config.configs.serverConfig;

import java.util.ArrayList;
import java.util.List;

public class serversCategory {

    public static List<ConfigCategory> create(TextTunnelsConfig defaults, TextTunnelsConfig config) {
        //add each server as a category
        List<serverConfig.ServersConfig> allServers = config.serversConfig.serversConfigs;
        List<ConfigCategory> categories = new ArrayList<>(allServers.size());
        for (serverConfig.ServersConfig serverConfig : allServers) {
            ConfigCategory cat = ConfigCategory.createBuilder()
                    .name(Text.literal(serverConfig.name))
                    .tooltip(Text.literal("config for " + serverConfig.name))
                    .option(Option.<Boolean>createBuilder()
                            .name(Text.literal("Enable"))
                            .description(OptionDescription.of(Text.literal("Toggle Text Tunnels for this server")))
                            .binding(true, () -> serverConfig.enabled, newVal -> serverConfig.enabled = newVal)
                            .controller(TickBoxControllerBuilder::create)
                            .build())
                    .option(Option.<String>createBuilder()
                            .name(Text.literal("Server Name"))
                            .binding(serverConfig.name,
                                    () -> serverConfig.name,
                                    newValue -> serverConfig.name = newValue)
                            .controller(StringControllerBuilder::create)
                            .build())
                    .option(Option.<String>createBuilder()
                            .name(Text.literal("Server Ip"))
                            .binding(serverConfig.ip,
                                    () -> serverConfig.ip,
                                    newValue -> serverConfig.ip = newValue)
                            .controller(StringControllerBuilder::create)
                            .build())

                    .groups(createGroups(serverConfig.channelConfigs))
                    .build();
            categories.add(cat);
        }
        return categories;
    }

    private static List<OptionGroup> createGroups(List<serverConfig.ChannelConfig> allChannels) {
        List<OptionGroup> groups = new ArrayList<>(allChannels.size());
        for (org.olim.text_tunnels.config.configs.serverConfig.ChannelConfig channel : allChannels) {
            OptionGroup group = (OptionGroup.createBuilder()
                    .name(Text.translatable("Tunnel Rule: " + channel.name))
                    .collapsed(false)
                    .option(Option.<String>createBuilder()
                            .name(Text.literal("Name"))
                            .binding(channel.name,
                                    () -> channel.name,
                                    newValue -> channel.name = newValue)
                            .controller(StringControllerBuilder::create)
                            .build())
                    .option(Option.<String>createBuilder()
                            .name(Text.literal("Receive Prefix"))
                            .binding(channel.recivePrefix,
                                    () -> channel.recivePrefix,
                                    newValue -> channel.recivePrefix = newValue)
                            .controller(StringControllerBuilder::create)
                            .build())
                    .option(Option.<String>createBuilder()
                            .name(Text.literal("Send Prefix"))
                            .binding(channel.sendPrefix,
                                    () -> channel.sendPrefix,
                                    newValue -> channel.sendPrefix = newValue)
                            .controller(StringControllerBuilder::create)
                            .build())

                    .build());
            groups.add(group);

        }
        return groups;
    }

}
