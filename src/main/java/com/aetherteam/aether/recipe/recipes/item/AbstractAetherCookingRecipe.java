package com.aetherteam.aether.recipe.recipes.item;

import com.aetherteam.aether.recipe.book.AetherBookCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

public abstract class AbstractAetherCookingRecipe extends AbstractCookingRecipe {
    private final AetherBookCategory category;

    public AbstractAetherCookingRecipe(String group, AetherBookCategory category, Ingredient ingredient, ItemStack result, float experience, int cookingTime) {
        super(group, CookingBookCategory.MISC, ingredient, result, experience, cookingTime);
        this.category = category;
    }

    @Override
    public ItemStack result() {
        return super.result();
    }

    public AetherBookCategory aetherCategory() {
        return this.category;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() { //todo ??
        return null;
    }
}
