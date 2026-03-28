package org.olim.text_tunnels.mixins;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.MouseButtonEvent;
import org.olim.text_tunnels.ButtonsHandler;
import org.olim.text_tunnels.MessageSendHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Shadow
    protected EditBox input;

    @Inject(method = "resize", at = @At("TAIL"))
    private void textTunnels$afterResize(CallbackInfo ci) {
        ButtonsHandler.updatePositions(input.getX(), input.getY(), input.getHeight());
    }
    @Inject(method = "init", at = @At("RETURN"))
    private void textTunnels$afterInit(CallbackInfo ci) {
        ButtonsHandler.updatePositions(input.getX(), input.getY(), input.getHeight());
    }

    @Inject(method = "extractRenderState", at = @At("HEAD"))
    private void textTunnels$afterRender(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ButtonsHandler.render(context, mouseX, mouseY, delta);
    }

    @Inject(method = "mouseClicked", at= @At("RETURN"))
    private void textTunnels$mouseClicked(MouseButtonEvent click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        ButtonsHandler.mouseClicked(click, doubled);
    }



}
