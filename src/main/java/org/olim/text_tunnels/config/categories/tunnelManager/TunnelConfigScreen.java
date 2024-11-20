package org.olim.text_tunnels.config.categories.tunnelManager;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.olim.text_tunnels.config.ConfigManager;
import org.olim.text_tunnels.config.configs.TunnelConfig;

public class TunnelConfigScreen extends Screen {
    private static final int PADDING = 30;
    private static final int SPACING = 22;

    private TextFieldWidget nameInput;
    private TextFieldWidget recivePrefixInput;
    private TextFieldWidget sendPrefixInput;

    //text locations
    private int nameLabelY;
    private int reciveLabelY;
    private int sendLabelY;



    private final Screen parent;
    private final TunnelConfig config;

    protected TunnelConfigScreen(Screen parent, TunnelConfig config) {
        super(Text.translatable("text_tunnels.config.tunnelConfig.name"));

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
        nameInput.setMaxLength(100);
        heightOffset += SPACING;
        addDrawableChild(nameInput);

        //receive
        reciveLabelY = heightOffset;
        heightOffset += SPACING / 2;
        recivePrefixInput = new TextFieldWidget(client.textRenderer, PADDING, heightOffset, usableWidth, 20, Text.of(config.receivePrefix));
        recivePrefixInput.setText(config.receivePrefix);
        recivePrefixInput.setMaxLength(100);
        heightOffset += SPACING;
        addDrawableChild(recivePrefixInput);

        //send
        sendLabelY = heightOffset;
        heightOffset += SPACING / 2;
        sendPrefixInput = new TextFieldWidget(client.textRenderer, PADDING, heightOffset, usableWidth, 20, Text.of(config.sendPrefix));
        sendPrefixInput.setText(config.sendPrefix);
        sendPrefixInput.setMaxLength(100);
        addDrawableChild(sendPrefixInput);


        //finish
        ButtonWidget finishButton = ButtonWidget.builder(Text.translatable("text_tunnels.config.tunnelConfig.finish"), button -> close())
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
        context.drawTextWithShadow(this.textRenderer, Text.translatable("text_tunnels.config.tunnelConfig.label.name"), PADDING, nameLabelY, 0xFFFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.translatable("text_tunnels.config.tunnelConfig.label.receive"), PADDING, reciveLabelY, 0xFFFFFFFF);
        context.drawTextWithShadow(this.textRenderer, Text.translatable("text_tunnels.config.tunnelConfig.label.send"), PADDING, sendLabelY, 0xFFFFFFFF);
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
