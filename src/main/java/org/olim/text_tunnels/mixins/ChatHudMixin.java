package org.olim.text_tunnels.mixins;


import org.objectweb.asm.Opcodes;
import org.olim.text_tunnels.MessageReceiveHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.client.gui.components.ChatComponent;

@Mixin(ChatComponent.class)

public class ChatHudMixin {


    @Shadow
    @Final
    private List<GuiMessage.Line> trimmedMessages;

    @Redirect(method = "extractRenderState(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<GuiMessage.Line> textTunnels$beforeRender(ChatComponent instance) {
        return filterVisible();
    }


    @Redirect(method = "scrollChat", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<GuiMessage.Line> textTunnels$editVisibleForScroll(ChatComponent instance) {
        return filterVisible();
    }

    @Redirect(method = "refreshTrimmedMessages", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<GuiMessage.Line> textTunnels$editRefresh(ChatComponent instance) {
        return filterVisible();
    }

    @Redirect(method = "forEachLine", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<GuiMessage.Line> textTunnels$editForEachVisible(ChatComponent instance) {
        return filterVisible();
    }

    @Unique
    private List<GuiMessage.Line> filterVisible() {
        if (MessageReceiveHandler.isFilterInActive()) {
            return trimmedMessages;
        }
        return trimmedMessages.stream().filter(message -> MessageReceiveHandler.shouldShow(message.addedTime())).collect(Collectors.toList());
    }
}
