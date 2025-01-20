package com.aetherteam.aether.mixin.mixins.client.fabric;

import com.aetherteam.aether.client.event.listeners.MenuListener;
import com.aetherteam.aether.client.event.listeners.WorldPreviewListener;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Minecraft.class, priority = 444)
public class MinecraftMixin {
    @Inject(method = "setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;added()V", shift = At.Shift.BEFORE))
    public void setScreen(Screen guiScreen, CallbackInfo ci) {
        MenuListener.onGuiOpenHighest();
    }

    @Inject(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;init(Lnet/minecraft/client/Minecraft;II)V", shift = At.Shift.AFTER))
    private void afterInit(Screen guiScreen, CallbackInfo ci) {
        WorldPreviewListener.onGuiOpenLowest(guiScreen);
    }
}
