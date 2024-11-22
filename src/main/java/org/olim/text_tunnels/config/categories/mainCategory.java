package org.olim.text_tunnels.config.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;
import org.olim.text_tunnels.config.configs.TextTunnelsConfig;

public class mainCategory {

    public static ConfigCategory create(TextTunnelsConfig defaults, TextTunnelsConfig config) {
        return ConfigCategory.createBuilder()
                .name(Text.translatable("text_tunnels.config.main.name"))
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("text_tunnels.config.main.enabled"))
                        .description(OptionDescription.of(Text.translatable("text_tunnels.config.main.enabled.@Tooltip")))
                        .binding(defaults.mainConfig.enabled, () -> config.mainConfig.enabled, newVal -> config.mainConfig.enabled = newVal)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .build();
    }
}
