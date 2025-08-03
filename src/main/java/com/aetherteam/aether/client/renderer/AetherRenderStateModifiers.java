package com.aetherteam.aether.client.renderer;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.attachment.AetherDataAttachments;
import com.aetherteam.aether.client.renderer.accessory.layer.ArmorStandCapeLayer;
import com.aetherteam.aether.item.EquipmentUtil;
import com.aetherteam.aether.item.accessories.cape.CapeItem;
import com.aetherteam.aether.mixin.AetherMixinHooks;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.AccessoriesContainer;
import io.wispforest.accessories.api.slot.SlotTypeReference;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;

import java.util.UUID;

public class AetherRenderStateModifiers {
    public static ContextKey<UUID> UUID = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(Aether.MODID, "uuid"));
    public static ContextKey<Boolean> HAS_VEHICLE = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(Aether.MODID, "has_vehicle"));
    public static ContextKey<Boolean> ON_GROUND = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(Aether.MODID, "on_ground"));
    public static ContextKey<Boolean> HAS_VEHICLE_ON_GROUND = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(Aether.MODID, "has_vehicle_on_ground"));
    public static ContextKey<Boolean> IN_FLUID_TYPE = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(Aether.MODID, "in_fluid_type"));
    public static ContextKey<Boolean> HAS_FULL_VALKYRIE_ARMOR = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(Aether.MODID, "has_full_valkyrie_armor"));
    public static ContextKey<Integer> VALKYRIE_WING_ROTATION = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(Aether.MODID, "valkyrie_wing_rotation"));
    public static ContextKey<Integer> VALKYRIE_WING_ROTATION_OLD = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(Aether.MODID, "valkyrie_wing_rotation_old"));
    public static ContextKey<Boolean> HAS_INVISIBILITY_CLOAK = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(Aether.MODID, "has_invisibility_cloak"));

    public static ContextKey<ItemStack> IS_CAPE_VISIBLE = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(Aether.MODID, "is_cape_visible"));
    public static ContextKey<ResourceLocation> CAPE_TEXTURE = new ContextKey<>(ResourceLocation.fromNamespaceAndPath(Aether.MODID, "cape_texture"));

    public static void registerRenderStateModifier(RegisterRenderStateModifiersEvent event) {
        event.registerEntityModifier(PlayerRenderer.class, (abstractClientPlayer, playerRenderState) -> {
            playerRenderState.setRenderData(UUID, abstractClientPlayer.getUUID());
            playerRenderState.setRenderData(HAS_VEHICLE, abstractClientPlayer.getVehicle() != null);
            playerRenderState.setRenderData(ON_GROUND, abstractClientPlayer.onGround());
            playerRenderState.setRenderData(HAS_VEHICLE_ON_GROUND, abstractClientPlayer.getVehicle() != null && abstractClientPlayer.getVehicle().onGround());
            playerRenderState.setRenderData(IN_FLUID_TYPE, abstractClientPlayer.isInFluidType());
            playerRenderState.setRenderData(HAS_FULL_VALKYRIE_ARMOR, EquipmentUtil.hasFullValkyrieSet(abstractClientPlayer));
            playerRenderState.setRenderData(VALKYRIE_WING_ROTATION, abstractClientPlayer.getData(AetherDataAttachments.AETHER_PLAYER).getWingRotation());
            playerRenderState.setRenderData(VALKYRIE_WING_ROTATION_OLD, abstractClientPlayer.getData(AetherDataAttachments.AETHER_PLAYER).getWingRotationO());
            playerRenderState.setRenderData(HAS_INVISIBILITY_CLOAK, abstractClientPlayer.getData(AetherDataAttachments.AETHER_PLAYER).isWearingInvisibilityCloak());
            playerRenderState.setRenderData(IS_CAPE_VISIBLE, AetherMixinHooks.isCapeVisible(abstractClientPlayer));
        });

        event.registerEntityModifier(ArmorStandRenderer.class, (armorStand, armorStandRenderState) -> {
            armorStandRenderState.setRenderData(IS_CAPE_VISIBLE, AetherMixinHooks.isCapeVisible(armorStand));

            ResourceLocation texture = null;
            SlotTypeReference identifier = CapeItem.getStaticIdentifier();
            AccessoriesCapability accessories = AccessoriesCapability.get(armorStand);
            if (accessories != null) {
                AccessoriesContainer accessoriesContainer = accessories.getContainer(identifier);
                if (accessoriesContainer != null) {
                    ItemStack itemStack = accessoriesContainer.getAccessories().getItem(0);
                    if (!itemStack.isEmpty()) {
                        if (itemStack.getItem() instanceof CapeItem capeItem) {
                            texture = capeItem.getCapeTexture();
                            if (itemStack.getHoverName().getString().equalsIgnoreCase("swuff_'s cape")) { // Easter Egg cape texture.
                                texture = ArmorStandCapeLayer.SWUFF_CAPE_LOCATION;
                            }
                        }
                    }
                }
            }
            armorStandRenderState.setRenderData(CAPE_TEXTURE, texture);
        });
    }
}
