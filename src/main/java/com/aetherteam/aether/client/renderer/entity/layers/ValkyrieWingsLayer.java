package com.aetherteam.aether.client.renderer.entity.layers;

import com.aetherteam.aether.client.renderer.entity.model.ValkyrieModel;
import com.aetherteam.aether.client.renderer.entity.model.ValkyrieWingsModel;
import com.aetherteam.aether.client.renderer.entity.state.ValkyrieRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ValkyrieWingsLayer<T extends ValkyrieRenderState> extends RenderLayer<T, ValkyrieModel<T>> {
    private final ResourceLocation wingsLocation;
    private final ValkyrieWingsModel<ValkyrieRenderState> wings;

    public ValkyrieWingsLayer(RenderLayerParent<T, ValkyrieModel<T>> entityRenderer, ResourceLocation wingsLocation, ValkyrieWingsModel<ValkyrieRenderState> wingsModel) {
        super(entityRenderer);
        this.wingsLocation = wingsLocation;
        this.wings = wingsModel;
    }

    /**
     * Renders Valkyrie wings if the entity is not invisibility.
     *
     * @param poseStack       The rendering {@link PoseStack}.
     * @param buffer          The rendering {@link MultiBufferSource}.
     * @param packedLight     The {@link Integer} for the packed lighting for rendering.
     * @param renderState     The render state for the entity.
     * @param netHeadYaw      The {@link Float} for the head yaw rotation.
     * @param headPitch       The {@link Float} for the head pitch rotation.
     */
    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T renderState, float netHeadYaw, float headPitch) {
        this.setupWingRotation(renderState, renderState.ageInTicks);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(this.wingsLocation));
        if (!renderState.isInvisible) {
            this.wings.renderToBuffer(poseStack, consumer, packedLight, LivingEntityRenderer.getOverlayCoords(renderState, 0.0F));
        }
    }

    /**
     * Sets the model rotation of the wings.
     *
     * @param renderState The render state for the entity.
     * @param ticks  The {@link Float} for the entity's age in ticks.
     */
    public void setupWingRotation(T renderState, float ticks) {
        float sinage = this.handleWingSinage(renderState, ticks);
        float targetYRot = Mth.sin(sinage) / 6.0F - 0.2F;
        float targetZRot = Mth.cos(sinage) / (renderState.onGround ? 8.0F : 3.0F) - 0.125F;
        this.wings.leftWing.yRot = targetYRot;
        this.wings.leftWing.zRot = targetZRot;
        this.wings.rightWing.yRot = -targetYRot;
        this.wings.rightWing.zRot = -targetZRot;
    }

    /**
     * Handles the rotation value for the wings' rotation.
     *
     * @param renderState The render state for the entity.
     * @param sinage The {@link Float} for the entity's age in ticks.
     * @return The modified {@link Float} value.
     */
    private float handleWingSinage(T renderState, float sinage) {
        if (!renderState.onGround) {
            sinage *= 0.75F;
        } else {
            sinage *= 0.15F;
        }
        return sinage;
    }
}
