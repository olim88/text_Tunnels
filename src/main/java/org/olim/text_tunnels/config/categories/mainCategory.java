package org.olim.text_tunnels.config.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;
import org.olim.text_tunnels.config.configs.TextTunnelsConfig;

public class mainCategory {

    public static ConfigCategory create (TextTunnelsConfig defaults, TextTunnelsConfig config) {
        return ConfigCategory.createBuilder()
                .name(Text.literal("Main settings"))
                .tooltip(Text.literal("somthing"))
                .group(OptionGroup.createBuilder()
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.literal("Enable Text Tunnels"))
                                .description(OptionDescription.of(Text.literal("Toggle all the feature of this mod on and off")))
                                .binding(defaults.mainConfig.enabled, () -> config.mainConfig.enabled, newVal -> config.mainConfig.enabled = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .name(Text.literal("Name of the group"))
                        .description(OptionDescription.of(Text.literal("This text will appear when you hover over the name or focus on the collapse button with Tab.")))

                        .build())
                .build();
    }
}
