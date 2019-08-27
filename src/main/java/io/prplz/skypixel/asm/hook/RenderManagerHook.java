package io.prplz.skypixel.asm.hook;

import io.prplz.skypixel.Skypixel;
import net.minecraft.entity.Entity;

public class RenderManagerHook {

    private static final Skypixel skypixel = Skypixel.get();

    /**
     * @return false to cancel rendering of this entity, true to continue original shouldRender checks
     */
    public static boolean shouldRender(Entity entity) {
        if (skypixel.isInSkyblock()) {
            return !skypixel.shouldHideEntity(entity);
        }
        return true;
    }
}
