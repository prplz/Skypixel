package io.prplz.skypixel;

import net.minecraft.client.resources.I18n;

public enum Translation {

    KEY_CATEGORIES_SKYPIXEL,
    KEY_MENU,
    KEY_WARP_HOME,
    KEY_WARP_HUB,

    FEATURE_ENCHANTMENT_MENU,
    FEATURE_ANVIL_USES,
    FEATURE_CANCEL_ITEM_DAMAGE,
    FEATURE_CANCEL_INVENTORY_DRAG,
    FEATURE_KEYBINDS,

    SETTING_ENABLED,
    SETTING_DISABLED,
    SETTING_ON,
    SETTING_OFF;

    public final String key = "skypixel." + name().toLowerCase();

    public String format(Object... args) {
        return I18n.format(key, args);
    }
}
