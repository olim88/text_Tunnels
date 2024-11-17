package org.olim.text_tunnels.config.categories.tunnelManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.olim.text_tunnels.config.ConfigManager;
import org.olim.text_tunnels.config.configs.serverConfig;

import java.awt.*;
import java.util.List;

public class coinfgListWidget extends ElementListWidget<coinfgListWidget.AbstractEntry> {
    private final ConfigScreen screen;
    private final List<serverConfig.ChannelConfig> allChannels;

    public coinfgListWidget(MinecraftClient minecraftClient, ConfigScreen screen, List<serverConfig.ChannelConfig> allChannels, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
        this.screen = screen;
        this.allChannels = allChannels;

        //add labels
        addEntry(new LabelsEntry());
        //add tunnels
        for (serverConfig.ChannelConfig tunnel : allChannels) {
            addEntry(new TunnelEntry(tunnel));
        }
    }

    protected void addRuleAfterSelected() {
        int newIndex = Math.max(children().indexOf(getSelectedOrNull()), 0);

        allChannels.add(newIndex,new serverConfig.ChannelConfig());
        children().add(newIndex + 1, new TunnelEntry(allChannels.get(newIndex)));
    }

    protected void saveRules() {
        ConfigManager.save();
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 100;
    }

    @Override
    protected int getScrollbarX() {
        return super.getScrollbarX() + 50;
    }

    protected boolean removeEntry(AbstractEntry entry) {
        return super.removeEntry(entry);
    }

    protected static abstract class AbstractEntry extends ElementListWidget.Entry<coinfgListWidget.AbstractEntry> {
    }

    private class LabelsEntry extends AbstractEntry {
        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of();
        }

        @Override
        public List<? extends Element> children() {
            return List.of();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawCenteredTextWithShadow(client.textRenderer, Text.literal("Tunnel Name"), width / 2 - 125, y + 5, 0xFFFFFFFF);
            context.drawCenteredTextWithShadow(client.textRenderer, Text.literal("Tunnel Enabled"), width / 2, y + 5, 0xFFFFFFFF);
            context.drawCenteredTextWithShadow(client.textRenderer, Text.literal("Modify"), width / 2 + 100, y + 5, 0xFFFFFFFF);
        }
    }
    private class TunnelEntry extends AbstractEntry {
        //data
        private final serverConfig.ChannelConfig tunnel;

        private final List<? extends Element> children;

        //widgets
        private final ButtonWidget enabledButton;
        private final ButtonWidget openConfigButton;
        private final ButtonWidget deleteButton;

        //text location
        private final int nameX = width / 2 - 125;
        //saved data
        private double oldScrollAmount = 0;


        public TunnelEntry( serverConfig.ChannelConfig tunnel) {
            this.tunnel = tunnel;

            enabledButton = ButtonWidget.builder(enabledButtonText(), a -> toggleEnabled())
                    .size(50, 20)
                    .position(width / 2 - 25, 5)
                    .build();

            openConfigButton = ButtonWidget.builder(Text.literal("Edit"), a -> {
                        client.setScreen(new TunnelConfigScreen(screen, tunnel));
                    })
                    .size(50, 20)
                    .position(width / 2 + 45, 5)
                    //.tooltip(Tooltip.of(Text.translatable("skyblocker.config.chat.chatRules.screen.editRule.@Tooltip")))todo
                    .build();

            deleteButton = ButtonWidget.builder(Text.translatable("selectServer.delete"), a -> { //todo only part of the button can be clicked
                        oldScrollAmount = getScrollAmount();
                        client.setScreen(new ConfirmScreen(this::deleteEntry, Text.literal("Are you sure you want to delete this tunnel?"), Text.literal("Tunnel "+tunnel.name + "will be lost forever! (A long time!)"), Text.translatable("selectServer.deleteButton"), ScreenTexts.CANCEL));
                    })
                    .size(50, 20)
                    .position(width / 2 + 105, 5)
                    .build();

            children = List.of(enabledButton, openConfigButton, deleteButton);
        }

        private Text enabledButtonText() {
            if (tunnel.enabled) {
                return Text.literal("True").withColor(Color.GREEN.getRGB());
            } else {
                return Text.literal("False").withColor(Color.RED.getRGB());
            }
        }
        private void toggleEnabled() {
            tunnel.enabled = ! tunnel.enabled;
            enabledButton.setMessage(enabledButtonText());
        }

        private void deleteEntry(boolean confirmedAction) {
            if (confirmedAction) {
                //delete this
                allChannels.remove(tunnel);
                removeEntry(this);
            }

            client.setScreen(screen);
            setScrollAmount(oldScrollAmount);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of(new Selectable() {
                @Override
                public SelectionType getType() {
                    return SelectionType.HOVERED;
                }

                @Override
                public void appendNarrations(NarrationMessageBuilder builder) {
                    builder.put(NarrationPart.TITLE, tunnel.name);
                }
            });
        }

        @Override
        public List<? extends Element> children() {
            return children;
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) { //todo get strings form en_us.json
            //widgets
            enabledButton.setY(y);
            enabledButton.render(context, mouseX, mouseY, tickDelta);
            openConfigButton.setY(y);
            openConfigButton.render(context, mouseX, mouseY, tickDelta);
            deleteButton.setY(y);
            deleteButton.render(context, mouseX, mouseY, tickDelta);
            //text
            context.drawCenteredTextWithShadow(client.textRenderer, tunnel.name, nameX, y + 5, 0xFFFFFFFF);
        }

        public boolean isChange() {
            //return (!tunnel.enabled(configManager.chatRuleList.get(chatRuleIndex).getEnabled())); todo
            return false;
        }
    }
}
