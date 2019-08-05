package io.prplz.skypixel.gui;

import io.prplz.skypixel.utils.ItemUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Most of this copied from GuiEnchantment
public class GuiSkyblockEnchantment extends GuiContainer {

    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("skypixel", "gui/enchant.png");
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE = new ResourceLocation("textures/entity/enchanting_table_book.png");
    private static final Pattern ENCHANT_BUTTON_NAME_PATTERN = Pattern.compile("(?:\u00a7[0-9A-FK-OR])?(\\d+) LEVEL", Pattern.CASE_INSENSITIVE);
    private static final ModelBook MODEL_BOOK = new ModelBook();

    private float field_147071_v;
    private float field_147069_w;
    private float field_147082_x;
    private float field_147081_y;
    private float field_147080_z;
    private float field_147076_A;
    private ItemStack enchantItem;
    private Random random = new Random();

    private ContainerSkyblockEnchantment container;

    public GuiSkyblockEnchantment(ContainerSkyblockEnchantment container) {
        super(container);
        this.container = container;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(StatCollector.translateToLocal("container.enchant"), 12, 5, 0x404040);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        updateAnimation();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        int guiX = (width - xSize) / 2;
        int guiY = (height - ySize) / 2;

        for (int slot = 0; slot < 3; ++slot) {
            int slotMouseX = mouseX - (guiX + 60);
            int slotMouseY = mouseY - (guiY + 14 + 19 * slot);

            if (slotMouseX >= 0 && slotMouseY >= 0 && slotMouseX < 108 && slotMouseY < 19) {
                mc.playerController.windowClick(container.windowId, container.getEnchantButtonIndex(slot), 0, 0, mc.thePlayer);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
        int guiX = (width - xSize) / 2;
        int guiY = (height - ySize) / 2;
        drawTexturedModalRect(guiX, guiY, 0, 0, xSize, ySize);
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        ScaledResolution scaledresolution = new ScaledResolution(mc);
        GlStateManager.viewport((scaledresolution.getScaledWidth() - 320) / 2 * scaledresolution.getScaleFactor(), (scaledresolution.getScaledHeight() - 240) / 2 * scaledresolution.getScaleFactor(), 320 * scaledresolution.getScaleFactor(), 240 * scaledresolution.getScaleFactor());
        GlStateManager.translate(-0.34F, 0.23F, 0.0F);
        Project.gluPerspective(90.0F, 1.3333334F, 9.0F, 80.0F);
        float f = 1.0F;
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(0.0F, 3.3F, -16.0F);
        GlStateManager.scale(f, f, f);
        float f1 = 5.0F;
        GlStateManager.scale(f1, f1, f1);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_BOOK_TEXTURE);
        GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
        float f2 = field_147076_A + (field_147080_z - field_147076_A) * partialTicks;
        GlStateManager.translate((1.0F - f2) * 0.2F, (1.0F - f2) * 0.1F, (1.0F - f2) * 0.25F);
        GlStateManager.rotate(-(1.0F - f2) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
        float f3 = field_147069_w + (field_147071_v - field_147069_w) * partialTicks + 0.25F;
        float f4 = field_147069_w + (field_147071_v - field_147069_w) * partialTicks + 0.75F;
        f3 = (f3 - (float) MathHelper.truncateDoubleToInt((double) f3)) * 1.6F - 0.3F;
        f4 = (f4 - (float) MathHelper.truncateDoubleToInt((double) f4)) * 1.6F - 0.3F;

        if (f3 < 0.0F) {
            f3 = 0.0F;
        }

        if (f4 < 0.0F) {
            f4 = 0.0F;
        }

        if (f3 > 1.0F) {
            f3 = 1.0F;
        }

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        GlStateManager.enableRescaleNormal();
        MODEL_BOOK.render(null, 0.0F, f3, f4, f2, 0.0F, 0.0625F);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.matrixMode(GL11.GL_PROJECTION);
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(GL11.GL_MODELVIEW);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        for (int slot = 0; slot < 3; slot++) {
            ItemStack enchantButton = container.getEnchantButton(slot);
            int slotX = guiX + 60;
            int slotY = guiY + 14 + 19 * slot;
            zLevel = 0.0F;
            mc.getTextureManager().bindTexture(ENCHANTMENT_TABLE_GUI_TEXTURE);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (enchantButton != null && enchantButton.getItem() == Items.experience_bottle && enchantButton.hasDisplayName()) {
                Matcher matcher = ENCHANT_BUTTON_NAME_PATTERN.matcher(enchantButton.getDisplayName());
                int level = 0;
                String levelString = "";
                if (matcher.find()) {
                    levelString = matcher.group(1);
                    level = Integer.parseInt(levelString);
                }
                int textColor;

                if (mc.thePlayer.experienceLevel < level) {
                    drawTexturedModalRect(slotX, slotY, 0, 185, 108, 19);
                    textColor = 0x407f10;
                } else {
                    int slotMouseX = mouseX - slotX;
                    int slotMouseY = mouseY - slotY;

                    if (slotMouseX >= 0 && slotMouseY >= 0 && slotMouseX < 108 && slotMouseY < 19) {
                        drawTexturedModalRect(slotX, slotY, 0, 204, 108, 19);
                    } else {
                        drawTexturedModalRect(slotX, slotY, 0, 166, 108, 19);
                    }
                    textColor = 0x80ff20;
                }

                String name = "???";
                List<String> lore = ItemUtils.getLore(enchantButton);
                if (lore != null && lore.size() >= 2) {
                    name = lore.get(1).substring(5);
                    if (mc.fontRendererObj.getStringWidth(name) > 86.0F) {
                        name = name.replace("Protection", "Prot.");
                        name = name.replace("Arthropods", "Arth.");
                    }
                }
                mc.fontRendererObj.drawStringWithShadow(name, slotX + 2, slotY + 2, textColor);
                mc.fontRendererObj.drawStringWithShadow(levelString, (float) (slotX + 106 - mc.fontRendererObj.getStringWidth(levelString)), (float) (slotY + 9), textColor);
            } else {
                drawTexturedModalRect(slotX, slotY, 0, 185, 108, 19);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        for (int slot = 0; slot < 3; slot++) {
            if (isPointInRegion(60, 14 + 19 * slot, 108, 17, mouseX, mouseY)) {
                ItemStack enchantButton = container.getEnchantButton(slot);
                if (enchantButton != null) {
                    drawHoveringText(enchantButton.getTooltip(mc.thePlayer, false), mouseX, mouseY);
                }
                break;
            }
        }
    }

    @Override
    protected void renderToolTip(ItemStack stack, int x, int y) {
        // Don't draw tooltip for the enchant item if it will cover the enchant list
        ItemStack enchantButton = container.getEnchantButton(0);
        if (enchantButton != null && enchantButton.getItem() == Items.experience_bottle && stack == container.getEnchantItem()) {
            return;
        }
        super.renderToolTip(stack, x, y);
    }

    public void updateAnimation() {
        ItemStack currentEnchantItem = container.getEnchantItem();

        if (!ItemStack.areItemStacksEqual(currentEnchantItem, enchantItem)) {
            enchantItem = currentEnchantItem;

            do {
                field_147082_x += (float) (random.nextInt(4) - random.nextInt(4));

            } while (!(field_147071_v > field_147082_x + 1.0F) && !(field_147071_v < field_147082_x - 1.0F));
        }

        field_147069_w = field_147071_v;
        field_147076_A = field_147080_z;
        boolean flag = false;

        for (int slot = 0; slot < 3; slot++) {
            ItemStack enchantButton = container.getEnchantButton(slot);
            if (enchantButton != null && enchantButton.getItem() == Items.experience_bottle) {
                flag = true;
            }
        }

        if (flag) {
            field_147080_z += 0.2F;
        } else {
            field_147080_z -= 0.2F;
        }

        field_147080_z = MathHelper.clamp_float(field_147080_z, 0.0F, 1.0F);
        float f1 = (field_147082_x - field_147071_v) * 0.4F;
        float f = 0.2F;
        f1 = MathHelper.clamp_float(f1, -f, f);
        field_147081_y += (f1 - field_147081_y) * 0.9F;
        field_147071_v += field_147081_y;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        // Overriding this prevents the "drag" click mechanic, which gets cancelled in most skyblock inventories.
        // Remove when/if hypixel fixes it.
    }
}
