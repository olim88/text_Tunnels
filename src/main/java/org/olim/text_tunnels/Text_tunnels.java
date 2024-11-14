package org.olim.text_tunnels;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.Screen;
import org.olim.text_tunnels.config.configManager;

public class Text_tunnels implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        //load config
        configManager.init();
        System.out.println("Text Tunnels Mod Initialized!");
    }
}