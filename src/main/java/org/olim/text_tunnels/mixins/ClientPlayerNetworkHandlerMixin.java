package org.olim.text_tunnels.mixins;


import net.minecraft.client.multiplayer.ClientPacketListener;
import org.olim.text_tunnels.MessageSendHandler;
import org.olim.text_tunnels.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPlayerNetworkHandlerMixin {

    @Shadow
    public abstract void sendCommand(String command);

    @Inject(method = "sendChat", at = @At(value = "HEAD"), cancellable = true)
    private void textTunnels$sendMessage(String content, CallbackInfo ci) {
        //do not try to edit commands;
        if (content.startsWith("/") || !ConfigManager.get().mainConfig.enabled) {
            return;
        }
        String newPrefix = MessageSendHandler.getPrefix();
        //make sure there is a prefix then send a command
        if (newPrefix != null && !newPrefix.isBlank()) {
            this.sendCommand(MessageSendHandler.getPrefix() + content);
            ci.cancel();
        }
    }
}
