package io.prplz.skypixel.gui;

import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

import java.util.List;

public class Label extends GuiItem {

    private IChatComponent text;
    private int lineHeight = 10;
    private List<IChatComponent> lines;

    public Label(IChatComponent text) {
        setText(text);
    }

    public static Label of(String text) {
        return new Label(new ChatComponentText(text));
    }

    public IChatComponent getText() {
        return text;
    }

    public void setText(IChatComponent text) {
        this.text = text;
        initLines();
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        initLines();
    }

    private void initLines() {
        int wrapWidth = Math.max(width, 20); // Infinite loop if wrapping width is too small
        lines = GuiUtilRenderComponents.splitText(text, wrapWidth, mc.fontRendererObj, false, true);
        height = lineHeight * lines.size();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        for (int line = 0; line < lines.size(); line++) {
            drawString(mc.fontRendererObj, lines.get(line).getFormattedText(), x, y + lineHeight * line, 0xFFFFFF);
        }
    }
}
