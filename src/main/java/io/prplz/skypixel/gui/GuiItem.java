package io.prplz.skypixel.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public abstract class GuiItem extends Gui {

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected int x = 0;
    protected int y = 0;
    protected int width = 0;
    protected int height = 0;

    public int getX() {
        return x;
    }

    public final void setX(int x) {
        setPosition(x, y);
    }

    public int getY() {
        return y;
    }

    public final void setY(int y) {
        setPosition(x, y);
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public final void setWidth(int width) {
        setSize(width, height);
    }

    public int getHeight() {
        return height;
    }

    public final void setHeight(int height) {
        setSize(width, height);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public boolean containsPoint(int x, int y) {
        return x >= this.x && x < this.x + width && y >= this.y && y < this.y + height;
    }

    /**
     * Called to draw the element to the screen
     */
    public abstract void draw(int mouseX, int mouseY, float partialTicks);

    /**
     * Called upon game tick
     */
    public void update() {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }
}
