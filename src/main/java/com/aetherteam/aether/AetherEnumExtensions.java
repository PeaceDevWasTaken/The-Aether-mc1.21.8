package com.aetherteam.aether;

import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.item.AetherItems;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;

import java.util.List;
import java.util.function.Supplier;

public class AetherEnumExtensions {
    public static final EnumProxy<Rarity> AETHER_LOOT_RARITY_PROXY = new EnumProxy<>(
        Rarity.class, -1, "aether:loot", ChatFormatting.GREEN
    );

    public static Object skyrootBoatType(int idx, Class<?> type) {
        if (idx == 5) // Lazy away around boxing the boolean
            return false;
        return type.cast(switch (idx) {
            case 0 -> (Supplier<Block>) AetherBlocks.SKYROOT_PLANKS;
            case 1 -> (String) "aether:skyroot";
            case 2 -> (Supplier<Item>) AetherItems.SKYROOT_BOAT;
            case 3 -> (Supplier<Item>) AetherItems.SKYROOT_CHEST_BOAT;
            case 4 -> (Supplier<Item>) AetherItems.SKYROOT_STICK;
            default -> throw new IllegalArgumentException("Unexpected parameter index: " + idx);
        });
    }

    private static String prefix(String id) {
        return Aether.MODID + ":" + id;
    }
}
