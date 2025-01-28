package com.aetherteam.aether.integration.quark;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.client.gui.screen.inventory.AccessoriesScreen;
import com.aetherteam.aether.inventory.menu.AccessoriesMenu;
import com.aetherteam.aether.mixin.mixins.common.accessor.EntityAccessor;
import com.aetherteam.aether.network.AetherPacketHandler;
import com.aetherteam.aether.network.packet.serverbound.OpenInventoryPacket;
import com.aetherteam.aether.network.packet.serverbound.QuarkBackpackPacket;
import com.aetherteam.nitrogen.network.PacketRelay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.violetmoon.quark.addons.oddities.client.screen.BackpackInventoryScreen;
import org.violetmoon.quark.addons.oddities.inventory.BackpackMenu;
import org.violetmoon.quark.addons.oddities.item.BackpackItem;
import org.violetmoon.quark.addons.oddities.module.BackpackModule;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.network.message.oddities.HandleBackpackMessage;

public class QuarkCompat {
    public static class Common {
        public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Aether.MODID);

        public static final RegistryObject<MenuType<AccessoriesBackpackMenu>> ACCESSORIES_BACKPACK = registerMenu("accessories_backpack", AccessoriesBackpackMenu::new);

        public static void init(IEventBus modEventBus) {
            MENU_TYPES.register(modEventBus);
        }

        private static<T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenu(String name, MenuType.MenuSupplier<T> menu) {
            return MENU_TYPES.register(name, () -> new MenuType<>(menu, FeatureFlags.VANILLA_SET));
        }

        public static void executeQuarkBackpackPacketBehavior(Player playerEntity, boolean open) {
            if (playerEntity instanceof ServerPlayer serverPlayer) {
                if (open) {
                    ItemStack stack = playerEntity.getItemBySlot(EquipmentSlot.CHEST);
                    if (stack.getItem() instanceof BackpackItem && playerEntity.containerMenu != null) {
                        ItemStack holding = playerEntity.containerMenu.getCarried().copy();
                        playerEntity.containerMenu.setCarried(ItemStack.EMPTY);
                        openAccessoriesBackpack(serverPlayer);
                        playerEntity.containerMenu.setCarried(holding);
                    }
                } else {
                    if (playerEntity.containerMenu != null) {
                        ItemStack holding = playerEntity.containerMenu.getCarried();
                        playerEntity.containerMenu.setCarried(ItemStack.EMPTY);

                        BackpackMenu.saveCraftingInventory(playerEntity);
                        playerEntity.containerMenu = playerEntity.inventoryMenu;
                        playerEntity.inventoryMenu.setCarried(holding);
                    }
                }
            }
        }

        public static void openAccessoriesBackpack(ServerPlayer serverPlayer) {
            if (BackpackModule.isEntityWearingBackpack(serverPlayer)) {
                NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider((id, inventory, playerEntity) -> new AccessoriesBackpackMenu(id, inventory), Component.translatable("container.crafting")));
            } else {
                NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider((id, inventory, playerEntity) -> new AccessoriesMenu(id, inventory), Component.translatable("container.crafting")));
            }
        }
    }

    @Mod.EventBusSubscriber(modid = Aether.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientMod {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            if (ModList.get().isLoaded("quark")) {
                event.enqueueWork(() -> {
                    MenuScreens.register(Common.ACCESSORIES_BACKPACK.get(), AccessoriesBackpackScreen::new);
                });
            }
        }
    }

    @Mod.EventBusSubscriber(modid = Aether.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ClientForge {
        private static ItemStack heldStack = null;
        private static boolean backpackRequested;

        public static void requestBackpack() {
            heldStack = Minecraft.getInstance().player.inventoryMenu.getCarried();
            PacketRelay.sendToServer(AetherPacketHandler.INSTANCE, new QuarkBackpackPacket(true));
        }

        @SubscribeEvent
        public static void clientSetup(TickEvent.ClientTickEvent event) {
            if (ModList.get().isLoaded("quark")) {
                Minecraft mc = Minecraft.getInstance();
                if (isInventoryGUI(mc.screen) && !backpackRequested && BackpackModule.isEntityWearingBackpack(mc.player) && !((EntityAccessor) mc.player).aether$isIsInsidePortal() && !mc.player.isCreative()) {
                    requestBackpack();
                    mc.player.inventoryMenu.setCarried(mc.player.getItemBySlot(EquipmentSlot.CHEST));
                    backpackRequested = true;
                } else if (mc.screen instanceof BackpackInventoryScreen) {
                    if (heldStack != null) {
                        mc.player.inventoryMenu.setCarried(heldStack);
                        heldStack = null;
                    }
                    backpackRequested = false;
                }
            }
        }

        private static boolean isInventoryGUI(Screen gui) {
            return gui != null && gui.getClass() == AccessoriesScreen.class;
        }

        public static boolean isAccessoriesBackpackGUI(Screen gui) {
            return gui instanceof AccessoriesBackpackScreen;
        }

        public static boolean isAccessoriesBackpackClass(Screen gui) {
            return gui.getClass() == AccessoriesBackpackScreen.class;
        }
    }
}
