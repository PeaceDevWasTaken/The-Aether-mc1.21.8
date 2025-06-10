package com.aetherteam.aether.data.generators.loot;

import com.aetherteam.aether.loot.AetherLoot;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.packs.LootData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public record AetherShearingLoot(HolderLookup.Provider registries) implements LootTableSubProvider {
    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> builder) {
        LootData.WOOL_ITEM_BY_DYE.forEach((dyeColor, item) -> builder.accept(
            AetherLoot.SHEAR_SHEEPUFF_BY_DYE.get(dyeColor),
            LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1.0F, 3.0F)).add(LootItem.lootTableItem(item)))
        ));
        builder.accept(
            AetherLoot.SHEAR_SHEEPUFF, LootTable.lootTable().withPool(EntityLootSubProvider.createSheepDispatchPool(AetherLoot.SHEAR_SHEEPUFF_BY_DYE))
        );
    }
}
