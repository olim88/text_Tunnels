package org.olim.text_tunnels.config.categories.tunnelManager;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.olim.text_tunnels.config.ConfigManager;
import org.olim.text_tunnels.config.configs.TunnelConfig;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class TunnelConfigScreen extends Screen {
    private static final int PADDING = 30;
    private static final int SPACING = 22;

    private static TextFieldWidget nameInput;
    private static TextFieldWidget recivePrefixInput;
    private static TextFieldWidget sendPrefixInput;
    private static ButtonWidget finishButton;

    //text locations
    private static int nameLabelY;
    private static int reciveLabelY;
    private static int sendLabelY;


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

        //finish
        finishButton = ButtonWidget.builder(Text.translatable("text_tunnels.config.tunnelConfig.finish"), button -> close())
                .width(usableWidth)
                .position(PADDING, this.height - PADDING - 20)
                .build();

        //name
        nameLabelY = heightOffset;
        heightOffset += SPACING / 2;
        nameInput = new TextFieldWidget(client.textRenderer, PADDING, heightOffset, usableWidth, 20, Text.of(config.name));
        nameInput.setMaxLength(100);
        nameInput.setText(config.name);
        heightOffset += SPACING;
        addDrawableChild(nameInput);

        //receive
        reciveLabelY = heightOffset;
        heightOffset += SPACING / 2;
        recivePrefixInput = new TextFieldWidget(client.textRenderer, PADDING, heightOffset, usableWidth, 20, Text.of(config.receivePrefix));
        recivePrefixInput.setMaxLength(100);
        recivePrefixInput.setText(config.receivePrefix);
        isReceivePrefixValid(config.receivePrefix);
        recivePrefixInput.setChangedListener(TunnelConfigScreen::isReceivePrefixValid);
        heightOffset += SPACING;
        addDrawableChild(recivePrefixInput);

        //send
        sendLabelY = heightOffset;
        heightOffset += SPACING / 2;
        sendPrefixInput = new TextFieldWidget(client.textRenderer, PADDING, heightOffset, usableWidth, 20, Text.of(config.sendPrefix));
        sendPrefixInput.setMaxLength(100);
        sendPrefixInput.setText(config.sendPrefix);
        sendPrefixInput.setTooltip(Tooltip.of(Text.translatable("text_tunnels.config.tunnelConfig.sendPrefix.@Tooltip")));
        addDrawableChild(sendPrefixInput);


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

    /**
     * Checks to make sure a string is valid regex updates tooltip accordingly
     *
     * @param string string to check
     */
    private static void isReceivePrefixValid(String string) {
        try {
            Pattern.compile(string);
            //regex is valid. set tooltip green and enable finish button
            recivePrefixInput.setTooltip(Tooltip.of(Text.translatable("text_tunnels.config.tunnelConfig.recivePrefix.@Tooltip").formatted(Formatting.GREEN)));
            finishButton.setTooltip(Tooltip.of(Text.translatable("text_tunnels.config.tunnelConfig.finish.@Tooltip.valid")));
            finishButton.active = true;
        } catch (PatternSyntaxException e) {
            //regex is invalid. set tooltip red and disable finish button
            recivePrefixInput.setTooltip(Tooltip.of(Text.translatable("text_tunnels.config.tunnelConfig.recivePrefix.@Tooltip").formatted(Formatting.RED)));
            finishButton.setTooltip(Tooltip.of(Text.translatable("text_tunnels.config.tunnelConfig.finish.@Tooltip.invalid").formatted(Formatting.RED)));
            finishButton.active = false;
        }
    }

}
