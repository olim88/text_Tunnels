package org.olim.text_tunnels.config.configs;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

import java.util.List;

public class serverConfig {
    @SerialEntry
    public List<ServersConfig> serversConfigs = List.of();


    public static class ServersConfig {

        @SerialEntry
        public String name = "temp";

        @SerialEntry
        public String ip = "temp";

        @SerialEntry
        public boolean enabled = true;

        @SerialEntry
        public List<ChannelConfig> channelConfigs = List.of(new ChannelConfig());
    }

    public static class ChannelConfig {
        @SerialEntry
        public String name = "Other";

        @SerialEntry
        public String recivePrefix = "";

        @SerialEntry
        public String sendPrefix = "";
    }
}
