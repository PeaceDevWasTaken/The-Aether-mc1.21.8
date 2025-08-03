package com.aetherteam.aether.client.renderer.player.layer;

import com.aetherteam.aether.entity.projectile.dart.AbstractDart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.ArrowModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

public class DartLayer<M extends PlayerModel> extends RenderLayer<PlayerRenderState, M> {
    private final ContextKey<Integer> dartCount;
    private final Model model;
    private final ResourceLocation texture;
    private final float offset;

    public DartLayer(LivingEntityRenderer<?, PlayerRenderState, M> renderer, EntityModelSet modelSet, ContextKey<Integer> dartCount, ResourceLocation texture, float offset) {
        super(renderer);
        this.dartCount = dartCount;
        this.model = new ArrowModel(modelSet.bakeLayer(ModelLayers.ARROW));
        this.texture = texture;
        this.offset = offset;
    }

    /**
     * [CODE COPY] - {@link StuckInBodyLayer#render(PoseStack, MultiBufferSource, int, LivingEntity, float, float)}.
     */
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, PlayerRenderState renderState, float netHeadYaw, float headPitch) {
        int i = this.numStuck(renderState);
        float offset = this.offset;
        RandomSource randomSource = RandomSource.create((long) (renderState.id * (0.25 * offset)));
        if (i > 0) {
            for(int j = 0; j < i; ++j) {
                poseStack.pushPose();
                ModelPart modelpart = this.getParentModel().getRandomBodyPart(randomSource);
                ModelPart.Cube modelpart$cube = modelpart.getRandomCube(randomSource);
                modelpart.translateAndRotate(poseStack);
                float f = randomSource.nextFloat();
                float f1 = randomSource.nextFloat();
                float f2 = randomSource.nextFloat();
                int k = randomSource.nextInt(3);
                switch (k) {
                    case 0 -> f = snapToFace(f);
                    case 1 -> f1 = snapToFace(f1);
                    default -> f2 = snapToFace(f2);
                }

                poseStack.translate(Mth.lerp(f, modelpart$cube.minX, modelpart$cube.maxX) / 16.0F, Mth.lerp(f1, modelpart$cube.minY, modelpart$cube.maxY) / 16.0F, Mth.lerp(f2, modelpart$cube.minZ, modelpart$cube.maxZ) / 16.0F);
                this.renderStuckItem(poseStack, buffer, packedLight, -(f * 2.0F - 1.0F), -(f1 * 2.0F - 1.0F), -(f2 * 2.0F - 1.0F));
                poseStack.popPose();
            }
        }
    }

    private static float snapToFace(float value) {
        return value > 0.5F ? 1.0F : 0.5F;
    }

    /**
     * [CODE COPY] - {@link net.minecraft.client.renderer.entity.layers.ArrowLayer#renderStuckItem(PoseStack, MultiBufferSource, int, Entity, float, float, float, float)}.
     * Adapted for {@link AbstractDart}.
     */
    private void renderStuckItem(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, float x, float y, float z) {
        float f = Mth.sqrt(x * x + z * z);
        float f1 = (float) (Math.atan2(x, z) * 180.0 / Math.PI);
        float f2 = (float) (Math.atan2(y, f) * 180.0 / Math.PI);
        poseStack.mulPose(Axis.YP.rotationDegrees(f1 - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(f2));
        this.model.renderToBuffer(poseStack, bufferSource.getBuffer(this.model.renderType(this.texture)), packedLight, OverlayTexture.NO_OVERLAY);
    }

    protected int numStuck(PlayerRenderState playerRenderState) {
        return playerRenderState.getRenderDataOrDefault(this.dartCount, 0);
    }
}
