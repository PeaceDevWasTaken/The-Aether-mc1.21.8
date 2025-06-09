package com.aetherteam.aether.recipe.recipes.item;

import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.recipe.book.AetherBookCategory;
import com.aetherteam.aether.recipe.AetherRecipeSerializers;
import com.aetherteam.aether.recipe.AetherRecipeTypes;
import com.aetherteam.aether.recipe.serializer.AetherCookingSerializer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class FreezingRecipe extends AbstractAetherCookingRecipe {
    public FreezingRecipe(String group, AetherBookCategory category, Ingredient ingredient, ItemStack result, float experience, int freezingTime) {
        super(group, category, ingredient, result, experience, freezingTime);
    }

    @Override
    public RecipeType<? extends AbstractCookingRecipe> getType() {
        return AetherRecipeTypes.FREEZING.get();
    }
    @Override
    protected Item furnaceIcon() {
        return AetherBlocks.FREEZER.asItem();
    }

    @Override
    public RecipeSerializer<FreezingRecipe> getSerializer() {
        return AetherRecipeSerializers.FREEZING.get();
    }

    public static class Serializer extends AetherCookingSerializer<FreezingRecipe> {
        public Serializer() {
            super(FreezingRecipe::new, 800);
        }
    }
}
