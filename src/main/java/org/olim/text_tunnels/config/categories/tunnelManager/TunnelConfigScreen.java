package org.olim.text_tunnels.config.categories.tunnelManager;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.olim.text_tunnels.config.ConfigManager;
import org.olim.text_tunnels.config.configs.serverConfig;

public class TunnelConfigScreen extends Screen {
    private static final int PADDING = 30;
    private static final int SPACING = 22;

    private TextFieldWidget nameInput;
    private TextFieldWidget recivePrefixInput;
    private TextFieldWidget sendPrefixInput;
    private ButtonWidget finishButton;

    //text locations
    private int nameLabelY;
    private int reciveLabelY;
    private int sendLabelY;



    private final Screen parent;
    private final serverConfig.ChannelConfig config;

    protected TunnelConfigScreen(Screen parent, serverConfig.ChannelConfig config) {
        super(Text.literal("Tunnel Config"));

        this.parent = parent;
        this.config = config;
    }

    @Override
    protected void init() {
        super.init();
        if (client == null) return;
        int usableWidth = this.width - PADDING * 2;
        int heightOffset = PADDING;

        //name
        nameLabelY = heightOffset;
        heightOffset += SPACING / 2;
        nameInput = new TextFieldWidget(client.textRenderer, PADDING, heightOffset, usableWidth, 20, Text.of(config.name));
        nameInput.setText(config.name);
        heightOffset += SPACING;
        addDrawableChild(nameInput);

        //receive
        reciveLabelY = heightOffset;
        heightOffset += SPACING / 2;
        recivePrefixInput = new TextFieldWidget(client.textRenderer, PADDING, heightOffset, usableWidth, 20, Text.of(config.receivePrefix));
        recivePrefixInput.setText(config.receivePrefix);
        heightOffset += SPACING;
        addDrawableChild(recivePrefixInput);

        //send
        sendLabelY = heightOffset;
        heightOffset += SPACING / 2;
        sendPrefixInput = new TextFieldWidget(client.textRenderer, PADDING, heightOffset, usableWidth, 20, Text.of(config.sendPrefix));
        sendPrefixInput.setText(config.sendPrefix);
        addDrawableChild(sendPrefixInput);


        //finish
        finishButton = ButtonWidget.builder(Text.literal("Finish"),button -> close())
                .width(usableWidth)
                .position(PADDING, this.height - PADDING - 20)
                .build();
        addDrawableChild(finishButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 16, 0xFFFFFFFF);

        //draw labels
        context.drawTextWithShadow(this.textRenderer, "Tunnel Name:", PADDING, nameLabelY, 0xFFFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Tunnel Receive Prefix:", PADDING, reciveLabelY, 0xFFFFFFFF);
        context.drawTextWithShadow(this.textRenderer, "Tunnel Send Prefix:", PADDING, sendLabelY, 0xFFFFFFFF);
    }

    @Override
    public void close() {
        if (client != null) {
            save();
            client.setScreen(parent);
        }
    }

    private void save() {
        config.name = nameInput.getText();
        config.receivePrefix = recivePrefixInput.getText();
        config.sendPrefix = sendPrefixInput.getText();
        ConfigManager.save();
    }
}