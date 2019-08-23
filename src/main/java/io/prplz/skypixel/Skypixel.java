package io.prplz.skypixel;

import io.prplz.skypixel.command.SkypixelCommand;
import io.prplz.skypixel.gui.replacement.ContainerSkyblockEnchantment;
import io.prplz.skypixel.gui.replacement.GuiChestNoDrag;
import io.prplz.skypixel.gui.replacement.GuiSkyblockEnchantment;
import io.prplz.skypixel.utils.ItemUtils;
import io.prplz.skypixel.utils.NBTUtils;
import io.prplz.skypixel.utils.ScoreboardUtils;
import io.prplz.skypixel.utils.TickExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import static net.minecraftforge.fml.common.Mod.EventHandler;

@Mod(modid = "skypixel", guiFactory = "io.prplz.skypixel.gui.settings.SettingsGuiFactory", useMetadata = true)
public class Skypixel {

    private static final Logger logger = LogManager.getLogger();
    private static Skypixel instance;
    private final Minecraft mc = Minecraft.getMinecraft();
    private File directory;
    private File settingsFile;
    private Settings settings;
    private int messageDelay = 0;
    private IChatComponent updateMessage;
    private boolean inHypixel;
    private boolean inSkyblock;
    private boolean forceSkyblock;
    private Keybinds keybinds;
    private TickExecutor tickExecutor;
    private FontRenderer romanNumeralsFont;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;

        directory = new File(event.getModConfigurationDirectory(), "skypixel");
        directory.mkdirs();
        settingsFile = new File(directory, "settings.json");

        try (FileReader in = new FileReader(settingsFile)) {
            settings = Settings.gson.fromJson(in, Settings.class);
        } catch (FileNotFoundException ignored) {
            logger.info("Creating " + settingsFile);
            settings = new Settings();
            trySaveSettings("Failed to create " + settingsFile);
        } catch (Exception ex) {
            logger.warn("Failed to load " + settingsFile, ex);
            // Backup the broken file to a unique name
            settingsFile.renameTo(new File(settingsFile.getParent(), settingsFile.getName() + "." + System.currentTimeMillis()));
            settings = new Settings();
            trySaveSettings("Failed to create " + settingsFile);
        }

        forceSkyblock = Boolean.getBoolean("skypixel.forceSkyblock");

        keybinds = new Keybinds(this);
        keybinds.register();

        tickExecutor = new TickExecutor();
        tickExecutor.register();

        ClientCommandHandler.instance.registerCommand(new SkypixelCommand());

        MinecraftForge.EVENT_BUS.register(this);

        String updateUrl = System.getProperty("skypixel.updateUrl", "%%UPDATE_URL%%");
        UpdateChecker updater = new UpdateChecker(updateUrl, res -> updateMessage = res.getUpdateMessage());
        updater.start();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        romanNumeralsFont = new FontRenderer(mc.gameSettings, new ResourceLocation("skypixel", "font/roman_numerals.png"), mc.renderEngine, false);
        romanNumeralsFont.onResourceManagerReload(mc.getResourceManager());
    }

    public static Skypixel get() {
        return instance;
    }

    public File getSettingsFile() {
        return settingsFile;
    }

    public Settings getSettings() {
        return settings;
    }

    public boolean isInHypixel() {
        return inHypixel;
    }

    public boolean isInSkyblock() {
        return inSkyblock;
    }

    public Keybinds getKeybinds() {
        return keybinds;
    }

    public TickExecutor getTickExecutor() {
        return tickExecutor;
    }

    public FontRenderer getRomanNumeralsFont() {
        return romanNumeralsFont;
    }

    public void saveSettings() throws Exception {
        try (FileWriter writer = new FileWriter(settingsFile)) {
            Settings.gson.toJson(settings, writer);
        }
    }

    public void trySaveSettings(String message) {
        try {
            saveSettings();
        } catch (Exception ex) {
            logger.warn(message, ex);
        }
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
        NBTTagCompound skyblockData = ItemUtils.getExtraAttributes(event.itemStack);
        if (skyblockData != null) {
            if (settings.anvilUsesEnabled.get() && skyblockData.hasKey("anvil_uses", NBTUtils.TYPE_ID_INT)) {
                event.toolTip.add(1, EnumChatFormatting.GRAY + "Anvil uses: " + skyblockData.getInteger("anvil_uses"));
            }
            if (event.showAdvancedItemTooltips) {
                event.toolTip.add(EnumChatFormatting.DARK_GRAY + "Skyblock: " + skyblockData.getString("id"));
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
                if (settings.enchantmentGuiEnabled.get()) {
                    event.gui = new GuiSkyblockEnchantment(new ContainerSkyblockEnchantment(mc.thePlayer.inventory, inventory));
                } else if (settings.cancelInventoryDrag.get()) {
                    event.gui = new GuiChestNoDrag(container);
                }
            } else if (inventory.getName().equals("Brewing Stand") || inventory.getName().equals("Anvil")
                    || inventory.getName().contains(" Minion ") || inventory.getName().equals("Runic Pedestal")
                    || inventory.getName().equals("Reforge Item")) {
                if (settings.cancelInventoryDrag.get()) {
                    event.gui = new GuiChestNoDrag(container);
                }
            }
        }
    }
}
