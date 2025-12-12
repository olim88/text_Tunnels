package org.olim.text_tunnels.mixins;


import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import org.objectweb.asm.Opcodes;
import org.olim.text_tunnels.MessageReceiveHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(ChatHud.class)

public class ChatHudMixin {


    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;

    @Redirect(method = "render(Lnet/minecraft/client/gui/hud/ChatHud$Backend;IIZ)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> textTunnels$beforeRender(ChatHud instance) {
        return filterVisible();
    }


    @Redirect(method = "scroll", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> textTunnels$editVisibleForScroll(ChatHud instance) {
        return filterVisible();
    }

    @Redirect(method = "refresh", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> textTunnels$editRefresh(ChatHud instance) {
        return filterVisible();
    }

    @Redirect(method = "forEachVisibleLine", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/hud/ChatHud;visibleMessages:Ljava/util/List;", opcode = Opcodes.GETFIELD))
    private List<ChatHudLine.Visible> textTunnels$editForEachVisible(ChatHud instance) {
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
