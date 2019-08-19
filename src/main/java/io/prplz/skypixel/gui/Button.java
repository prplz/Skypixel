package io.prplz.skypixel.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class Button extends GuiItem {

    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");

    protected String text;

    public Button(String text, int width) {
        this.text = text;
        this.width = width;
        height = 20;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(buttonTextures);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        boolean mouseOver = containsPoint(mouseX, mouseY);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int textureY = mouseOver ? 86 : 66;
        drawTexturedModalRect(x, y, 0, textureY, width / 2, height);
        drawTexturedModalRect(x + width / 2, y, 200 - width / 2, textureY, width / 2, height);
        int color = mouseOver ? 0xFFFFA0 : 0xE0E0E0;
        drawCenteredString(mc.fontRendererObj, text, x + width / 2, y + (height - 8) / 2, color);
    }
}
