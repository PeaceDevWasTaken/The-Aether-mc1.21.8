package com.aetherteam.aether.event.listeners;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.event.hooks.EntityHooks;
import io.github.fabricators_of_create.porting_lib.core.event.BaseEvent;
import io.github.fabricators_of_create.porting_lib.entity.events.*;
import io.github.fabricators_of_create.porting_lib.entity.events.living.MobEffectEvent;
import io.wispforest.accessories.api.events.OnDeathCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class EntityListener {
    /**
     * @see EntityHooks#addGoals(Entity)
     * @see EntityHooks#canMobSpawnWithAccessories(Entity)
     */
    public static boolean onEntityJoin(Entity entity, Level world, boolean loadedFromDisk) {
        EntityHooks.addGoals(entity);
        return true;
    }

    /**
     * @see EntityHooks#dismountPrevention(Entity, Entity, boolean)
     */
    public static boolean onMountEntity(Entity mountEntity, Entity riderEntity, boolean mounting) {
        return !EntityHooks.dismountPrevention(riderEntity, mountEntity, !mounting);
    }

    /**
     * @see EntityHooks#launchMount(Player)
     */
    public static void onRiderTick(Player player) {
        EntityHooks.launchMount(player);
    }

    /**
     * @see EntityHooks#skyrootBucketMilking(Entity, Player, InteractionHand)
     * @see EntityHooks#pickupBucketable(Entity, Player, InteractionHand)
     * @see EntityHooks#interactWithArmorStand(Entity, Player, ItemStack, Vec3, InteractionHand)
     */
    public static InteractionResult onInteractWithEntity(Entity targetEntity, Player player, InteractionHand interactionHand, @Nullable EntityHitResult hitResult) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        Vec3 position;

        if (hitResult == null) {
            AABB aABB = player.getBoundingBox().expandTowards(targetEntity.position()).inflate(1.0);
            hitResult = ProjectileUtil.getEntityHitResult(player, player.getEyePosition(), targetEntity.position(), aABB, entity -> entity == targetEntity, player.position().distanceTo(targetEntity.position()) + 1);
        }

        if (hitResult != null) {
            position = hitResult.getLocation();
        } else {
            position = targetEntity.position();
        }

        EntityHooks.skyrootBucketMilking(targetEntity, player, interactionHand);
        Optional<InteractionResult> result = EntityHooks.pickupBucketable(targetEntity, player, interactionHand);
        if (result.isEmpty()) {
            result = EntityHooks.interactWithArmorStand(targetEntity, player, itemStack, position, interactionHand);
        }
        return result.orElse(InteractionResult.PASS);
    }

    /**
     * @see EntityHooks#preventEntityHooked(Entity, HitResult)
     */
    public static void onProjectileHitEntity(ProjectileImpactEvent event) {
        Entity projectileEntity = event.getEntity();
        HitResult rayTraceResult = event.getRayTraceResult();
        event.setCanceled(EntityHooks.preventEntityHooked(projectileEntity, rayTraceResult));
    }

    /**
     * @see EntityHooks#preventSliderShieldBlock(DamageSource)
     */
    public static void onShieldBlock(ShieldBlockEvent event) {
        if (!event.isCanceled()) {
            event.setCanceled(EntityHooks.preventSliderShieldBlock(event.getDamageSource()));
        }
    }

    /**
     * @see EntityHooks#lightningHitKeys(Entity)
     */
    public static void onLightningStrike(EntityStruckByLightningEvent event) {
        Entity entity = event.getEntity();
        LightningBolt lightningBolt = event.getLightning();
        if (EntityHooks.lightningHitKeys(entity) || EntityHooks.thunderCrystalHitItems(entity, lightningBolt)) {
            event.setCanceled(true);
        }
    }

    /**
     * @see EntityHooks#trackDrops(LivingEntity, Collection)
     */
    public static boolean onPlayerDrops(LivingEntity entity, DamageSource source, Collection<ItemEntity> itemDrops, int lootingLevel, boolean recentlyHit) {
        EntityHooks.trackDrops(entity, itemDrops);
        return false;
    }

    /**
     * @see EntityHooks#modifyExperience(LivingEntity, int)
     */
    public static int onDropExperience(int experience, Player attackingPlayer, LivingEntity livingEntity) {
        int newExperience = EntityHooks.modifyExperience(livingEntity, experience);
        return newExperience;
    }

    /**
     * @see EntityHooks#preventInebriation(LivingEntity, MobEffectInstance)
     */
    public static void onEffectApply(MobEffectEvent.Applicable event) {
        LivingEntity livingEntity = event.getEntity();
        MobEffectInstance effectInstance = event.getEffectInstance();
        if (EntityHooks.preventInebriation(livingEntity, effectInstance)) {
            event.setResult(BaseEvent.Result.DENY);
        }
    }

    public static void init() {
        EntityEvents.ON_JOIN_WORLD.register(EntityListener::onEntityJoin);
        EntityMountEvents.registerForBoth(EntityListener::onMountEntity);
        PlayerTickEvents.START.register(EntityListener::onRiderTick);
        PlayerTickEvents.END.register(EntityListener::onRiderTick);
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> EntityListener.onInteractWithEntity(entity, player, hand, hitResult));
        ProjectileImpactEvent.PROJECTILE_IMPACT.register(EntityListener::onProjectileHitEntity);
        ShieldBlockEvent.EVENT.register(EntityListener::onShieldBlock);
        EntityStruckByLightningEvent.ENTITY_STRUCK_BY_LIGHTING.register(EntityListener::onLightningStrike);
        LivingEntityEvents.DROPS.register(EntityListener::onPlayerDrops);
        LivingEntityEvents.EXPERIENCE_DROP.register(EntityListener::onDropExperience);
        MobEffectEvent.APPLICABLE.register(EntityListener::onEffectApply);

        OnDeathCallback.EVENT.register((currentState, entity, capability, damageSource, droppedStacks) -> {
            List<ItemStack> droppedStacksCopy = new ArrayList<>(droppedStacks);
            boolean recentlyHit = entity.hurtMarked;
            int looting = EnchantmentHelper.getMobLooting(entity);
            droppedStacks.clear();
            droppedStacks.addAll(EntityHooks.handleEntityAccessoryDrops(entity, droppedStacksCopy, recentlyHit, looting));
            return TriState.DEFAULT;
        });
    }
}
