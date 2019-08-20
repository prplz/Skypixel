package io.prplz.skypixel.gui;

import io.prplz.skypixel.Settings;
import io.prplz.skypixel.property.Property;
import net.minecraft.util.MathHelper;

public class ChatColorPropertyPicker extends GuiItem {

    private final Property<Settings.ChatColor> property;
    private final String text;
    private final int itemSize = 12;
    private final int itemMargin = 4;
    private final int textHeight = 10;
    private final Settings.ChatColor[] values;

    public ChatColorPropertyPicker(Property<Settings.ChatColor> property, String text) {
        this.property = property;
        this.text = text;
        width = (itemSize + itemMargin) * 8;
        height = (itemSize + itemMargin) * 2 + textHeight;
        values = Settings.ChatColor.values();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        drawString(mc.fontRendererObj, text, x, y, 0xFFFFFF);
        for (int i = 0; i < values.length; i++) {
            Settings.ChatColor color = values[i];
            int x = this.x + (i % 8) * (itemSize + itemMargin);
            int y = this.y + textHeight + i / 8 * (itemSize + itemMargin);
            if (color == property.get()) {
                drawRect(x - 1, y - 1, x + itemSize + 1, y + itemSize + 1, 0xFFFFFFFF);
            }
            drawRect(x, y, x + itemSize, y + itemSize, color.color | 0xFF000000);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int itemX = MathHelper.floor_float((float) (mouseX - x) / (itemMargin + itemSize));
        int itemY = MathHelper.floor_float((float) (mouseY - y - textHeight) / (itemMargin + itemSize));
        if (itemX >= 0 && itemX < 8 && itemY >= 0 && itemY < 2) {
            int index = itemX + itemY * 8;
            property.set(values[index]);
        }
    }
}
