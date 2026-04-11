package org.olim.text_tunnels.mixins;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
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

@Mixin(ChatHud.class)

public class ChatHudMixin {


    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;

    @WrapOperation(method = "render(Lnet/minecraft/client/gui/hud/ChatHud$Backend;IIZ)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> textTunnels$beforeRender(ChatHud instance, Operation<List<ChatHudLine.Visible>> original) {
        return filterVisible(instance, original);
    }


    @WrapOperation(method = "scroll", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> textTunnels$editVisibleForScroll(ChatHud instance, Operation<List<ChatHudLine.Visible>> original) {
        return filterVisible(instance, original);
    }

    @WrapOperation(method = "refresh", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> textTunnels$editRefresh(ChatHud instance, Operation<List<ChatHudLine.Visible>> original) {
        return filterVisible(instance, original);
    }

    @WrapOperation(method = "forEachVisibleLine", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> textTunnels$editForEachVisible(ChatHud instance, Operation<List<ChatHudLine.Visible>> original) {
        return filterVisible(instance, original);
    }

    @Unique
    private List<ChatHudLine.Visible> filterVisible(ChatHud instance, Operation<List<ChatHudLine.Visible>> original) {
        if (MessageReceiveHandler.isFilterInActive()) {
            original.call(instance);
        }
        return visibleMessages.stream().filter(message -> MessageReceiveHandler.shouldShow(message.addedTime())).collect(Collectors.toCollection(ArrayList::new));
    }
}
