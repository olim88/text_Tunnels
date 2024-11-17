package org.olim.text_tunnels.mixins;


import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.olim.text_tunnels.MessageReceiveHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChatHud.class)
public class ChatHudMixin {


    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", at = @At("HEAD"))
    private void onLogChatMessage(Text message, MessageSignatureData signatureData, MessageIndicator indicator, CallbackInfo ci) {
        // You can modify the message here before it gets added
        MessageReceiveHandler.addMessage(message);
    }


    @Redirect(method = "render", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> beforeRender(ChatHud instance) {
       return filterVisible();
    }

    @Redirect(method = "scroll", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> editVisibleForScroll(ChatHud instance) {
        return filterVisible();
    }
    @Redirect(method = "getTextStyleAt", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> editVisibleForGetTextStyleAt(ChatHud instance) {
        return filterVisible();
    }
    @Redirect(method = "getIndicatorAt", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> editVisibleForGetIndicatorAt(ChatHud instance) {
        return filterVisible();
    }
    @Redirect(method = "getMessageIndex", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> editVisibleForGetMessageIndex(ChatHud instance) {
        return filterVisible();
    }
    @Redirect(method = "getMessageLineIndex", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> editVisibleForGetMessageLineIndex(ChatHud instance) {
        return filterVisible();
    }

    @Unique
    private List<ChatHudLine.Visible> filterVisible() {
        if (MessageReceiveHandler.isFilterInActive()) {
            return visibleMessages;
        }
        return visibleMessages.stream().filter(message -> MessageReceiveHandler.shouldShow(message.addedTime())).toList();
    }




}
