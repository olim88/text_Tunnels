package org.olim.text_tunnels.mixins;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
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
    protected TextFieldWidget chatField;

    @Inject(method = "resize", at = @At("TAIL"))
    private void textTunnels$afterResize(CallbackInfo ci) {
        ButtonsHandler.updatePositions(chatField.getX(), chatField.getY(), chatField.getHeight());
    }
    @Inject(method = "init", at = @At("RETURN"))
    private void textTunnels$afterInit(CallbackInfo ci) {
        ButtonsHandler.updatePositions(chatField.getX(), chatField.getY(), chatField.getHeight());
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void textTunnels$afterRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ButtonsHandler.render(context, mouseX, mouseY, delta);
    }

    @Inject(method = "mouseClicked", at= @At("RETURN"))
    private void textTunnels$mouseClicked(Click click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        ButtonsHandler.mouseClicked(click, doubled);
    }



}
