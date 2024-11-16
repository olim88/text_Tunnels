package org.olim.text_tunnels.mixins;


import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import org.olim.text_tunnels.Text_tunnels;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayerNetworkHandlerMixin {

    @Shadow public abstract ClientConnection getConnection();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(MinecraftClient client, ClientConnection clientConnection, ClientConnectionState clientConnectionState, CallbackInfo ci) {
        // Get the server's IP address from the connection
        String serverAddress = getConnection().getAddress().toString();
        Text_tunnels.loadForServer(serverAddress);
    }
}
