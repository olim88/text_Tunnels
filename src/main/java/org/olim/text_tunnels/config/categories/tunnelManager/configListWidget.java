package org.olim.text_tunnels.config.categories.tunnelManager;

import org.olim.text_tunnels.config.ConfigManager;
import org.olim.text_tunnels.config.configs.TunnelConfig;

import java.awt.*;
import java.util.List;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class configListWidget extends ContainerObjectSelectionList<configListWidget.AbstractEntry> {
    private final ConfigScreen screen;
    private final List<TunnelConfig> allChannels;

    public configListWidget(Minecraft minecraftClient, ConfigScreen screen, List<TunnelConfig> allChannels, int i, int j, int k, int l) {
        super(minecraftClient, i, j, k, l);
        this.screen = screen;
        this.allChannels = allChannels;

        fillOutList();
    }

    /**
     * Fills out the list with the header and then all current tunnels
     */
    private void fillOutList(){
        //add labels
        addEntry(new LabelsEntry());
        //add tunnels
        for (TunnelConfig tunnel : allChannels) {
            addEntry(new TunnelEntry(tunnel));
        }
    }

    protected void addRule() {
        allChannels.add(new TunnelConfig());
        this.addEntry(new TunnelEntry(allChannels.getLast()));
    }

    /**
     * Swaps the order of 2 given indexes. They must be both within r
     * @param oldIndex first index
     * @param newIndex second index
     */
    protected void changeOrder(int oldIndex, int newIndex) throws IndexOutOfBoundsException {
        //update channels
        TunnelConfig temp = allChannels.get(newIndex);
        allChannels.set(newIndex, allChannels.get(oldIndex));
        allChannels.set(oldIndex, temp);
        //update entry's
        this.clearEntries();
        fillOutList();
    }

    protected void saveRules() {
        ConfigManager.save();
    }

    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 200;
    }

    @Override
    protected int scrollBarX() {
        return super.scrollBarX() + 50;
    }

    protected static abstract class AbstractEntry extends ContainerObjectSelectionList.Entry<configListWidget.AbstractEntry> {
    }

    private class LabelsEntry extends AbstractEntry {
        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of();
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of();
        }

        @Override
        public void extractContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.centeredText(minecraft.font, Component.translatable("text_tunnels.config.tunnelConfig.configList.newTunnel"), width / 2 - 125, this.getY() + 5, 0xFFFFFFFF);
            context.centeredText(minecraft.font, Component.translatable("text_tunnels.config.tunnelConfig.configList.tunnelEnabled"), width / 2, this.getY() + 5, 0xFFFFFFFF);
            context.centeredText(minecraft.font, Component.translatable("text_tunnels.config.tunnelConfig.configList.modify"), width / 2 + 100, this.getY() + 5, 0xFFFFFFFF);
            context.centeredText(minecraft.font, Component.translatable("text_tunnels.config.tunnelConfig.configList.reorder"), width / 2 + 185, this.getY() + 5, 0xFFFFFFFF);
        }
    }

    private class TunnelEntry extends AbstractEntry {
        //data
        private final TunnelConfig tunnel;

        private final List<? extends GuiEventListener> children;

        //widgets
        private final Button enabledButton;
        private final Button openConfigButton;
        private final Button deleteButton;
        private final Button moveUpButton;
        private final Button moveDownButton;

        //text location
        private final int nameX = width / 2 - 125;

        public TunnelEntry(TunnelConfig tunnel) {
            this.tunnel = tunnel;

            enabledButton = Button.builder(enabledButtonText(), a -> toggleEnabled())
                    .size(50, 20)
                    .pos(width / 2 - 25, 5)
                    .build();

            openConfigButton = Button.builder(Component.translatable("text_tunnels.config.tunnelConfig.configList.edit"), a -> {
                        minecraft.setScreen(new TunnelConfigScreen(screen, tunnel));
                    })
                    .size(50, 20)
                    .pos(width / 2 + 45, 5)
                    .build();

            deleteButton = Button.builder(Component.translatable("selectServer.delete"), a -> {
                        minecraft.setScreen(new ConfirmScreen(this::deleteEntry, Component.translatable("text_tunnels.config.tunnelConfig.configList.deleteQuestion"), Component.translatable("text_tunnels.config.tunnelConfig.configList.lost", tunnel.name), Component.translatable("selectServer.deleteButton"), CommonComponents.GUI_CANCEL));
                    })
                    .size(50, 20)
                    .pos(width / 2 + 105, 5)
                    .build();
            moveUpButton = Button.builder(Component.literal("↑"), a -> moveUp())
                    .size(20, 20)
                    .pos(width / 2 + 165, 5)
                    .build();
            moveDownButton = Button.builder(Component.literal("↓"), a -> moveDown())
                    .size(20, 20)
                    .pos(width / 2 + 190, 5)
                    .build();

            children = List.of(enabledButton, openConfigButton, deleteButton, moveUpButton, moveDownButton);
        }

        private Component enabledButtonText() {
            if (tunnel.enabled) {
                return Component.translatable("text_tunnels.config.tunnelConfig.configList.true").withColor(Color.GREEN.getRGB());
            } else {
                return Component.translatable("text_tunnels.config.tunnelConfig.configList.false").withColor(Color.RED.getRGB());
            }
        }

        private void toggleEnabled() {
            tunnel.enabled = !tunnel.enabled;
            enabledButton.setMessage(enabledButtonText());


        }

        private void moveUp() {
            int currentIndex = allChannels.indexOf(tunnel);
            if (currentIndex == 0) return;
            changeOrder(currentIndex, currentIndex - 1);
        }

        private void moveDown() {
            int currentIndex = allChannels.indexOf(tunnel);
            //do not change if not possible
            if (currentIndex >= allChannels.size() - 1) return;
            changeOrder(currentIndex, currentIndex + 1);
        }

        private void deleteEntry(boolean confirmedAction) {
            if (confirmedAction) {
                //delete this
                allChannels.remove(tunnel);
                removeEntryFromTop(this);
            }

            minecraft.setScreen(screen);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(new NarratableEntry() {
                @Override
                public NarrationPriority narrationPriority() {
                    return NarrationPriority.HOVERED;
                }

                @Override
                public void updateNarration(NarrationElementOutput builder) {
                    builder.add(NarratedElementType.TITLE, tunnel.name);
                }
            });
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return children;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor context, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            //widgets
            enabledButton.setY(this.getY());
            enabledButton.extractRenderState(context, mouseX, mouseY, tickDelta);
            openConfigButton.setY(this.getY());
            openConfigButton.extractRenderState(context, mouseX, mouseY, tickDelta);
            deleteButton.setY(this.getY());
            deleteButton.extractRenderState(context, mouseX, mouseY, tickDelta);
            moveUpButton.setY(this.getY());
            moveUpButton.extractRenderState(context, mouseX, mouseY, tickDelta);
            moveDownButton.setY(this.getY());
            moveDownButton.extractRenderState(context, mouseX, mouseY, tickDelta);
            //text
            context.centeredText(minecraft.font, tunnel.name, nameX, this.getY() + 5, 0xFFFFFFFF);
        }

    }
}
