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
        public List<TunnelConfig> tunnelConfigs = List.of(new TunnelConfig());
    }

    public static class TunnelConfig {
        @SerialEntry
        public boolean enabled = true;

        @SerialEntry
        public String name = "New";

        @SerialEntry
        public String receivePrefix = "";

        @SerialEntry
        public String sendPrefix = "";


        public TunnelConfig() {}
        public TunnelConfig(String name, String receivePrefix, String sendPrefix) {
            this.name = name;
            this.receivePrefix = receivePrefix;
            this.sendPrefix = sendPrefix;
        }
    }
}
