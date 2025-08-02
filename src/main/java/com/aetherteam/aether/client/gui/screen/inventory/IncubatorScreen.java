package com.aetherteam.aether.client.gui.screen.inventory;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.client.gui.screen.inventory.recipebook.IncubatorRecipeBookComponent;
import com.aetherteam.aether.inventory.menu.IncubatorMenu;
import com.aetherteam.aether.item.AetherItems;
import com.aetherteam.aether.recipe.book.AetherRecipeBookCategories;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

import java.util.List;

public class IncubatorScreen extends AbstractRecipeBookScreen<IncubatorMenu> {
    private static final ResourceLocation INCUBATOR_GUI_TEXTURES = ResourceLocation.fromNamespaceAndPath(Aether.MODID, "textures/gui/menu/incubator.png");
    private static final ResourceLocation LIT_PROGRESS_TEXTURE = ResourceLocation.fromNamespaceAndPath(Aether.MODID, "menu/lit_progress");
    private static final ResourceLocation INCUBATION_PROGRESS_TEXTURE = ResourceLocation.fromNamespaceAndPath(Aether.MODID, "menu/incubation_progress");
    private static final List<RecipeBookComponent.TabInfo> TABS = List.of(
        new RecipeBookComponent.TabInfo(Items.COMPASS, AetherRecipeBookCategories.INCUBATION_SEARCH),
        new RecipeBookComponent.TabInfo(AetherItems.BLUE_MOA_EGG.get(), AetherRecipeBookCategories.INCUBATION_MISC.get()));

    public IncubatorScreen(IncubatorMenu menu, Inventory playerInventory, Component title) {
        super(menu, new IncubatorRecipeBookComponent(), playerInventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
        int left = this.getGuiLeft();
        int top = this.getGuiTop();
        guiGraphics.blit(RenderType::guiTextured, INCUBATOR_GUI_TEXTURES, left, top, 0, 0, this.getXSize(), this.getYSize(), 256, 256);
        if (this.getMenu().isIncubating()) {
            int incubationTimeRemaining = this.getMenu().getIncubationTimeRemaining() + 1;
            guiGraphics.blitSprite(RenderType::guiTextured, LIT_PROGRESS_TEXTURE, 14, 14, 0, 14 - incubationTimeRemaining, left + 74, top + 36 + 13 - incubationTimeRemaining, 14, incubationTimeRemaining);
        }
        int incubationProgressScaled = this.getMenu().getIncubationProgressScaled();
        guiGraphics.blitSprite(RenderType::guiTextured, INCUBATION_PROGRESS_TEXTURE, 10, 54, 0, 54 - incubationProgressScaled, left + 103, top + 15 + 55 - incubationProgressScaled, 10, incubationProgressScaled);
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(this.leftPos + 37, this.height / 2 - 49);
    }
}
