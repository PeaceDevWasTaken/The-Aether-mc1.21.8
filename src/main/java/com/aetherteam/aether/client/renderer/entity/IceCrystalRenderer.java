package com.aetherteam.aether.client.renderer.entity;

import com.aetherteam.aether.entity.projectile.crystal.AbstractCrystal;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class IceCrystalRenderer<T extends AbstractCrystal> extends CloudCrystalRenderer<T> {
    public IceCrystalRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 0.25, 0);
        super.render(renderState, poseStack, buffer, packedLight);
        poseStack.popPose();
    }
}
