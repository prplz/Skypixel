package io.prplz.skypixel.asm.hook;

import io.prplz.skypixel.Settings;
import io.prplz.skypixel.Skypixel;
import io.prplz.skypixel.utils.ItemUtils;
import io.prplz.skypixel.utils.RomanNumerals;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RenderItemHook {

    private static final Skypixel skypixel = Skypixel.get();

    /**
     * Called at the end of {@link RenderItem#renderItemOverlayIntoGUI(FontRenderer, ItemStack, int, int, String)}
     */
    public static void renderItemOverlayIntoGUI(ItemStack item, int x, int y) {
        if (!skypixel.isInSkyblock() || item == null || item.stackSize != 1) {
            return;
        }
        NBTTagCompound skyblockData = ItemUtils.getExtraAttributes(item);
        if (skyblockData == null) {
            return;
        }
        int tier = 0;
        String id = skyblockData.getString("id");
        if (skypixel.getSettings().potionItemTierEnabled.get() && id.equals("POTION")) {
            tier = skyblockData.getInteger("potion_level");
        } else if (skypixel.getSettings().armorItemTierEnabled.get() && id.startsWith("PERFECT_")) {
            String[] parts = id.split("_");
            if (parts.length == 3) {
                try {
                    tier = Integer.parseInt(parts[2]);
                } catch (NumberFormatException ignored) {
                }
            }
        } else if (skypixel.getSettings().minionItemTierEnabled.get() && id.contains("_GENERATOR_")) {
            String[] parts = id.split("_");
            try {
                tier = Integer.parseInt(parts[parts.length - 1]);
            } catch (NumberFormatException ignored) {
            }
        }
        if (tier > 0) {
            String text;
            if (skypixel.getSettings().itemTierType.get() == Settings.NumeralType.ROMAN) {
                text = RomanNumerals.get(tier);
            } else {
                text = Integer.toString(tier);
            }
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableBlend();
            skypixel.getRomanNumeralsFont().drawStringWithShadow(text, x + 17 - skypixel.getRomanNumeralsFont().getStringWidth(text), y + 17 - 8, skypixel.getSettings().itemTierColor.get().color);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        }
    }
}
