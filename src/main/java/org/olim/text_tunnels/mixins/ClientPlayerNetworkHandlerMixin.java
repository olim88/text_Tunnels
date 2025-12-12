package org.olim.text_tunnels.mixins;


import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.olim.text_tunnels.MessageSendHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayerNetworkHandlerMixin {

    @ModifyVariable(method = "sendChatMessage", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private String textTunnels$sendMessage(String content) {
        //do not try to edit commands;
        if (content.startsWith("/")) {
            return content;
        }
        return MessageSendHandler.getPrefix() + content;
    }
}
