package com.aetherteam.aether.integration.quark;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.AetherConfig;
import com.aetherteam.aether.client.AetherKeys;
import com.aetherteam.aether.client.gui.component.inventory.AccessoryButton;
import com.aetherteam.aether.client.gui.screen.inventory.AccessoriesScreen;
import com.aetherteam.aether.client.gui.screen.inventory.RecipeBookBehavior;
import com.aetherteam.aether.client.gui.screen.perks.AetherCustomizationsScreen;
import com.aetherteam.aether.client.gui.screen.perks.MoaSkinsScreen;
import com.aetherteam.aether.inventory.menu.AccessoriesMenu;
import com.aetherteam.aether.mixin.mixins.client.accessor.ScreenAccessor;
import com.aetherteam.aether.network.AetherPacketHandler;
import com.aetherteam.aether.network.packet.serverbound.ClearItemPacket;
import com.aetherteam.aether.perk.PerkUtil;
import com.aetherteam.nitrogen.api.users.User;
import com.aetherteam.nitrogen.api.users.UserData;
import com.aetherteam.nitrogen.network.PacketRelay;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import org.violetmoon.quark.addons.oddities.client.screen.BackpackInventoryScreen;
import org.violetmoon.quark.addons.oddities.inventory.BackpackMenu;
import org.violetmoon.quark.addons.oddities.module.BackpackModule;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.network.message.oddities.HandleBackpackMessage;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.client.gui.CuriosButton;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.client.gui.RenderButton;
import top.theillusivec4.curios.common.inventory.CosmeticCurioSlot;
import top.theillusivec4.curios.common.inventory.CurioSlot;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.client.CPacketDestroy;
import top.theillusivec4.curios.common.network.client.CPacketToggleRender;

import javax.annotation.Nullable;

public class AccessoriesBackpackScreen extends BackpackInventoryScreen {
    public static final ResourceLocation SKINS_BUTTON = new ResourceLocation(Aether.MODID, "textures/gui/perks/skins/skins_button.png");
    public static final ResourceLocation CUSTOMIZATION_BUTTON = new ResourceLocation(Aether.MODID, "textures/gui/perks/customization/customization_button.png");
    public static final ResourceLocation RECIPE_BUTTON = new ResourceLocation("textures/gui/recipe_button.png");

    private static final ResourceLocation ACCESSORIES_INVENTORY = new ResourceLocation(Aether.MODID, "textures/gui/inventory/quark_backpack_accessories.png");
    private static final ResourceLocation ACCESSORIES_INVENTORY_CREATIVE = new ResourceLocation(Aether.MODID, "textures/gui/inventory/quark_backpack_accessories_creative.png");
    private static final ResourceLocation CURIO_INVENTORY = new ResourceLocation(Curios.MODID, "textures/gui/inventory.png");

    private static final SimpleContainer DESTROY_ITEM_CONTAINER = new SimpleContainer(1);
    private boolean widthTooNarrow;
    private boolean isRenderButtonHovered;
    @Nullable
    private Slot destroyItemSlot;
    private final Player player;
    private boolean closeHack = false;
    private boolean buttonClicked;

    public AccessoriesBackpackScreen(InventoryMenu backpack, Inventory inventory, Component component) {
        super(backpack, inventory, component);
        this.player = inventory.player;
    }

    @Override
    public void init() {
        super.init();
        this.clearWidgets();

        // Basic Curio-based initialization.
        if (this.getMinecraft().player != null) {
            this.imageWidth = this.getMinecraft().player.isCreative() ? 176 + this.creativeXOffset() : 176;
        }
        this.widthTooNarrow = this.width < 379;

        if (this.getMinecraft().player != null && this.getRecipeBookComponent().isVisible()) {
//            this.getRecipeBookComponent().toggleVisibility();
            this.updateScreenPosition();
        }

        this.addRenderableWidget(new ImageButton(this.getGuiLeft() + 136, this.height / 2 - 22, 20, 18, 0, 0, 19, RECIPE_BUTTON, (pressed) -> {
            this.getRecipeBookComponent().toggleVisibility();
            this.updateScreenPosition();
            pressed.setPosition(this.getGuiLeft() + 136, this.height / 2 - 22);
            this.buttonClicked = true;
        }));

        this.updateRenderButtons();

        // Create perk-related buttons.
        ImageButton skinsButton = this.createSkinsButton();
        this.addRenderableWidget(skinsButton);

        User user = UserData.Client.getClientUser();
        if (user != null && (PerkUtil.hasDeveloperGlow().test(user) || PerkUtil.hasHalo().test(user))) {
            ImageButton customizationButton = this.createCustomizationButton();
            this.addRenderableWidget(customizationButton);
        }
    }

