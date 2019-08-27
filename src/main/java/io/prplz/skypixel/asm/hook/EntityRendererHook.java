package io.prplz.skypixel.asm.hook;

import io.prplz.skypixel.Skypixel;
import net.minecraft.entity.Entity;

import java.util.List;

public class EntityRendererHook {

    private static final Skypixel skypixel = Skypixel.get();

    public static List<Entity> getMouseOverEntityList(List<Entity> list) {
        if (skypixel.isInSkyblock()) {
            list.removeIf(Skypixel.get()::shouldHideEntity);
        }
        return list;
    }
}
