package io.prplz.skypixel.asm.hook;

import io.prplz.skypixel.Skypixel;

public class TileEntityEndPortalRendererHook {

    private static final Skypixel skypixel = Skypixel.get();

    /**
     * @return true to cancel rendering tile entity, false to proceed as normal
     */
    public static boolean renderTileEntityAt() {
        return skypixel.isInSkyblock() && skypixel.getSettings().hideEndPortals.get();
    }
}
