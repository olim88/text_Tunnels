package org.olim.text_tunnels.config.categories.tunnelManager;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.olim.text_tunnels.config.ConfigManager;
import org.olim.text_tunnels.config.configs.ServersConfig;

public class ConfigScreen extends Screen {

    private configListWidget TunnelsListWidget;
    private final Screen parent;
    private final ServersConfig config;

    public ConfigScreen(Screen parent, ServersConfig serverConfig) {
        super(Component.translatable("text_tunnels.config.tunnelConfig.config.for", serverConfig.name));
        this.parent = parent;
        config = serverConfig;

    }

    @Override
    protected void init() {
        super.init();
        TunnelsListWidget = new configListWidget(minecraft, this, config.tunnelConfigs, width, height - 96, 32, 25);
        addRenderableWidget(TunnelsListWidget);

        GridLayout gridWidget = new GridLayout();
        gridWidget.defaultCellSetting().paddingHorizontal(5).paddingVertical(2);
        GridLayout.RowHelper adder = gridWidget.createRowHelper(3);
        Button buttonNew = Button.builder(Component.translatable("text_tunnels.config.tunnelConfig.config.newTunnel"), button -> TunnelsListWidget.addRule()).build();
        adder.addChild(buttonNew);
        Button buttonDone = Button.builder(CommonComponents.GUI_DONE, button -> {
            TunnelsListWidget.saveRules();
            if (minecraft != null) {
                onClose();
            }
        }).build();
        adder.addChild(buttonDone);
        gridWidget.arrangeElements();
        FrameLayout.centerInRectangle(gridWidget, 0, this.height - 64, this.width, 64);
        gridWidget.visitWidgets(this::addRenderableWidget);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);
        context.centeredText(this.font, this.title, this.width / 2, 16, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
        ConfigManager.save();
        if (minecraft != null) {
            this.minecraft.setScreenAndShow(parent);
        }

    }
}
