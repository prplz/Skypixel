package io.prplz.skypixel.gui;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public abstract class Pane extends GuiItem {

    protected int contentHeight = 0;
    protected float scrollPosition = 0;
    protected int scrollbarTicks = 0;
    protected List<GuiItem> elements = new ArrayList<>();

    @Override
    public void setSize(int width, int height) {
        clampScrollPosition();
        super.setSize(width, height);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution res = new ScaledResolution(mc);
        double scaleW = mc.displayWidth / res.getScaledWidth_double();
        double scaleH = mc.displayHeight / res.getScaledHeight_double();
        drawRect(x, y, x + width, y + height, 0x80000000);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (x * scaleW), (int) (mc.displayHeight - (y + height) * scaleH), (int) (width * scaleW), (int) (height * scaleH));
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y - (int) scrollPosition, 0);
        drawContents(mouseX - x, mouseY + (int) scrollPosition - y, partialTicks);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        if (scrollbarTicks > 0) {
            int scrollbarWidth = 3;
            int scrollbarHeight = height / 5; // TODO: should scale according to content height
            int scrollbarX = x + width - scrollbarWidth;
            int scrollbarY = y + (int) (scrollPosition / (contentHeight - height) * (height - scrollbarHeight));
            int alpha = Math.min(255, scrollbarTicks * 10);
            drawRect(scrollbarX, scrollbarY, scrollbarX + scrollbarWidth, scrollbarY + scrollbarHeight, 0x808080 | alpha << 24);
        }
    }

    public void drawContents(int mouseX, int mouseY, float partialTicks) {
        for (GuiItem element : elements) {
            element.draw(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void update() {
        if (scrollbarTicks > 0) {
            scrollbarTicks--;
        }
    }

    private void clampScrollPosition() {
        if (contentHeight < height) {
            scrollPosition = 0;
        } else {
            scrollPosition = MathHelper.clamp_float(scrollPosition, 0.0f, contentHeight - height);
        }
    }

    public void handleMouseInput() {
        ScaledResolution res = new ScaledResolution(mc);
        int mouseX = (int) (res.getScaledWidth_double() / mc.displayWidth * Mouse.getEventX());
        int mouseY = (int) (res.getScaledHeight_double() / mc.displayHeight * (mc.displayHeight - Mouse.getEventY()));

        if (containsPoint(mouseX, mouseY)) {
            int wheel = Mouse.getEventDWheel();
            if (wheel != 0 && height < contentHeight) {
                scrollbarTicks = 50;
                scrollPosition -= wheel * 0.1f;
                clampScrollPosition();
            }

            int mouseButton = Mouse.getEventButton();
            if (Mouse.getEventButtonState()) {
                mouseClicked(mouseX - x, mouseY - y + (int) scrollPosition, mouseButton);
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (GuiItem element : elements) {
            if (element.containsPoint(mouseX, mouseY)) {
                element.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }
}