package com.aetherteam.aether.mixin.mixins.client.fabric;

import io.wispforest.accessories.impl.AccessoriesEventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStand.class)
public class ArmorStandMixin {
    @Inject(method = "brokenByAnything(Lnet/minecraft/world/damagesource/DamageSource;)V", at = @At(value = "HEAD"))
    private void handleAccessoriesDrop(DamageSource damageSource, CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        AccessoriesEventHandler.onDeath(entity, damageSource);
    }
}
