package com.aetherteam.aether.mixin.mixins.client.jei;

import com.aetherteam.aether.integration.quark.AccessoriesBackpackScreen;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.runtime.IScreenHelper;
import mezz.jei.gui.events.GuiEventHandler;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.stream.Stream;

@Mixin(GuiEventHandler.class)
public class GuiEventHandlerMixin {
    @WrapOperation(method = "onDrawScreenPost(Lnet/minecraft/client/gui/screens/Screen;Lnet/minecraft/client/gui/GuiGraphics;II)V", at = @At(value = "INVOKE", target = "Lmezz/jei/api/runtime/IScreenHelper;getGuiClickableArea(Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;DD)Ljava/util/stream/Stream;"), remap = false)
    public Stream<IGuiClickableArea> onDrawScreenPost(IScreenHelper instance, AbstractContainerScreen<?> abstractContainerScreen, double mouseX, double mouseY, Operation<Stream<IGuiClickableArea>> original) {
        if (abstractContainerScreen.getClass() == AccessoriesBackpackScreen.class) {
            return Stream.empty();
        }
        return original.call(instance, abstractContainerScreen, mouseX, mouseX);
    }
}
