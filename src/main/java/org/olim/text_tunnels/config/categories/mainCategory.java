package org.olim.text_tunnels.config.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
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
                .group(OptionGroup.createBuilder()
                        .name(Text.translatable("text_tunnels.config.main.buttonStyle"))
                        .collapsed(false)
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("text_tunnels.config.main.buttonStyle.spacing"))
                                .description(OptionDescription.of(Text.translatable("text_tunnels.config.main.buttonStyle.spacing.@Tooltip")))
                                .binding(defaults.mainConfig.buttonStyle.spacing,
                                        () -> config.mainConfig.buttonStyle.spacing,
                                        newValue -> config.mainConfig.buttonStyle.spacing = newValue)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 10).step(1))
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("text_tunnels.config.main.buttonStyle.heightOffset"))
                                .description(OptionDescription.of(Text.translatable("text_tunnels.config.buttonStyle.heightOffset.@Tooltip")))
                                .binding(defaults.mainConfig.buttonStyle.heightOffset,
                                        () -> config.mainConfig.buttonStyle.heightOffset,
                                        newValue -> config.mainConfig.buttonStyle.heightOffset = newValue)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(0, 20).step(1))
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("text_tunnels.config.main.buttonStyle.fancyStyle"))
                                .description(OptionDescription.of(Text.translatable("text_tunnels.config.main.buttonStyle.fancyStyle.@Tooltip")))
                                .binding(defaults.mainConfig.buttonStyle.fancyStyle, () -> config.mainConfig.buttonStyle.fancyStyle, newVal -> config.mainConfig.buttonStyle.fancyStyle = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .build())
                .build();
    }
}
