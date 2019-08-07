package io.prplz.skypixel;

import net.minecraft.client.resources.I18n;

public enum Translation {

    KEY_CATEGORIES_SKYPIXEL,
    KEY_MENU,
    KEY_WARP_HOME,
    KEY_WARP_HUB;

    public final String key = "skypixel." + name().toLowerCase();

    public String format(Object... args) {
        return I18n.format(key, args);
    }
}
