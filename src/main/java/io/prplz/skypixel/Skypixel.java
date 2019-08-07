package io.prplz.skypixel;

import io.prplz.skypixel.gui.ContainerSkyblockEnchantment;
import io.prplz.skypixel.gui.GuiChestNoDrag;
import io.prplz.skypixel.gui.GuiSkyblockEnchantment;
import io.prplz.skypixel.utils.NBTUtils;
import io.prplz.skypixel.utils.ScoreboardUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import static net.minecraftforge.fml.common.Mod.EventHandler;

@Mod(modid = "skypixel", useMetadata = true)
public class Skypixel {

    private final Minecraft mc = Minecraft.getMinecraft();
    private int messageDelay = 0;
    private IChatComponent updateMessage;
    private boolean inHypixel;
    private boolean inSkyblock;
    private boolean forceSkyblock;
    private Keybinds keybinds;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        forceSkyblock = Boolean.getBoolean("skypixel.forceSkyblock");

        keybinds = new Keybinds(this);
        keybinds.register();

        MinecraftForge.EVENT_BUS.register(this);

        String updateUrl = System.getProperty("skypixel.updateUrl", "%%UPDATE_URL%%");
        UpdateChecker updater = new UpdateChecker(updateUrl, res -> updateMessage = res.getUpdateMessage());
        updater.start();
    }

    public boolean isInHypixel() {
        return inHypixel;
    }

    public boolean isInSkyblock() {
        return inSkyblock;
    }

    @SubscribeEvent
    public void onConnected(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        // Server may not be hypixel, we will check for sidebar clues in tick event
        inHypixel = false;
    }

    @SubscribeEvent
    public void onDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        // No longer in a server
        inHypixel = false;
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        // World changed, we may no longer be in skyblock
        inSkyblock = false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.thePlayer == null) {
            return;
        }

        if (updateMessage != null) {
            if (++messageDelay == 80) {
                mc.thePlayer.addChatMessage(updateMessage);
                updateMessage = null;
            }
        }

        if (!inHypixel) {
            // Does tab header contain MC.HYPIXEL.NET"
            IChatComponent tabHeader = mc.ingameGUI.getTabList().header;
            if (tabHeader != null && tabHeader.getUnformattedText().contains("MC.HYPIXEL.NET")) {
                inHypixel = true;
            }
        }

        if (inHypixel && !inSkyblock) {
            // Does sidebar title contain "SKYBLOCK"
            ScoreObjective sidebar = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(ScoreboardUtils.DISPLAY_SLOT_SIDEBAR);
            if (sidebar != null) {
                if (EnumChatFormatting.getTextWithoutFormattingCodes(sidebar.getDisplayName()).contains("SKYBLOCK")) {
                    inSkyblock = true;
                }
            }
        }

        if (forceSkyblock) {
            inHypixel = true;
            inSkyblock = true;
        }
    }

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent event) {
        if (!inSkyblock) {
            return;
        }
        if (event.itemStack.hasTagCompound()) {
            if (event.itemStack.getTagCompound().hasKey("ExtraAttributes", NBTUtils.TYPE_ID_COMPOUND)) {
                NBTTagCompound extraAttributes = event.itemStack.getTagCompound().getCompoundTag("ExtraAttributes");
                if (extraAttributes.hasKey("anvil_uses", NBTUtils.TYPE_ID_INT)) {
                    event.toolTip.add(1, EnumChatFormatting.GRAY + "Anvil uses: " + extraAttributes.getInteger("anvil_uses"));
                }
                if (event.showAdvancedItemTooltips) {
                    event.toolTip.add(EnumChatFormatting.DARK_GRAY + "Skyblock: " + extraAttributes.getString("id"));
                }
            }
        }
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        if (!inSkyblock) {
            return;
        }
        if (event.gui instanceof GuiChest) {
            GuiChest gui = (GuiChest) event.gui;
            ContainerChest container = (ContainerChest) gui.inventorySlots;
            IInventory inventory = container.getLowerChestInventory();
            if (inventory.getName().equals("Enchant Item")) {
                event.gui = new GuiSkyblockEnchantment(new ContainerSkyblockEnchantment(mc.thePlayer.inventory, inventory));
            } else if (inventory.getName().equals("Brewing Stand") || inventory.getName().equals("Anvil")
                    || inventory.getName().contains(" Minion ") || inventory.getName().equals("Runic Pedestal")
                    || inventory.getName().equals("Reforge Item")) {
                event.gui = new GuiChestNoDrag(container);
            }
        }
    }
}
