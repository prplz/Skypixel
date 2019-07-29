package io.prplz.skypixel.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerSkyblockEnchantment extends Container {

    public final IInventory chest;

    public ContainerSkyblockEnchantment(InventoryPlayer playerInventory, IInventory chest) {
        this.chest = chest;

        int numRows = chest.getSizeInventory() / 9;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < 9; col++) {
                if (row == 1 && col == 4) {
                    // The enchanting slot
                    addSlotToContainer(new Slot(chest, col + row * 9, 25, 47));
                } else {
                    // Place chest slots that we don't want to see way off the screen
                    addSlotToContainer(new Slot(chest, col + row * 9, 9999, 9999));
                }
            }
        }

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            addSlotToContainer(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    // Copied from ContainerChest
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = null;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int numRows = chest.getSizeInventory() / 9;

            if (index < numRows * 9) {
                if (!mergeItemStack(itemstack1, numRows * 9, inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!mergeItemStack(itemstack1, 0, numRows * 9, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public int getEnchantButtonIndex(int slot) {
        return 29 + slot * 2;
    }

    // Get the enchanting "button" in the chest
    public ItemStack getEnchantButton(int slot) {
        return chest.getStackInSlot(getEnchantButtonIndex(slot));
    }

    // Get the item being enchanted
    public ItemStack getEnchantItem() {
        return chest.getStackInSlot(13);
    }
}
