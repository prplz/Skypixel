package io.prplz.skypixel.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static List<String> getLore(ItemStack item) {
        if (!item.hasTagCompound()) {
            return null;
        }
        if (!item.getTagCompound().hasKey("display", NBTUtils.TYPE_ID_COMPOUND)) {
            return null;
        }
        NBTTagCompound display = item.getTagCompound().getCompoundTag("display");
        if (!display.hasKey("Lore", NBTUtils.TYPE_ID_LIST)) {
            return null;
        }
        NBTTagList lore = display.getTagList("Lore", NBTUtils.TYPE_ID_STRING);
        List<String> loreList = new ArrayList<>(lore.tagCount());
        for (int i = 0; i < lore.tagCount(); i++) {
            loreList.add(lore.getStringTagAt(i));
        }
        return loreList;
    }

    public static NBTTagCompound getExtraAttributes(ItemStack item) {
        if (item.hasTagCompound() && item.getTagCompound().hasKey("ExtraAttributes", NBTUtils.TYPE_ID_COMPOUND)) {
            return item.getTagCompound().getCompoundTag("ExtraAttributes");
        }
        return null;
    }
}
