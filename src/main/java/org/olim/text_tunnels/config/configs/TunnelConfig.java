package org.olim.text_tunnels.config.configs;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class TunnelConfig {
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
