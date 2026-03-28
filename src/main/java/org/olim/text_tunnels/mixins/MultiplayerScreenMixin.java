package org.olim.text_tunnels.mixins;

import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import org.olim.text_tunnels.ManageServerConfigs;
import org.olim.text_tunnels.Text_tunnels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public class MultiplayerScreenMixin {

    @Inject(method = "join(Lnet/minecraft/client/multiplayer/ServerData;)V",at=@At("HEAD"))
    public void textTunnels$connect(ServerData serverInfo, CallbackInfo ci) {
        //make sure server is in config
        ManageServerConfigs.updateSeverList();

        // Get the server's IP address from the connection;
        Text_tunnels.loadForServer(serverInfo.ip);
    }
}
