package org.olim.text_tunnels.config.configs;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

import java.util.List;

public class ServersConfig {

    @SerialEntry
    public String name;

    @SerialEntry
    public String ip;

    @SerialEntry
    public boolean enabled = true;

    @SerialEntry
    public List<TunnelConfig> tunnelConfigs = List.of(new TunnelConfig());
}
