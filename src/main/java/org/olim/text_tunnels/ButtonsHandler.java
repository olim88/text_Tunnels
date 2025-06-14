package org.olim.text_tunnels;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.olim.text_tunnels.config.ConfigManager;
import org.olim.text_tunnels.config.configs.MainConfig;

import java.util.ArrayList;
import java.util.List;

public class ButtonsHandler {
    private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

    private static final List<ButtonWidget> activeButtons = new ArrayList<>();
    private static final List<Boolean> notificationIndicators = new ArrayList<>();
    private static boolean positionsSet = false;

    public static void load(List<String> buttonNames) {
        positionsSet = false;
        activeButtons.clear();
        //add all button
        activeButtons.add(new ButtonWidget.Builder(Text.of("All"), button -> channelUpdate(button, -1)).build());

        //generate new button names and offset them
        for (int i = 0; i < buttonNames.size(); i++) {
            int finalI = i;
            activeButtons.add(new ButtonWidget.Builder(Text.of(buttonNames.get(i)), button -> channelUpdate(button, finalI)).build());
        }

        //reset notification indicators
        notificationIndicators.clear();
        for (int i = 0; i <= buttonNames.size(); i++) {
            notificationIndicators.add(false);
        }
    }

    public static void clear() {
        activeButtons.clear();
    }

    private static void channelUpdate(ButtonWidget pressedButton, int index) {
        Text_tunnels.updateTunnel(index);
        //set active to focused
        updateFocus(pressedButton);
    }

    public static void render(DrawContext context, int mouseX, int mouseY, float delta) {
        //render all the buttons
        for (ButtonWidget button : activeButtons) {
            if (ConfigManager.get().mainConfig.buttonStyle.fancyStyle) {
                customButtonRender(context, button);
            } else {
                button.render(context, mouseX, mouseY, delta);
            }
        }

        //draw missed messages bubble
        if (!ConfigManager.get().mainConfig.unreadIndicators.enabled) return;
        int scale = ConfigManager.get().mainConfig.unreadIndicators.scale;
        int index = 0;
        for (ButtonWidget button : activeButtons) {
            if (notificationIndicators.get(index)) {
                //disable when channel is detected
                if (button.isFocused()) {
                    notificationIndicators.set(index, false);
                    continue;
                }
                MainConfig.IndicatorStyle style = ConfigManager.get().mainConfig.unreadIndicators.style;
                context.drawTexture(RenderLayer::getGuiTexturedOverlay, style.getIdentifier(), button.getX() + button.getWidth() - (style.size/2) * scale, button.getY() - (4 * scale), 1f, 1f, style.size * scale, style.size * scale, style.size * scale, style.size * scale);
            }
            index++;
        }
    }

    public static void addNotificationIndicator(int index) {
        notificationIndicators.set(index, true);
    }


    public static void updatePositions(int x, int y, int height) {
        if (activeButtons.isEmpty()) {
            return;
        }
        int rowOffset = 0;
        for (ButtonWidget button : activeButtons) {
            button.setPosition(x + rowOffset, y - height - 2 - ConfigManager.get().mainConfig.buttonStyle.heightOffset); // take the height of the button the spacing between the bottom and the text input and the config space
            button.setHeight(height);
            //set the width to needed
            int width = CLIENT.textRenderer.getWidth(button.getMessage()) + 4; //+4 to account for padding in the button
            button.setWidth(width);
            rowOffset += width + ConfigManager.get().mainConfig.buttonStyle.spacing; // add with of button and space between them
        }
        if (!positionsSet) {
            //focus selected button (assume first is)
            updateFocus(activeButtons.getFirst());
            positionsSet = true;
        }

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

    public static void customButtonRender(DrawContext context, ButtonWidget button) {
        //expand button height if its focused
        if (button.isFocused()) {
            button.setHeight(button.getHeight() + 4);
            button.setY(button.getY() - 4);
        }

        context.fill(RenderLayer.getGui(), button.getX(), button.getY(), button.getX() + button.getWidth(), button.getY() + button.getHeight(), CLIENT.options.getTextBackgroundColor(Integer.MIN_VALUE));

        int i = button.active ? 16777215 : 10526880;
        button.drawMessage(context, CLIENT.textRenderer, i | MathHelper.ceil(255.0F) << 24);
        // reset button height
        if (button.isFocused()) {
            button.setHeight(button.getHeight() - 4);
            button.setY(button.getY() + 4);
        }
    }
}
