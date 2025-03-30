package com.aetherteam.aether.event.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class AetherEntityEvents {
    public static final Event<ExperienceDrop> ON_EXPERIENCE_DROP = EventFactory.createArrayBacked(ExperienceDrop.class, invokers -> (entity, attackingPlayer, helper) -> {
        for (var invoker : invokers) invoker.onExperienceDrop(entity, attackingPlayer, helper);
    });

    public interface ExperienceDrop {
        void onExperienceDrop(LivingEntity entity, @Nullable Player attackingPlayer, ExperienceDropHelper helper);
    }

    public static class ExperienceDropHelper extends CancellableCallbackImpl {
        private final int originalExperiencePoints;

        private int droppedExperiencePoints;

        public ExperienceDropHelper(int originalExperience) {
            this.originalExperiencePoints = this.droppedExperiencePoints = originalExperience;
        }

        public int getDroppedExperience() {
            return this.droppedExperiencePoints;
        }

        public void setDroppedExperience(int droppedExperience) {
            this.droppedExperiencePoints = droppedExperience;
        }

        public int getOriginalExperience() {
            return this.originalExperiencePoints;
        }

        public int getFinalExperienceAmount() {
            return this.isCanceled() ? 0 : this.getDroppedExperience();
        }
    }
}
