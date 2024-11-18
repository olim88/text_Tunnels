package org.olim.text_tunnels.config.configs;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

import java.util.List;

public class TextTunnelsConfig {

    @SerialEntry
    public MainConfig mainConfig = new MainConfig();

    @SerialEntry
    public List<ServersConfig> serversConfigs;


}
