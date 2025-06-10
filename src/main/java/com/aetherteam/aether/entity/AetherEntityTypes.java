package com.aetherteam.aether.entity;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.data.resources.AetherMobCategory;
import com.aetherteam.aether.entity.block.FloatingBlockEntity;
import com.aetherteam.aether.entity.block.TntPresent;
import com.aetherteam.aether.entity.miscellaneous.CloudMinion;
import com.aetherteam.aether.entity.miscellaneous.Parachute;
import com.aetherteam.aether.entity.monster.*;
import com.aetherteam.aether.entity.monster.dungeon.FireMinion;
import com.aetherteam.aether.entity.monster.dungeon.Mimic;
import com.aetherteam.aether.entity.monster.dungeon.Sentry;
import com.aetherteam.aether.entity.monster.dungeon.Valkyrie;
import com.aetherteam.aether.entity.monster.dungeon.boss.Slider;
import com.aetherteam.aether.entity.monster.dungeon.boss.SunSpirit;
import com.aetherteam.aether.entity.monster.dungeon.boss.ValkyrieQueen;
import com.aetherteam.aether.entity.passive.*;
import com.aetherteam.aether.entity.projectile.PoisonNeedle;
import com.aetherteam.aether.entity.projectile.ZephyrSnowball;
import com.aetherteam.aether.entity.projectile.crystal.CloudCrystal;
import com.aetherteam.aether.entity.projectile.crystal.FireCrystal;
import com.aetherteam.aether.entity.projectile.crystal.IceCrystal;
import com.aetherteam.aether.entity.projectile.crystal.ThunderCrystal;
import com.aetherteam.aether.entity.projectile.dart.EnchantedDart;
import com.aetherteam.aether.entity.projectile.dart.GoldenDart;
import com.aetherteam.aether.entity.projectile.dart.PoisonDart;
import com.aetherteam.aether.entity.projectile.weapon.HammerProjectile;
import com.aetherteam.aether.entity.projectile.weapon.ThrownLightningKnife;
import com.aetherteam.aether.item.AetherItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AetherEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Aether.MODID);

    // Passive Mobs
    public static final DeferredHolder<EntityType<?>, EntityType<Phyg>> PHYG = ENTITY_TYPES.register("phyg",
            () -> EntityType.Builder.of(Phyg::new, MobCategory.CREATURE).sized(0.9F, 0.9F).clientTrackingRange(10).build(AetherEntityKeys.PHYG));

    public static final DeferredHolder<EntityType<?>, EntityType<FlyingCow>> FLYING_COW = ENTITY_TYPES.register("flying_cow",
            () -> EntityType.Builder.of(FlyingCow::new, MobCategory.CREATURE).sized(0.9F, 1.4F).clientTrackingRange(10).build(AetherEntityKeys.FLYING_COW));

    public static final DeferredHolder<EntityType<?>, EntityType<Sheepuff>> SHEEPUFF = ENTITY_TYPES.register("sheepuff",
            () -> EntityType.Builder.of(Sheepuff::new, MobCategory.CREATURE).sized(0.9F, 1.3F).clientTrackingRange(10).build(AetherEntityKeys.SHEEPUFF));

    public static final DeferredHolder<EntityType<?>, EntityType<Moa>> MOA = ENTITY_TYPES.register("moa",
            () -> EntityType.Builder.of(Moa::new, MobCategory.CREATURE).sized(0.9F, 2.15F).clientTrackingRange(10).build(AetherEntityKeys.MOA));

    public static final DeferredHolder<EntityType<?>, EntityType<Aerbunny>> AERBUNNY = ENTITY_TYPES.register("aerbunny",
            () -> EntityType.Builder.of(Aerbunny::new, MobCategory.CREATURE).sized(0.6F, 0.5F).clientTrackingRange(10).build(AetherEntityKeys.AERBUNNY));

    public static final DeferredHolder<EntityType<?>, EntityType<Aerwhale>> AERWHALE = ENTITY_TYPES.register("aerwhale",
            () -> EntityType.Builder.of(Aerwhale::new, AetherMobCategory.AETHER_AERWHALE).fireImmune().sized(3.0F, 3.0F).clientTrackingRange(10).build(AetherEntityKeys.AERWHALE));

    // Hostile Mobs
    public static final DeferredHolder<EntityType<?>, EntityType<Swet>> BLUE_SWET = ENTITY_TYPES.register("blue_swet",
            () -> EntityType.Builder.of(Swet::new, AetherMobCategory.AETHER_SURFACE_MONSTER).sized(0.9F, 0.9F).clientTrackingRange(10).build(AetherEntityKeys.BLUE_SWET));

    public static final DeferredHolder<EntityType<?>, EntityType<Swet>> GOLDEN_SWET = ENTITY_TYPES.register("golden_swet",
            () -> EntityType.Builder.of(Swet::new, AetherMobCategory.AETHER_SURFACE_MONSTER).sized(0.9F, 0.9F).clientTrackingRange(10).build(AetherEntityKeys.GOLDEN_SWET));

    public static final DeferredHolder<EntityType<?>, EntityType<PassiveWhirlwind>> WHIRLWIND = ENTITY_TYPES.register("whirlwind",
            () -> EntityType.Builder.of(PassiveWhirlwind::new, AetherMobCategory.AETHER_SURFACE_MONSTER).fireImmune().sized(0.6F, 0.8F).clientTrackingRange(8).build(AetherEntityKeys.WHIRLWIND));

    public static final DeferredHolder<EntityType<?>, EntityType<EvilWhirlwind>> EVIL_WHIRLWIND = ENTITY_TYPES.register("evil_whirlwind",
            () -> EntityType.Builder.of(EvilWhirlwind::new, AetherMobCategory.AETHER_SURFACE_MONSTER).fireImmune().sized(0.6F, 0.8F).clientTrackingRange(8).build(AetherEntityKeys.EVIL_WHIRLWIND));

    public static final DeferredHolder<EntityType<?>, EntityType<AechorPlant>> AECHOR_PLANT = ENTITY_TYPES.register("aechor_plant",
            () -> EntityType.Builder.of(AechorPlant::new, AetherMobCategory.AETHER_SURFACE_MONSTER).sized(1.0F, 1.0F).clientTrackingRange(8).build(AetherEntityKeys.AECHOR_PLANT));

    public static final DeferredHolder<EntityType<?>, EntityType<Cockatrice>> COCKATRICE = ENTITY_TYPES.register("cockatrice",
            () -> EntityType.Builder.of(Cockatrice::new, AetherMobCategory.AETHER_DARKNESS_MONSTER).sized(0.9F, 2.15F).clientTrackingRange(10).build(AetherEntityKeys.COCKATRICE));

    public static final DeferredHolder<EntityType<?>, EntityType<Zephyr>> ZEPHYR = ENTITY_TYPES.register("zephyr",
            () -> EntityType.Builder.of(Zephyr::new, AetherMobCategory.AETHER_SKY_MONSTER).sized(4.5F, 3.5F).clientTrackingRange(10).build(AetherEntityKeys.ZEPHYR));

    // Dungeon Mobs
    public static final DeferredHolder<EntityType<?>, EntityType<Mimic>> MIMIC = ENTITY_TYPES.register("mimic",
            () -> EntityType.Builder.of(Mimic::new, MobCategory.MONSTER).sized(1.0F, 2.0F).clientTrackingRange(8).build(AetherEntityKeys.MIMIC));

    public static final DeferredHolder<EntityType<?>, EntityType<Sentry>> SENTRY = ENTITY_TYPES.register("sentry",
            () -> EntityType.Builder.of(Sentry::new, MobCategory.MONSTER).sized(0.9F, 0.9F).clientTrackingRange(10).build(AetherEntityKeys.SENTRY));

    public static final DeferredHolder<EntityType<?>, EntityType<Slider>> SLIDER = ENTITY_TYPES.register("slider",
            () -> EntityType.Builder.of(Slider::new, MobCategory.MONSTER).sized(2.0F, 2.0F).fireImmune().clientTrackingRange(10).build(AetherEntityKeys.SLIDER));

    public static final DeferredHolder<EntityType<?>, EntityType<Valkyrie>> VALKYRIE = ENTITY_TYPES.register("valkyrie",
            () -> EntityType.Builder.of(Valkyrie::new, MobCategory.MONSTER).sized(0.8F, 1.95F).clientTrackingRange(8).build(AetherEntityKeys.VALKYRIE));

    public static final DeferredHolder<EntityType<?>, EntityType<ValkyrieQueen>> VALKYRIE_QUEEN = ENTITY_TYPES.register("valkyrie_queen",
            () -> EntityType.Builder.of(ValkyrieQueen::new, MobCategory.MONSTER).sized(0.8F, 1.95F).fireImmune().clientTrackingRange(10).build(AetherEntityKeys.VALKYRIE_QUEEN));

    public static final DeferredHolder<EntityType<?>, EntityType<FireMinion>> FIRE_MINION = ENTITY_TYPES.register("fire_minion",
            () -> EntityType.Builder.of(FireMinion::new, MobCategory.MONSTER).sized(1.1F, 1.95F).fireImmune().clientTrackingRange(8).build(AetherEntityKeys.FIRE_MINION));

    public static final DeferredHolder<EntityType<?>, EntityType<SunSpirit>> SUN_SPIRIT = ENTITY_TYPES.register("sun_spirit",
            () -> EntityType.Builder.of(SunSpirit::new, MobCategory.MONSTER).sized(2.5F, 3.4F).fireImmune().clientTrackingRange(10).build(AetherEntityKeys.SUN_SPIRIT));

    // Miscellaneous Entities
    public static final DeferredHolder<EntityType<?>, EntityType<Boat>> SKYROOT_BOAT = ENTITY_TYPES.register("skyroot_boat",
            () -> EntityType.Builder.<Boat>of((type, level) -> new Boat(type, level, AetherItems.SKYROOT_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build(AetherEntityKeys.SKYROOT_BOAT));

    public static final DeferredHolder<EntityType<?>, EntityType<ChestBoat>> SKYROOT_CHEST_BOAT = ENTITY_TYPES.register("skyroot_chest_boat",
            () -> EntityType.Builder.<ChestBoat>of((type, level) -> new ChestBoat(type, level, AetherItems.SKYROOT_CHEST_BOAT), MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build(AetherEntityKeys.SKYROOT_CHEST_BOAT));

    public static final DeferredHolder<EntityType<?>, EntityType<CloudMinion>> CLOUD_MINION = ENTITY_TYPES.register("cloud_minion",
            () -> EntityType.Builder.<CloudMinion>of(CloudMinion::new, MobCategory.MISC).sized(0.75F, 0.75F).clientTrackingRange(5).build(AetherEntityKeys.CLOUD_MINION));

    public static final DeferredHolder<EntityType<?>, EntityType<Parachute>> COLD_PARACHUTE = ENTITY_TYPES.register("cold_parachute",
            () -> EntityType.Builder.of(Parachute::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(8).build(AetherEntityKeys.COLD_PARACHUTE));

    public static final DeferredHolder<EntityType<?>, EntityType<Parachute>> GOLDEN_PARACHUTE = ENTITY_TYPES.register("golden_parachute",
            () -> EntityType.Builder.of(Parachute::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(8).build(AetherEntityKeys.GOLDEN_PARACHUTE));

    public static final DeferredHolder<EntityType<?>, EntityType<FloatingBlockEntity>> FLOATING_BLOCK = ENTITY_TYPES.register("floating_block",
            () -> EntityType.Builder.<FloatingBlockEntity>of(FloatingBlockEntity::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(10).updateInterval(20).build(AetherEntityKeys.FLOATING_BLOCK));

    public static final DeferredHolder<EntityType<?>, EntityType<TntPresent>> TNT_PRESENT = ENTITY_TYPES.register("tnt_present",
            () -> EntityType.Builder.<TntPresent>of(TntPresent::new, MobCategory.MISC).fireImmune().sized(1.0F, 1.0F).eyeHeight(0.15F).clientTrackingRange(10).updateInterval(10).build(AetherEntityKeys.TNT_PRESENT));

    // Projectiles
    public static final DeferredHolder<EntityType<?>, EntityType<ZephyrSnowball>> ZEPHYR_SNOWBALL = ENTITY_TYPES.register("zephyr_snowball",
            () -> EntityType.Builder.<ZephyrSnowball>of(ZephyrSnowball::new, MobCategory.MISC).sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10).build(AetherEntityKeys.ZEPHYR_SNOWBALL));

    public static final DeferredHolder<EntityType<?>, EntityType<CloudCrystal>> CLOUD_CRYSTAL = ENTITY_TYPES.register("cloud_crystal",
            () -> EntityType.Builder.<CloudCrystal>of(CloudCrystal::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build(AetherEntityKeys.CLOUD_CRYSTAL));

    public static final DeferredHolder<EntityType<?>, EntityType<FireCrystal>> FIRE_CRYSTAL = ENTITY_TYPES.register("fire_crystal",
            () -> EntityType.Builder.<FireCrystal>of(FireCrystal::new, MobCategory.MISC).sized(0.85F, 0.85F).clientTrackingRange(4).updateInterval(10).fireImmune().build(AetherEntityKeys.FIRE_CRYSTAL));

    public static final DeferredHolder<EntityType<?>, EntityType<IceCrystal>> ICE_CRYSTAL = ENTITY_TYPES.register("ice_crystal",
            () -> EntityType.Builder.<IceCrystal>of(IceCrystal::new, MobCategory.MISC).sized(1.2F, 1.2F).clientTrackingRange(4).updateInterval(10).fireImmune().build(AetherEntityKeys.ICE_CRYSTAL));

    public static final DeferredHolder<EntityType<?>, EntityType<ThunderCrystal>> THUNDER_CRYSTAL = ENTITY_TYPES.register("thunder_crystal",
            () -> EntityType.Builder.<ThunderCrystal>of(ThunderCrystal::new, MobCategory.MISC).sized(0.7F, 0.7F).updateInterval(2).build(AetherEntityKeys.THUNDER_CRYSTAL));

    public static final DeferredHolder<EntityType<?>, EntityType<GoldenDart>> GOLDEN_DART = ENTITY_TYPES.register("golden_dart",
            () -> EntityType.Builder.<GoldenDart>of(GoldenDart::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(AetherEntityKeys.GOLDEN_DART));

    public static final DeferredHolder<EntityType<?>, EntityType<PoisonDart>> POISON_DART = ENTITY_TYPES.register("poison_dart",
            () -> EntityType.Builder.<PoisonDart>of(PoisonDart::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(AetherEntityKeys.POISON_DART));

    public static final DeferredHolder<EntityType<?>, EntityType<EnchantedDart>> ENCHANTED_DART = ENTITY_TYPES.register("enchanted_dart",
            () -> EntityType.Builder.<EnchantedDart>of(EnchantedDart::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(AetherEntityKeys.ENCHANTED_DART));

    public static final DeferredHolder<EntityType<?>, EntityType<PoisonNeedle>> POISON_NEEDLE = ENTITY_TYPES.register("poison_needle",
            () -> EntityType.Builder.<PoisonNeedle>of(PoisonNeedle::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build(AetherEntityKeys.POISON_NEEDLE));

    public static final DeferredHolder<EntityType<?>, EntityType<ThrownLightningKnife>> LIGHTNING_KNIFE = ENTITY_TYPES.register("lightning_knife",
            () -> EntityType.Builder.<ThrownLightningKnife>of(ThrownLightningKnife::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build(AetherEntityKeys.LIGHTNING_KNIFE));

    public static final DeferredHolder<EntityType<?>, EntityType<HammerProjectile>> HAMMER_PROJECTILE = ENTITY_TYPES.register("hammer_projectile",
            () -> EntityType.Builder.<HammerProjectile>of(HammerProjectile::new, MobCategory.MISC).sized(0.35F, 0.35F).clientTrackingRange(4).updateInterval(10).build(AetherEntityKeys.HAMMER_PROJECTILE));

    /**
     * @see Aether#eventSetup(IEventBus)
     */
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        // Passive Mobs
        event.register(AetherEntityTypes.PHYG.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AetherAnimal::checkAetherAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(AetherEntityTypes.FLYING_COW.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AetherAnimal::checkAetherAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(AetherEntityTypes.SHEEPUFF.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AetherAnimal::checkAetherAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(AetherEntityTypes.MOA.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AetherAnimal::checkAetherAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(AetherEntityTypes.AERBUNNY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AetherAnimal::checkAetherAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(AetherEntityTypes.AERWHALE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Aerwhale::checkAerwhaleSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);

        // Hostile Mobs
        event.register(AetherEntityTypes.BLUE_SWET.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Swet::checkSwetSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(AetherEntityTypes.GOLDEN_SWET.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Swet::checkSwetSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(AetherEntityTypes.WHIRLWIND.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractWhirlwind::checkWhirlwindSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(AetherEntityTypes.EVIL_WHIRLWIND.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AbstractWhirlwind::checkWhirlwindSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(AetherEntityTypes.AECHOR_PLANT.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AechorPlant::checkAechorPlantSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(AetherEntityTypes.COCKATRICE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Cockatrice::checkCockatriceSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(AetherEntityTypes.ZEPHYR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Zephyr::checkZephyrSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
    }

    /**
     * @see Aether#eventSetup(IEventBus)
     */
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        // Passive Mobs
        event.put(AetherEntityTypes.PHYG.get(), Phyg.createMobAttributes().build());
        event.put(AetherEntityTypes.FLYING_COW.get(), FlyingCow.createMobAttributes().build());
        event.put(AetherEntityTypes.SHEEPUFF.get(), Sheepuff.createMobAttributes().build());
        event.put(AetherEntityTypes.MOA.get(), Moa.createMobAttributes().build());
        event.put(AetherEntityTypes.AERBUNNY.get(), Aerbunny.createMobAttributes().build());
        event.put(AetherEntityTypes.AERWHALE.get(), Aerwhale.createMobAttributes().build());

        // Hostile Mobs
        event.put(AetherEntityTypes.BLUE_SWET.get(), Swet.createMobAttributes().build());
        event.put(AetherEntityTypes.GOLDEN_SWET.get(), Swet.createMobAttributes().build());
        event.put(AetherEntityTypes.WHIRLWIND.get(), AbstractWhirlwind.createMobAttributes().build());
        event.put(AetherEntityTypes.EVIL_WHIRLWIND.get(), AbstractWhirlwind.createMobAttributes().build());
        event.put(AetherEntityTypes.AECHOR_PLANT.get(), AechorPlant.createMobAttributes().build());
        event.put(AetherEntityTypes.COCKATRICE.get(), Cockatrice.createMobAttributes().build());
        event.put(AetherEntityTypes.ZEPHYR.get(), Zephyr.createMobAttributes().build());

        // Dungeon Mobs
        event.put(AetherEntityTypes.MIMIC.get(), Mimic.createMobAttributes().build());
        event.put(AetherEntityTypes.SENTRY.get(), Sentry.createMobAttributes().build());
        event.put(AetherEntityTypes.SLIDER.get(), Slider.createMobAttributes().build());
        event.put(AetherEntityTypes.VALKYRIE.get(), Valkyrie.createMobAttributes().build());
        event.put(AetherEntityTypes.VALKYRIE_QUEEN.get(), ValkyrieQueen.createMobAttributes().build());
        event.put(AetherEntityTypes.FIRE_MINION.get(), FireMinion.createMobAttributes().build());
        event.put(AetherEntityTypes.SUN_SPIRIT.get(), SunSpirit.createMobAttributes().build());

        // Miscellaneous Entities
        event.put(AetherEntityTypes.CLOUD_MINION.get(), CloudMinion.createMobAttributes().build());
    }
}
