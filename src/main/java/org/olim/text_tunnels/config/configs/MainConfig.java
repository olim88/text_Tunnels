package org.olim.text_tunnels.config.configs;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

import java.util.List;

public class MainConfig {
    @SerialEntry
    public boolean enabled = true;

    @SerialEntry
    public ButtonStyle buttonStyle = new ButtonStyle();

    public static class ButtonStyle {
        @SerialEntry
        public int spacing = 0;

        @SerialEntry
        public int heightOffset = 0;

        @SerialEntry
        public boolean fancyStyle = true;
    }


}