    /**
     * [CODE COPY] - {@link CuriosScreen#updateScreenPosition()}.<br>
     * [CODE COPY] - {@link RecipeBookComponent#updateScreenPosition(int, int)}.
     */
    private void updateScreenPosition() {
        int i;
        if (this.getRecipeBookComponent().isVisible() && !this.widthTooNarrow) {
            int offset = 200 - this.creativeXOffset();
            i = 177 + (this.width - this.getXSize() - offset) / 2;
        } else {
            i = (this.width - this.getXSize()) / 2;
        }
        this.leftPos = i;
        this.updateRenderButtons();
    }

    /**
     * Creates the button for the {@link MoaSkinsScreen}.
     * @return The {@link ImageButton}.
     */
    private ImageButton createSkinsButton() {
        ImageButton skinsButton = new ImageButton(this.getGuiLeft() - 22, this.getGuiTop() + 2, 20, 20, 0, 0, 20, SKINS_BUTTON, 20, 40,
                (pressed) -> this.getMinecraft().setScreen(new MoaSkinsScreen(this)),
                Component.translatable("gui.aether.accessories.skins_button")) {
            @Override
            public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                super.render(guiGraphics, mouseX, mouseY, partialTick);
                if (!AccessoriesBackpackScreen.this.getRecipeBookComponent().isVisible()) {
                    this.setX(AccessoriesBackpackScreen.this.getGuiLeft() - 22);
                    this.setY(AccessoriesBackpackScreen.this.getGuiTop() + 2);
                } else {
                    this.setX(AccessoriesBackpackScreen.this.getGuiLeft() + 2);
                    this.setY(AccessoriesBackpackScreen.this.getGuiTop() - 22);
                }
            }
        };
        skinsButton.setTooltip(Tooltip.create(Component.translatable("gui.aether.accessories.skins_button")));
        return skinsButton;
    }

    /**
     * Creates the button for the {@link AetherCustomizationsScreen}.
     * @return The {@link ImageButton}.
     */
    private ImageButton createCustomizationButton() {
        ImageButton customizationButton = new ImageButton(this.getGuiLeft() - 22, this.getGuiTop() + 24, 20, 20, 0, 0, 20, CUSTOMIZATION_BUTTON, 20, 40,
                (pressed) -> this.getMinecraft().setScreen(new AetherCustomizationsScreen(this)),
                Component.translatable("gui.aether.accessories.customization_button")) {
            @Override
            public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                super.render(guiGraphics, mouseX, mouseY, partialTick);
                if (!AccessoriesBackpackScreen.this.getRecipeBookComponent().isVisible()) {
                    this.setX(AccessoriesBackpackScreen.this.getGuiLeft() - 22);
                    this.setY(AccessoriesBackpackScreen.this.getGuiTop() + 24);
                } else {
                    this.setX(AccessoriesBackpackScreen.this.getGuiLeft() + 24);
                    this.setY(AccessoriesBackpackScreen.this.getGuiTop() - 22);
                }
            }
        };
        customizationButton.setTooltip(Tooltip.create(Component.translatable("gui.aether.accessories.customization_button")));
        return customizationButton;
    }

    private void updateRenderButtons() {
        ScreenAccessor screenAccessor = (ScreenAccessor) this;
        screenAccessor.aether$getNarratables().removeIf(widget -> widget instanceof RenderButton);
        this.children().removeIf(widget -> widget instanceof RenderButton);
        this.renderables.removeIf(widget -> widget instanceof RenderButton);
        for (Slot inventorySlot : this.getMenu().slots) {
            if (inventorySlot instanceof CurioSlot curioSlot && !(inventorySlot instanceof CosmeticCurioSlot)) {
                this.addRenderableWidget(new RenderButton(curioSlot, this.getGuiLeft() + inventorySlot.x + 11, this.getGuiTop() + inventorySlot.y - 3, 8, 8, 75, 0, 8, CURIO_INVENTORY,
                        (button) -> NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketToggleRender(curioSlot.getIdentifier(), inventorySlot.getSlotIndex()))) {
                    @Override
                    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
                        this.setX(AccessoriesBackpackScreen.this.getGuiLeft() + inventorySlot.x + 11);
                        this.setY(AccessoriesBackpackScreen.this.getGuiTop() + inventorySlot.y - 3);
                    }
                });
            }
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderables.removeIf(renderable -> renderable instanceof CuriosButton);

        if (!this.getRecipeBookComponent().isVisible() || !this.widthTooNarrow) {
            super.render(guiGraphics, mouseX, mouseY, partialTicks);

            boolean isButtonHovered = false;
            for (Renderable renderable : this.renderables) {
                if (renderable instanceof RenderButton renderButton) {
                    renderButton.renderButtonOverlay(guiGraphics, mouseX, mouseY, partialTicks);
                    if (renderButton.isHovered()) {
                        isButtonHovered = true;
                    }
                }
            }
            this.isRenderButtonHovered = isButtonHovered;
            LocalPlayer clientPlayer = Minecraft.getInstance().player;
            if (!this.isRenderButtonHovered && clientPlayer != null && clientPlayer.inventoryMenu.getCarried().isEmpty() && this.getSlotUnderMouse() != null) {
                Slot slot = this.getSlotUnderMouse();
                if (slot instanceof CurioSlot curioSlot && !slot.hasItem()) {
                    guiGraphics.renderTooltip(this.font, Component.literal(curioSlot.getSlotName()), mouseX, mouseY);
                }
            }

            if (this.getMinecraft().player != null) {
                if (this.getMinecraft().player.isCreative() && this.destroyItemSlot == null) {
                    this.destroyItemSlot = new Slot(DESTROY_ITEM_CONTAINER, 0, 172, 142);
                    this.getMenu().slots.add(this.destroyItemSlot);
                } else if (!this.getMinecraft().player.isCreative() && this.destroyItemSlot != null) {
                    this.getMenu().slots.remove(this.destroyItemSlot);
                    this.destroyItemSlot = null;
                }
            }

            if (this.destroyItemSlot != null && this.isHovering(this.destroyItemSlot.x, this.destroyItemSlot.y, 16, 16, mouseX, mouseY)) {
                guiGraphics.renderTooltip(this.font, Component.translatable("inventory.binSlot"), mouseX, mouseY);
            }

            if (this.getMinecraft().player != null) {
                this.imageWidth = this.getMinecraft().player.isCreative() ? 176 + this.creativeXOffset() : 176;
            }
        } else {
            super.render(guiGraphics, mouseX, mouseY, partialTicks);
        }
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        if (this.getMinecraft().player != null) {
            int i = this.getGuiLeft();
            int j = this.getGuiTop();
            guiGraphics.blit(this.getMinecraft().player.isCreative() ? ACCESSORIES_INVENTORY_CREATIVE : ACCESSORIES_INVENTORY, i, j, 0, 0, this.getXSize() + this.creativeXOffset(), this.getYSize());
            InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, i + 33, j + 75, 30, (float) (i + 31) - mouseX, (float) (j + 75 - 50) - mouseY, this.getMinecraft().player);
            this.moveCharmsButtons();
        }
    }

    private void moveCharmsButtons() {
        for (Renderable renderable : this.renderables) {
            // Charms buttons have a static Y pos, so use that to only focus on them.
            if (renderable instanceof ImageButton img) {
                if (img.getY() == this.height / 2 - 22) {
                    img.setPosition(img.getX(), img.getY() - 29);
                }
            }
        }
    }

    /**
     * @return The {@link Integer} y-offset for the GUI.
     */
    private int creativeXOffset() {
        return this.getMinecraft().player != null && this.getMinecraft().player.isCreative() ? 18 : 0;
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Minecraft minecraft = this.getMinecraft();
        LocalPlayer clientPlayer = minecraft.player;
        if (clientPlayer != null && clientPlayer.inventoryMenu.getCarried().isEmpty()) {
            if (this.isRenderButtonHovered) {
                guiGraphics.renderTooltip(this.font, Component.translatable("gui.curios.toggle"), mouseX, mouseY);
            } else if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
                guiGraphics.renderTooltip(this.font, this.hoveredSlot.getItem(), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (this.getMinecraft().player != null) {
            guiGraphics.drawString(this.font, this.title, 115, 6, 4210752, false);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
//        if (this.getRecipeBookComponent().isVisible() && this.widthTooNarrow) {
//            this.getRecipeBookComponent().toggleVisibility();
//            this.updateScreenPosition();
//            return true;
//        } else
        if (AetherKeys.OPEN_ACCESSORY_INVENTORY.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode))) {
            LocalPlayer playerEntity = this.getMinecraft().player;
            if (playerEntity != null) {
                playerEntity.closeContainer();
            }
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    protected boolean isHovering(int rectX, int rectY, int rectWidth, int rectHeight, double pointX, double pointY) {
        if (this.isRenderButtonHovered) {
            return false;
        }
        return super.isHovering(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
    }

    /**
     * [CODE COPY] {@link net.minecraft.client.gui.screens.inventory.AbstractContainerScreen}.<br><br>
     * Heavily modified to only have behavior for the item trash slot.
     */
    @Override
    protected void slotClicked(@Nullable Slot slot, int slotId, int mouseButton, ClickType type) {
        this.getRecipeBookComponent().slotClicked(slot);
        if (this.getMinecraft().player != null && this.getMinecraft().gameMode != null) {
            boolean flag = type == ClickType.QUICK_MOVE;
            if (slot != null || type == ClickType.QUICK_CRAFT) {
                if (slot == null || slot.mayPickup(this.getMinecraft().player)) {
                    if (slot == this.destroyItemSlot && this.destroyItemSlot != null && flag) {
                        for (int j = 0; j < this.getMinecraft().player.inventoryMenu.getItems().size(); ++j) {
                            this.getMinecraft().gameMode.handleCreativeModeItemAdd(ItemStack.EMPTY, j);
                            NetworkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), new CPacketDestroy());
                        }
                    } else {
                        if (slot == this.destroyItemSlot && this.destroyItemSlot != null) {
                            this.getMenu().setCarried(ItemStack.EMPTY);
                            PacketRelay.sendToServer(AetherPacketHandler.INSTANCE, new ClearItemPacket(this.getMinecraft().player.getId()));
                        }
                    }
                }
            }
            if (slot != null) {
                slotId = slot.index;
            }
            this.minecraft.gameMode.handleInventoryMouseClick(this.menu.containerId, slotId, mouseButton, type, this.minecraft.player);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.buttonClicked) {
            this.buttonClicked = false;
            return true;
        } else {
            return super.mouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public void containerTick() {
        super.containerTick();

        if (!BackpackModule.isEntityWearingBackpack(this.player)) {
            ItemStack curr = this.player.containerMenu.getCarried();
            BackpackMenu.saveCraftingInventory(this.player);
            this.closeHack = true;
            QuarkClient.ZETA_CLIENT.sendToServer(new HandleBackpackMessage(false));
            this.minecraft.setScreen(new AccessoriesScreen(new AccessoriesMenu(player.containerMenu.containerId, player.getInventory()), this.player.getInventory(), this.title));
            this.player.inventoryMenu.setCarried(curr);
        }
    }

    @Override
    public boolean canSeeEffects() {
        int i = this.getGuiLeft() + this.getXSize() + 2 + this.creativeXOffset();
        int j = this.width - i;
        return j > 13;
    }

    @Override
    public void removed() {
        if (this.closeHack) {
            this.closeHack = false;
            return;
        }
        super.removed();
    }
}
