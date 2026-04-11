package org.olim.text_tunnels.mixins;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import org.objectweb.asm.Opcodes;
import org.olim.text_tunnels.MessageReceiveHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mixin(ChatComponent.class)

public class ChatHudMixin {


    @Shadow
    @Final
    private List<GuiMessage.Line> trimmedMessages;

    @WrapOperation(method = "extractRenderState(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<GuiMessage.Line> textTunnels$beforeRender(ChatComponent instance, Operation<List<GuiMessage.Line>> original) {
        return filterVisible(instance, original);
    }


    @WrapOperation(method = "scrollChat", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<GuiMessage.Line> textTunnels$editVisibleForScroll(ChatComponent instance, Operation<List<GuiMessage.Line>> original) {
        return filterVisible(instance, original);
    }

    @WrapOperation(method = "refreshTrimmedMessages", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<GuiMessage.Line> textTunnels$editRefresh(ChatComponent instance, Operation<List<GuiMessage.Line>> original) {
        return filterVisible(instance, original);
    }

    @WrapOperation(method = "forEachLine", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/ChatComponent;trimmedMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<GuiMessage.Line> textTunnels$editForEachVisible(ChatComponent instance, Operation<List<GuiMessage.Line>> original) {
        return filterVisible(instance, original);
    }

    @Unique
    private List<GuiMessage.Line> filterVisible(ChatComponent instance,Operation<List<GuiMessage.Line>> original) {
        if (MessageReceiveHandler.isFilterInActive()) {
            return original.call(instance);
        }
        return trimmedMessages.stream().filter(message -> MessageReceiveHandler.shouldShow(message.addedTime())).collect(Collectors.toCollection(ArrayList::new));
    }
}
