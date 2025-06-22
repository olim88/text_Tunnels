package org.olim.text_tunnels.config.configs;

import dev.isxander.yacl3.config.v2.api.SerialEntry;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.util.Identifier;

public class MainConfig {
    @SerialEntry
    public boolean enabled = true;

    @SerialEntry
    public boolean peakingEnabled = true;

    @SerialEntry
    public ButtonStyle buttonStyle = new ButtonStyle();

    @SerialEntry
    public UnreadIndicators unreadIndicators = new UnreadIndicators();

    public static class ButtonStyle {
        @SerialEntry
        public int spacing = 2;

        @SerialEntry
        public int heightOffset = 0;

        @SerialEntry
        public boolean fancyStyle = true;


    }

    public static class UnreadIndicators {
        @SerialEntry
        public boolean enabled = false;

        @SerialEntry
        public int scale = 1;

        @SerialEntry
        public IndicatorStyle style = IndicatorStyle.FLAME;

    }

    public enum IndicatorStyle {
        FLAME(Identifier.ofVanilla("textures/particle/flame.png"), 2, 8),
        GLINT(Identifier.ofVanilla("textures/particle/glint.png"), 0, 8),
        CRIT(Identifier.ofVanilla("textures/particle/critical_hit.png"), 0, 8),
        SOUL(Identifier.ofVanilla("textures/particle/sculk_soul_8.png"), 4, 16);


        private final Identifier identifier;
        public final int yOffset;
        public final int size;

        IndicatorStyle(Identifier identifier, int yOffset, int size) {
            this.identifier = identifier;
            this.yOffset = yOffset;
            this.size = size;
        }

        public Identifier getIdentifier() {
            return identifier;
        }

        @Override
        public String toString() {
            return I18n.translate("text_tunnels.config.main.unreadIndicators.style.mode." + name());
        }
    }
}





