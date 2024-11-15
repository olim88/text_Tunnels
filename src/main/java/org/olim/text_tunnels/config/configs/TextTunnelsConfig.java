package org.olim.text_tunnels.config.configs;

import dev.isxander.yacl3.config.v2.api.SerialEntry;

public class TextTunnelsConfig {

    @SerialEntry
    public MainConfig mainConfig = new MainConfig();

    @SerialEntry
    public serverConfig serversConfig = new serverConfig();

}
