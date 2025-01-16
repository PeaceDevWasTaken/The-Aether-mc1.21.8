package com.aetherteam.aether.utils;

import com.aetherteam.aether.mixin.mixins.common.accessor.EntityAccessor;
import io.github.fabricators_of_create.porting_lib.enchant.CustomEnchantingBehaviorItem;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.material.Fluid;

import java.util.Map;

public class FabricUtils {
    public static boolean isInFluidType(Entity livingEntity) {
        for (Object2DoubleMap.Entry<TagKey<Fluid>> entry : ((EntityAccessor) livingEntity).getFluidHeight().object2DoubleEntrySet()) {
            return entry.getDoubleValue() > 0;
        }
        return false;
    }

    public static boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (stack.getItem() instanceof CustomEnchantingBehaviorItem enchantingItem)
            return enchantingItem.canApplyAtEnchantingTable(stack, enchantment);
        return enchantment.category.canEnchant(stack.getItem());
    }

    public static Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
        return EnchantmentHelper.deserializeEnchantments(stack.getEnchantmentTags());
    }
}
