package org.olim.text_tunnels.mixins;


import net.minecraft.client.multiplayer.ClientPacketListener;
import org.olim.text_tunnels.MessageSendHandler;
import org.olim.text_tunnels.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ClientPacketListener.class)
public class ClientPlayerNetworkHandlerMixin {

    @ModifyVariable(method = "sendChat", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private String textTunnels$sendMessage(String content) {
        //do not try to edit commands;
        if (content.startsWith("/") || !ConfigManager.get().mainConfig.enabled) {
            return content;
        }
        return MessageSendHandler.getPrefix() + content;
    }
}
