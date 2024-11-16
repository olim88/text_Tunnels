package org.olim.text_tunnels.config;

import com.google.gson.FieldNamingPolicy;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.olim.text_tunnels.Text_tunnels;
import org.olim.text_tunnels.config.categories.mainCategory;
import org.olim.text_tunnels.config.categories.serversCategory;
import org.olim.text_tunnels.config.configs.TextTunnelsConfig;

import java.nio.file.Path;

public class configManager {
    private static final Path CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("textTunnels.json");
    private static final ConfigClassHandler<TextTunnelsConfig> HANDLER = ConfigClassHandler.createBuilder(TextTunnelsConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(CONFIG_FILE)
                    .setJson5(false)
                    .appendGsonBuilder(builder -> builder
                            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                            .registerTypeHierarchyAdapter(Identifier.class, new Identifier.Serializer()))
                    .build())
            .build();

    public static TextTunnelsConfig get() {
        return HANDLER.instance();
    }

    public static void init() {
        HANDLER.load();
    }

    public static void save() {
        HANDLER.save();
    }



    public static Screen getConfigScreen(Screen parentScreen) {
        return YetAnotherConfigLib.create(HANDLER, (defaults, config, builder) -> {
            builder.title(Text.literal("Text Tunnels Settings"))
                    .category(mainCategory.create(defaults, config))
                    .categories(serversCategory.create(defaults, config, parentScreen))
                    .save(Text_tunnels::configUpdated)
                    .build();
            return builder;

            

        }).generateScreen(parentScreen);


    }
}
