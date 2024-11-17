package org.olim.text_tunnels;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ButtonsHandler {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static final List<ButtonWidget> activeButtons = new ArrayList<>();

    public static void load(List<String> buttonNames) {
        activeButtons.clear();
        //add all button
        activeButtons.add(new ButtonWidget.Builder(Text.of("All"), button -> channelUpdate(button, -1)).build());

        //generate new button names and offset them
        for (int i = 0; i < buttonNames.size(); i++) {
            int finalI = i;
            activeButtons.add(new ButtonWidget.Builder(Text.of(buttonNames.get(i)), button -> channelUpdate(button, finalI)).build());
        }
    }

    private static void channelUpdate(ButtonWidget pressedButton, int index) {
        Text_tunnels.updateTunnel(index);
        //set active to focused
        updateFocus(pressedButton);
    }

    public static void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //render all the buttons
        for (ButtonWidget button : activeButtons) {
            button.render(context, mouseX, mouseY, delta);
        }
    }


    public static void updatePositions(int x, int y, int height) {
        int rowOffset = 0;
        for (ButtonWidget button : activeButtons) {
            button.setPosition(x + rowOffset, y - height - 4);
            button.setHeight(height);
            //set the width to needed
            int width = CLIENT.textRenderer.getWidth(button.getMessage()) + 4; //+4 to account for padding
            button.setWidth(width);
            rowOffset += width;
        }
        //focus selected button (assume first is)
        updateFocus(activeButtons.getFirst());
    }

    private static void updateFocus(ButtonWidget active) {
        for (ButtonWidget button : activeButtons) {
            button.setFocused(button == active);
        }

    }

    public static void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (ButtonWidget button : activeButtons) {
            button.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
}
