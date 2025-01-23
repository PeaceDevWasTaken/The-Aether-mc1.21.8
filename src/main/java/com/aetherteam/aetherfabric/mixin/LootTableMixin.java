package com.aetherteam.aetherfabric.mixin;

import com.aetherteam.aether.loot.modifiers.AetherLootTableModifications;
import com.aetherteam.aetherfabric.pond.LootContextExtension;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(LootTable.class)
public abstract class LootTableMixin {
    @WrapMethod(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V")
    private void finishCollectingLoot(LootContext context, Consumer<ItemStack> consumer, Operation<Void> original) {
        var lootTableId = context.getLevel().getServer().reloadableRegistries().get()
            .registry(Registries.LOOT_TABLE)
            .flatMap(lootTables -> {
                return lootTables.getResourceKey((LootTable)(Object)this)
                    .map(ResourceKey::location);
            });

        ((LootContextExtension) context).pushTableId(lootTableId.orElse(null));

        ObjectArrayList<ItemStack> stacks = new ObjectArrayList<>();
        original.call(context, (Consumer<ItemStack>) stacks::add);
        AetherLootTableModifications.apply(stacks, context).forEach(consumer);

        ((LootContextExtension) context).popTableId();
    }
}
