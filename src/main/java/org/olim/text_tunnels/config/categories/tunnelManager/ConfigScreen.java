package org.olim.text_tunnels.config.categories.tunnelManager;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.olim.text_tunnels.config.ConfigManager;
import org.olim.text_tunnels.config.configs.serverConfig;

public class ConfigScreen extends Screen {

    private coinfgListWidget TunnelsListWidget;
    private final Screen parent;
    private final serverConfig.ServersConfig config;

    public ConfigScreen(Screen parent, serverConfig.ServersConfig serverConfig) {
        super(Text.literal("Tunnels config for: \""+serverConfig.name+"\""));
        this.parent = parent;
        config = serverConfig;

    }

    @Override
    protected void init() {
        super.init();
        TunnelsListWidget = new coinfgListWidget(client, this, config.tunnelConfigs, width, height - 96, 32, 25);
        addDrawableChild(TunnelsListWidget);

        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginY(2);
        GridWidget.Adder adder = gridWidget.createAdder(3);
        ButtonWidget buttonNew = ButtonWidget.builder(Text.literal("New Tunnel"), button -> TunnelsListWidget.addRuleAfterSelected()).build();
        adder.add(buttonNew);
        ButtonWidget buttonDone = ButtonWidget.builder(ScreenTexts.DONE, button -> {
            TunnelsListWidget.saveRules();
            if (client != null) {
                close();
            }
        }).build();
        adder.add(buttonDone);
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, this.height - 64, this.width, 64);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 16, 0xFFFFFFFF);
    }

    @Override
    public void close() {
        ConfigManager.save();
        if (client != null) {
            this.client.setScreen(parent);
        }

    }
}
