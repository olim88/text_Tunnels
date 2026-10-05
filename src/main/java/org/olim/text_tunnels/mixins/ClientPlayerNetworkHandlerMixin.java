package org.olim.text_tunnels.mixins;


import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.olim.text_tunnels.MessageSendHandler;
import org.olim.text_tunnels.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayerNetworkHandlerMixin {

    @Shadow
    public abstract void sendChatCommand(String command);

    @Inject(method = "sendChatMessage", at = @At(value = "HEAD"), cancellable = true)
    private void textTunnels$sendMessage(String content, CallbackInfo ci) {
        //do not try to edit commands;
        if (content.startsWith("/") || !ConfigManager.get().mainConfig.enabled) {
            return;
        }
        String newPrefix = MessageSendHandler.getPrefix();
        //make sure there is a prefix then send a command
        if (newPrefix != null && !newPrefix.isBlank()) {
            this.sendChatCommand(MessageSendHandler.getPrefix() + content);
            ci.cancel();
        }
    }
}
