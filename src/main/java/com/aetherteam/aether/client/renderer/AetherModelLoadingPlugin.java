package com.aetherteam.aether.client.renderer;

import com.aetherteam.aether.Aether;
import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.client.renderer.block.FastModel;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;

public class AetherModelLoadingPlugin implements ModelLoadingPlugin {
    @Override
    public void onInitializeModelLoader(Context pluginContext) {
        pluginContext.modifyModelAfterBake().register((original, context) -> {
            if (context.id().getNamespace().equals(Aether.MODID)) {
                String path = context.id().getPath();
                if (path.equals(AetherBlocks.BERRY_BUSH.getId().getPath())) {
                    return new FastModel(original);
                } else if (path.equals(AetherBlocks.POTTED_BERRY_BUSH.getId().getPath())) {
                    return new FastModel(original);
                }
            }
            return original;
        });
    }
}
