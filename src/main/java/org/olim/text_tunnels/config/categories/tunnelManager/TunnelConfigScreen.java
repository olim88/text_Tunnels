package org.olim.text_tunnels.config.categories.tunnelManager;

import net.minecraft.client.gui.Font;
import org.olim.text_tunnels.config.ConfigManager;
import org.olim.text_tunnels.config.configs.TunnelConfig;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TunnelConfigScreen extends Screen {
    private static final int PADDING = 30;
    private static final int SPACING = 22;

    private static EditBox nameInput;
    private static EditBox recivePrefixInput;
    private static EditBox sendPrefixInput;
    private static Button finishButton;

    //text locations
    private static int nameLabelY;
    private static int reciveLabelY;
    private static int sendLabelY;


    private final Screen parent;
    private final TunnelConfig config;

    protected TunnelConfigScreen(Screen parent, TunnelConfig config) {
        super(Component.translatable("text_tunnels.config.tunnelConfig.name"));

        this.parent = parent;
        this.config = config;
    }

    @Override
    protected void init() {
        super.init();
        if (minecraft == null) return;
        int usableWidth = this.width - PADDING * 2;
        int heightOffset = PADDING;

        //finish
        finishButton = Button.builder(Component.translatable("text_tunnels.config.tunnelConfig.finish"), button -> onClose())
                .width(usableWidth)
                .pos(PADDING, this.height - PADDING - 20)
                .build();

        //name
        nameLabelY = heightOffset;
        heightOffset += SPACING / 2;
        nameInput = new EditBox(minecraft.font, PADDING, heightOffset, usableWidth, 20, Component.nullToEmpty(config.name));
        nameInput.setMaxLength(100);
        nameInput.setValue(config.name);
        heightOffset += SPACING;
        addRenderableWidget(nameInput);

        //receive
        reciveLabelY = heightOffset;
        heightOffset += SPACING / 2;
        recivePrefixInput = new EditBox(minecraft.font, PADDING, heightOffset, usableWidth, 20, Component.nullToEmpty(config.receivePrefix));
        recivePrefixInput.setMaxLength(100);
        recivePrefixInput.setValue(config.receivePrefix);
        isReceivePrefixValid(config.receivePrefix);
        recivePrefixInput.setResponder(TunnelConfigScreen::isReceivePrefixValid);
        heightOffset += SPACING;
        addRenderableWidget(recivePrefixInput);

        //send
        sendLabelY = heightOffset;
        heightOffset += SPACING / 2;
        sendPrefixInput = new EditBox(minecraft.font, PADDING, heightOffset, usableWidth, 20, Component.nullToEmpty(config.sendPrefix));
        sendPrefixInput.setMaxLength(100);
        sendPrefixInput.setValue(config.sendPrefix);
        sendPrefixInput.setTooltip(Tooltip.create(Component.translatable("text_tunnels.config.tunnelConfig.sendPrefix.@Tooltip")));
        addRenderableWidget(sendPrefixInput);


        addRenderableWidget(finishButton);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractRenderState(context, mouseX, mouseY, delta);
        context.centeredText(this.font, this.title, this.width / 2, 16, 0xFFFFFFFF);

        //draw labels
        context.text(this.font, Component.translatable("text_tunnels.config.tunnelConfig.label.name"), PADDING, nameLabelY, 0xFFFFFFFF);
        context.text(this.font, Component.translatable("text_tunnels.config.tunnelConfig.label.receive"), PADDING, reciveLabelY, 0xFFFFFFFF);
        context.text(this.font, Component.translatable("text_tunnels.config.tunnelConfig.label.send"), PADDING, sendLabelY, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
        if (minecraft != null) {
            save();
            minecraft.setScreenAndShow(parent);
        }
    }

    private void save() {
        config.name = nameInput.getValue();
        config.receivePrefix = recivePrefixInput.getValue();
        config.sendPrefix = sendPrefixInput.getValue();
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
            recivePrefixInput.setTooltip(Tooltip.create(Component.translatable("text_tunnels.config.tunnelConfig.recivePrefix.@Tooltip").withStyle(ChatFormatting.GREEN)));
            finishButton.setTooltip(Tooltip.create(Component.translatable("text_tunnels.config.tunnelConfig.finish.@Tooltip.valid")));
            finishButton.active = true;
        } catch (PatternSyntaxException e) {
            //regex is invalid. set tooltip red and disable finish button
            recivePrefixInput.setTooltip(Tooltip.create(Component.translatable("text_tunnels.config.tunnelConfig.recivePrefix.@Tooltip").withStyle(ChatFormatting.RED)));
            finishButton.setTooltip(Tooltip.create(Component.translatable("text_tunnels.config.tunnelConfig.finish.@Tooltip.invalid").withStyle(ChatFormatting.RED)));
            finishButton.active = false;
        }
    }

}
