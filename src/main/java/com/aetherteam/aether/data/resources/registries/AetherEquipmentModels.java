package com.aetherteam.aether.data.resources.registries;

import com.aetherteam.aether.Aether;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentModel;

import java.util.function.BiConsumer;

public class AetherEquipmentModels {
    public static final ResourceLocation ZANITE = ResourceLocation.fromNamespaceAndPath(Aether.MODID, "zanite");
    public static final ResourceLocation GRAVITITE = ResourceLocation.fromNamespaceAndPath(Aether.MODID, "gravitite");
    public static final ResourceLocation NEPTUNE = ResourceLocation.fromNamespaceAndPath(Aether.MODID, "neptune");
    public static final ResourceLocation VALKYRIE = ResourceLocation.fromNamespaceAndPath(Aether.MODID, "valkyrie");
    public static final ResourceLocation PHOENIX = ResourceLocation.fromNamespaceAndPath(Aether.MODID, "phoenix");
    public static final ResourceLocation OBSIDIAN = ResourceLocation.fromNamespaceAndPath(Aether.MODID, "obsidian");
    public static final ResourceLocation SENTRY = ResourceLocation.fromNamespaceAndPath(Aether.MODID, "sentry");

    public static void bootstrap(BiConsumer<ResourceLocation, EquipmentModel> output) {
        output.accept(ZANITE, onlyHumanoid("zanite"));
        output.accept(GRAVITITE, onlyHumanoid("gravitite"));
        output.accept(NEPTUNE, onlyHumanoid("neptune"));
        output.accept(VALKYRIE, onlyHumanoid("valkyrie"));
        output.accept(PHOENIX, onlyHumanoid("phoenix"));
        output.accept(OBSIDIAN, onlyHumanoid("obsidian"));
        output.accept(SENTRY, onlyHumanoidLeggings("sentry"));
    }

    private static EquipmentModel onlyHumanoid(String name) {
        return EquipmentModel.builder().addHumanoidLayers(ResourceLocation.fromNamespaceAndPath(Aether.MODID, name)).build();
    }

    private static EquipmentModel onlyHumanoidLeggings(String name) {
        return EquipmentModel.builder().addLayers(EquipmentModel.LayerType.HUMANOID_LEGGINGS, EquipmentModel.Layer.leatherDyeable(ResourceLocation.fromNamespaceAndPath(Aether.MODID, name), false)).build();
    }
}
