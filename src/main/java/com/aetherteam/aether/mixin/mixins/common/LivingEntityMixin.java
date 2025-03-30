package com.aetherteam.aether.mixin.mixins.common;

import com.aetherteam.aether.entity.monster.dungeon.boss.ValkyrieQueen;
import com.aetherteam.aether.event.events.AetherEntityEvents;
import com.aetherteam.aether.item.combat.abilities.armor.PhoenixArmor;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Shadow
    @Nullable
    protected Player lastHurtByPlayer;

    /**
     * Handles vertical swimming for Phoenix Armor in lava without being affected by the upwards speed debuff from lava.
     * @param ci The {@link CallbackInfo} for the void method return.
     * @see PhoenixArmor#boostVerticalLavaSwimming(LivingEntity)
     */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getFluidJumpThreshold()D", shift = At.Shift.AFTER), method = "travel(Lnet/minecraft/world/phys/Vec3;)V")
    private void travel(CallbackInfo ci) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        PhoenixArmor.boostVerticalLavaSwimming(livingEntity);
    }

    @WrapWithCondition(method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;knockback(DDD)V"))
    private boolean hurt(LivingEntity instance, double strength, double x, double z, @Local(argsOnly = true) DamageSource source) {
        return (!(instance instanceof ValkyrieQueen) || !(source.getDirectEntity() instanceof Projectile));
    }

    @WrapOperation(method = "dropExperience", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"))
    private void aetherFabric$adjustExperienceAmount(ServerLevel level, Vec3 pos, int amount, Operation<Void> original) {
        var helper = new AetherEntityEvents.ExperienceDropHelper(amount);

        AetherEntityEvents.ON_EXPERIENCE_DROP.invoker().onExperienceDrop((LivingEntity) (Object) this, this.lastHurtByPlayer, helper);

        original.call(level, pos, helper.getFinalExperienceAmount());
    }
}
