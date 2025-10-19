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
import org.olim.text_tunnels.config.configs.TunnelConfig;

import java.awt.*;
import java.util.List;

public class configListWidget extends ElementListWidget<configListWidget.AbstractEntry> {
    private final ConfigScreen screen;
    private final List<TunnelConfig> allChannels;

    public configListWidget(MinecraftClient minecraftClient, ConfigScreen screen, List<TunnelConfig> allChannels, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
        this.screen = screen;
        this.allChannels = allChannels;

        //add labels
        addEntry(new LabelsEntry());
        //add tunnels
        for (TunnelConfig tunnel : allChannels) {
            addEntry(new TunnelEntry(tunnel));
        }
    }

    protected void addRuleAfterSelected() {
        int newIndex = Math.max(children().indexOf(getSelectedOrNull()), 0);

        allChannels.add(newIndex, new TunnelConfig());
        this.addEntry(new TunnelEntry(allChannels.get(newIndex)));
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

    protected static abstract class AbstractEntry extends ElementListWidget.Entry<configListWidget.AbstractEntry> {
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
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawCenteredTextWithShadow(client.textRenderer, Text.translatable("text_tunnels.config.tunnelConfig.configList.newTunnel"), width / 2 - 125, this.getY() + 5, 0xFFFFFFFF);
            context.drawCenteredTextWithShadow(client.textRenderer, Text.translatable("text_tunnels.config.tunnelConfig.configList.tunnelEnabled"), width / 2, this.getY() + 5, 0xFFFFFFFF);
            context.drawCenteredTextWithShadow(client.textRenderer, Text.translatable("ext_tunnels.config.tunnelConfig.configList.modify"), width / 2 + 100, this.getY() + 5, 0xFFFFFFFF);
        }
    }

    private class TunnelEntry extends AbstractEntry {
        //data
        private final TunnelConfig tunnel;

        private final List<? extends Element> children;

        //widgets
        private final ButtonWidget enabledButton;
        private final ButtonWidget openConfigButton;
        private final ButtonWidget deleteButton;

        //text location
        private final int nameX = width / 2 - 125;

        public TunnelEntry(TunnelConfig tunnel) {
            this.tunnel = tunnel;

            enabledButton = ButtonWidget.builder(enabledButtonText(), a -> toggleEnabled())
                    .size(50, 20)
                    .position(width / 2 - 25, 5)
                    .build();

            openConfigButton = ButtonWidget.builder(Text.translatable("text_tunnels.config.tunnelConfig.configList.edit"), a -> {
                        client.setScreen(new TunnelConfigScreen(screen, tunnel));
                    })
                    .size(50, 20)
                    .position(width / 2 + 45, 5)
                    .build();

            deleteButton = ButtonWidget.builder(Text.translatable("selectServer.delete"), a -> {
                        client.setScreen(new ConfirmScreen(this::deleteEntry, Text.translatable("text_tunnels.config.tunnelConfig.configList.deleteQuestion"), Text.translatable("text_tunnels.config.tunnelConfig.configList.lost", tunnel.name), Text.translatable("selectServer.deleteButton"), ScreenTexts.CANCEL));
                    })
                    .size(50, 20)
                    .position(width / 2 + 105, 5)
                    .build();

            children = List.of(enabledButton, openConfigButton, deleteButton);
        }

        private Text enabledButtonText() {
            if (tunnel.enabled) {
                return Text.translatable("text_tunnels.config.tunnelConfig.configList.true").withColor(Color.GREEN.getRGB());
            } else {
                return Text.translatable("text_tunnels.config.tunnelConfig.configList.false").withColor(Color.RED.getRGB());
            }
        }

        private void toggleEnabled() {
            tunnel.enabled = !tunnel.enabled;
            enabledButton.setMessage(enabledButtonText());


        }

        private void deleteEntry(boolean confirmedAction) {
            if (confirmedAction) {
                //delete this
                allChannels.remove(tunnel);
                removeEntry(this);
            }

            client.setScreen(screen);
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
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            //widgets
            enabledButton.setY(this.getY());
            enabledButton.render(context, mouseX, mouseY, tickDelta);
            openConfigButton.setY(this.getY());
            openConfigButton.render(context, mouseX, mouseY, tickDelta);
            deleteButton.setY(this.getY());
            deleteButton.render(context, mouseX, mouseY, tickDelta);
            //text
            context.drawCenteredTextWithShadow(client.textRenderer, tunnel.name, nameX, this.getY() + 5, 0xFFFFFFFF);
        }

    }
}
