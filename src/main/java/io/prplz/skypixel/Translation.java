package io.prplz.skypixel;

import net.minecraft.client.resources.I18n;

public enum Translation {

    FEATURE_ANVIL_USES,
    FEATURE_CANCEL_INVENTORY_DRAG,
    FEATURE_CANCEL_ITEM_DAMAGE,
    FEATURE_ENCHANTMENT_MENU,
    FEATURE_ITEM_TIER,
    FEATURE_ITEM_TIER_ARMOR,
    FEATURE_ITEM_TIER_MINION,
    FEATURE_ITEM_TIER_POTION,
    FEATURE_KEYBINDS,
    FEATURE_NUMERAL_TYPE,
    FEATURE_TEXT_COLOR,
    KEY_CATEGORIES_SKYPIXEL,
    KEY_MENU,
    KEY_WARP_HOME,
    KEY_WARP_HUB,
    SETTING_DISABLED,
    SETTING_ENABLED,
    SETTING_OFF,
    SETTING_ON;

    public final String key = "skypixel." + name().toLowerCase();

    public String format(Object... args) {
        return I18n.format(key, args);
    }
}
