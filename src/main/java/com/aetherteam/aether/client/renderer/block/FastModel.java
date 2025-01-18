package com.aetherteam.aether.client.renderer.block;

import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.model.ForwardingBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class FastModel extends ForwardingBakedModel {
    private static final RenderMaterial MATERIAL_SOLID = RendererAccess.INSTANCE.getRenderer().materialFinder().blendMode(BlendMode.SOLID).find();
    private static final RenderMaterial MATERIAL_CUTOUT = RendererAccess.INSTANCE.getRenderer().materialFinder().blendMode(BlendMode.CUTOUT).find();

    public FastModel(BakedModel owner) {
        this.wrapped = owner;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        context.pushTransform(quad -> {
            quad.material(Minecraft.useFancyGraphics() ? MATERIAL_CUTOUT : MATERIAL_SOLID);
            return true;
        });
        super.emitBlockQuads(blockView, state, pos, randomSupplier, context);
        context.popTransform();
    }
}
