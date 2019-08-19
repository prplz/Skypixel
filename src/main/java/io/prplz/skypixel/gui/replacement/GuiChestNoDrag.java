package io.prplz.skypixel.gui.replacement;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;

public class GuiChestNoDrag extends GuiChest {

    public GuiChestNoDrag(ContainerChest container) {
        super(Minecraft.getMinecraft().thePlayer.inventory, container.getLowerChestInventory());
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        // Overriding this prevents the "drag" click mechanic, which gets cancelled in most skyblock inventories.
        // Remove when/if hypixel fixes it.
    }
}