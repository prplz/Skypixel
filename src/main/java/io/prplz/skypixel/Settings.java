package io.prplz.skypixel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.prplz.skypixel.property.Property;
import io.prplz.skypixel.property.PropertyAdapterFactory;
import net.minecraft.client.resources.I18n;

public class Settings {

    public static final Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new PropertyAdapterFactory())
            .setPrettyPrinting()
            .create();

    public Property<Boolean> enchantmentGuiEnabled = new Property<>(true);
    public Property<Boolean> anvilUsesEnabled = new Property<>(true);
    public Property<AnvilUsesPosition> anvilUsesPosition = new Property<>(AnvilUsesPosition.TOP);
    public Property<ChatColor> anvilUsesColor = new Property<>(ChatColor.GRAY);
    public Property<Boolean> cancelItemDamage = new Property<>(true);
    public Property<Boolean> cancelInventoryDrag = new Property<>(true);
    public Property<Boolean> minionItemTierEnabled = new Property<>(true);
    public Property<Boolean> potionItemTierEnabled = new Property<>(true);
    public Property<Boolean> armorItemTierEnabled = new Property<>(true);
    public Property<ChatColor> itemTierColor = new Property<>(ChatColor.WHITE);
    public Property<NumeralType> itemTierType = new Property<>(NumeralType.ROMAN);
    public Property<Boolean> forgeClientCommandFix = new Property<>(true);


    @SuppressWarnings("unused")
    public enum AnvilUsesPosition {
        TOP,
        BOTTOM;

        @Override
        public String toString() {
            return I18n.format("skypixel.position_" + name().toLowerCase());
        }
    }

    @SuppressWarnings("unused")
    public enum NumeralType {
        ROMAN, ARABIC;

        @Override
        public String toString() {
            return I18n.format("skypixel.numeral_type_" + name().toLowerCase());
        }
    }

    @SuppressWarnings("unused")
    public enum ChatColor {
        BLACK('0', 0x0000000),
        DARK_BLUE('1', 0x0000aa),
        DARK_GREEN('2', 0x00aa00),
        DARK_AQUA('3', 0x00aaaa),
        DARK_RED('4', 0xaa0000),
        DARK_PURPLE('5', 0xaa00aa),
        GOLD('6', 0xffaa00),
        GRAY('7', 0xaaaaaa),
        DARK_GRAY('8', 0x555555),
        BLUE('9', 0x5555ff),
        GREEN('a', 0x55ff55),
        AQUA('b', 0x55ffff),
        RED('c', 0xff5555),
        LIGHT_PURPLE('d', 0xff55ff),
        YELLOW('e', 0xffff55),
        WHITE('f', 0xffffff);

        public final char code;
        public final int color;
        public final String controlString;

        ChatColor(char code, int color) {
            this.code = code;
            this.color = color;
            controlString = "\u00a7" + code;
        }
    }
}
