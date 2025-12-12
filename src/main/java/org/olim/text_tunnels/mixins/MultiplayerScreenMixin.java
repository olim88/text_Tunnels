package org.olim.text_tunnels.mixins;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import org.olim.text_tunnels.ManageServerConfigs;
import org.olim.text_tunnels.Text_tunnels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin {

    @Inject(method = "connect(Lnet/minecraft/client/network/ServerInfo;)V",at=@At("HEAD"))
    public void textTunnels$connect(ServerInfo serverInfo, CallbackInfo ci) {
        //make sure server is in config
        ManageServerConfigs.updateSeverList();

        // Get the server's IP address from the connection;
        Text_tunnels.loadForServer(serverInfo.address);
    }
}
