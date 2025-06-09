package com.aetherteam.aether.recipe.recipes.item;

import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.recipe.book.AetherBookCategory;
import com.aetherteam.aether.recipe.AetherRecipeSerializers;
import com.aetherteam.aether.recipe.AetherRecipeTypes;
import com.aetherteam.aether.recipe.serializer.AetherCookingSerializer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public class EnchantingRecipe extends AbstractAetherCookingRecipe {
    public EnchantingRecipe(String group, AetherBookCategory category, Ingredient ingredient, ItemStack result, float experience, int enchantingTime) {
        super(group, category, ingredient, result, experience, enchantingTime);
    }

    @Override
    public RecipeType<? extends AbstractCookingRecipe> getType() {
        return AetherRecipeTypes.ENCHANTING.get();
    }

    @Override
    protected Item furnaceIcon() {
        return AetherBlocks.ALTAR.asItem();
    }

    @Override
    public RecipeSerializer<EnchantingRecipe> getSerializer() {
        return AetherRecipeSerializers.ENCHANTING.get();
    }

    public static class Serializer extends AetherCookingSerializer<EnchantingRecipe> {
        public Serializer() {
            super(EnchantingRecipe::new, 250);
        }
    }
}
