package com.aetherteam.aether.network.packet.serverbound;

import com.aetherteam.aether.integration.quark.QuarkCompat;
import com.aetherteam.nitrogen.network.BasePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;

public record QuarkBackpackPacket(boolean open) implements BasePacket {
    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.open());
    }

    public static QuarkBackpackPacket decode(FriendlyByteBuf buf) {
        boolean open = buf.readBoolean();
        return new QuarkBackpackPacket(open);
    }

    @Override
    public void execute(@Nullable Player playerEntity) {
        if (ModList.get().isLoaded("quark")) {
            QuarkCompat.Common.executeQuarkBackpackPacketBehavior(playerEntity, this.open());
        }
    }
}
