package org.olim.text_tunnels.config.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumDropdownControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;
import org.olim.text_tunnels.ButtonsHandler;
import org.olim.text_tunnels.config.configs.MainConfig;
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
                .option(Option.<Boolean>createBuilder()
                        .name(Text.translatable("text_tunnels.config.main.peakingEnabled"))
                        .description(OptionDescription.of(Text.translatable("text_tunnels.config.main.peakingEnabled.@Tooltip")))
                        .binding(defaults.mainConfig.peakingEnabled, () -> config.mainConfig.peakingEnabled, newVal -> config.mainConfig.peakingEnabled = newVal)
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .group(OptionGroup.createBuilder()
                        .name(Text.translatable("text_tunnels.config.main.buttonStyle"))
                        .collapsed(true)
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
                                .description(OptionDescription.of(Text.translatable("text_tunnels.config.main.buttonStyle.heightOffset.@Tooltip")))
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
                .group(OptionGroup.createBuilder()
                        .name(Text.translatable("text_tunnels.config.main.unreadIndicators"))
                        .collapsed(true)
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("text_tunnels.config.main.unreadIndicators.enabled"))
                                .description(OptionDescription.of(Text.translatable("text_tunnels.config.main.unreadIndicators.enabled.@Tooltip")))
                                .binding(defaults.mainConfig.unreadIndicators.enabled, () -> config.mainConfig.unreadIndicators.enabled, newVal -> config.mainConfig.unreadIndicators.enabled = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("text_tunnels.config.main.unreadIndicators.scale"))
                                .description(OptionDescription.of(Text.translatable("text_tunnels.config.main.unreadIndicators.scale.@Tooltip")))
                                .binding(defaults.mainConfig.unreadIndicators.scale,
                                        () -> config.mainConfig.unreadIndicators.scale,
                                        newValue -> config.mainConfig.unreadIndicators.scale = newValue)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(1, 3).step(1))
                                .build())
                        .option(Option.<MainConfig.IndicatorStyle>createBuilder()
                                .name(Text.translatable("text_tunnels.config.main.unreadIndicators.style"))
                                .description(OptionDescription.of(Text.translatable("text_tunnels.config.main.unreadIndicators.style.@Tooltip")))
                                .binding(defaults.mainConfig.unreadIndicators.style,
                                        () -> config.mainConfig.unreadIndicators.style,
                                        newValue -> config.mainConfig.unreadIndicators.style = newValue)
                                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(MainConfig.IndicatorStyle.class))
                                .build())
                        .build())
                .build();
    }
}
