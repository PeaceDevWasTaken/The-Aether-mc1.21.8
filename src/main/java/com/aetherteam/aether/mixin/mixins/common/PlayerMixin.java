package com.aetherteam.aether.mixin.mixins.common;

import com.aetherteam.aether.entity.passive.MountableAnimal;
import com.aetherteam.aether.event.hooks.AbilityHooks;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow
    public abstract void resetAttackStrengthTicker();

    @WrapOperation(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;resetAttackStrengthTicker()V"))
    private void aetherFabric$preventResetHere(Player instance, Operation<Void> original) {
        // NO-OP
    }

    @Inject(method = "attack(Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "RETURN", ordinal = 2))
    private void aetherFabric$callResetAtEnd(Entity target, CallbackInfo ci) {
        this.resetAttackStrengthTicker();
    }

    /**
     * Damages gloves only once during a sweeping attack, instead of once for every damaged entity in the attack.
     * @param target The target {@link Entity}.
     * @param ci The {@link CallbackInfo} for the void method return.
     * @see AbilityHooks.AccessoryHooks#damageGloves(Player)
     */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostHurtEffects(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/Entity;)V", shift = At.Shift.AFTER), method = "attack(Lnet/minecraft/world/entity/Entity;)V")
    private void attack(Entity target, CallbackInfo ci) {
        Player player = (Player) (Object) this;
        AbilityHooks.AccessoryHooks.damageGloves(player);
    }

    /**
     * Used to set whether the player tried to crouch for {@link MountableAnimal}, before crouching is cancelled for mounts by the {@link Player} class.
     * @param ci The {@link CallbackInfo} for the void method return.
     */
    @Inject(at = @At(value = "HEAD"), method = "rideTick()V")
    private void rideTick(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        if (!player.level().isClientSide()) {
            if (player.isPassenger() && player.getVehicle() instanceof MountableAnimal mountableAnimal) {
                mountableAnimal.setPlayerTriedToCrouch(player.isShiftKeyDown());
            }
        }
    }
}
