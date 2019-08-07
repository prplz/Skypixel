package io.prplz.skypixel.asm.hook;

import io.prplz.skypixel.Skypixel;

public class ItemStackHook {

    /**
     * @return true to cancel item damage, false to proceed as normal
     */
    public static boolean damageItem() {
        return Skypixel.get().isInSkyblock();
    }
}
