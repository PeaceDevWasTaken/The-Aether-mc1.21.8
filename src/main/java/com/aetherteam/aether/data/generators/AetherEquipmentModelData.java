package com.aetherteam.aether.data.generators;

import com.aetherteam.aether.data.resources.registries.AetherEquipmentModels;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentModel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AetherEquipmentModelData implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public AetherEquipmentModelData(PackOutput packOutput) {
        this.pathProvider = packOutput.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/equipment");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Map<ResourceLocation, EquipmentModel> map = new HashMap<>();
        AetherEquipmentModels.bootstrap((location, model) -> {
            if (map.putIfAbsent(location, model) != null) {
                throw new IllegalStateException("Tried to register equipment model twice for id: " + location);
            }
        });
        return DataProvider.saveAll(output, EquipmentModel.CODEC, this.pathProvider, map);
    }

    @Override
    public String getName() {
        return "Aether Equipment Model Definitions";
    }
}
