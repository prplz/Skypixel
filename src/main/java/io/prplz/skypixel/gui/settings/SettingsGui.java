package io.prplz.skypixel.gui.settings;

import io.prplz.skypixel.Settings;
import io.prplz.skypixel.Skypixel;
import io.prplz.skypixel.gui.BooleanPropertyButton;
import io.prplz.skypixel.gui.Image;
import io.prplz.skypixel.gui.Label;
import io.prplz.skypixel.gui.Pane;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static io.prplz.skypixel.Translation.*;

public class SettingsGui extends GuiScreen {

    private final Skypixel skypixel = Skypixel.get();
    private final GuiScreen parent;
    private FeatureList featureList;
    private final List<Feature> features = new ArrayList<>();
    private Pane rightPane;
    private boolean fromCommand;

    public SettingsGui(GuiScreen parent) {
        this.parent = parent;
        Settings settings = skypixel.getSettings();
        features.add(new Feature(
                FEATURE_ENCHANTMENT_MENU.format(),
                Feature.enabledIf(settings.enchantmentGuiEnabled),
                new FeaturePane(
                        new BooleanPropertyButton(settings.enchantmentGuiEnabled, FEATURE_ENCHANTMENT_MENU, 200),
                        Label.of("Replaces Hypixel's enchantment table menu with a custom menu that looks similar to the one in regular Minecraft."),
                        new Image(new ResourceLocation("skypixel", "screenshots/enchant.png"))
                )
        ));
        features.add(new Feature(
                FEATURE_ANVIL_USES.format(),
                Feature.enabledIf(settings.anvilUsesEnabled),
                new FeaturePane(
                        new BooleanPropertyButton(settings.anvilUsesEnabled, FEATURE_ANVIL_USES, 200),
                        Label.of("Shows the item's anvil uses in the tooltip."),
                        Label.of("A higher anvil use count incurs a higher cost when combining items in an anvil."),
                        new Image(new ResourceLocation("skypixel", "screenshots/anvil_uses.png"))
                )
        ));
        features.add(new Feature(
                FEATURE_CANCEL_ITEM_DAMAGE.format(),
                Feature.enabledIf(settings.cancelItemDamage),
                new FeaturePane(
                        new BooleanPropertyButton(settings.cancelItemDamage, FEATURE_CANCEL_ITEM_DAMAGE, 200),
                        Label.of("Prevents the game client from trying to apply damage to tools and weapons."),
                        Label.of("This fixes some buggy behaviour caused by the server constantly updating your items.")
                )
        ));
        features.add(new Feature(
                FEATURE_CANCEL_INVENTORY_DRAG.format(),
                Feature.enabledIf(settings.cancelInventoryDrag),
                new FeaturePane(
                        new BooleanPropertyButton(settings.cancelInventoryDrag, FEATURE_CANCEL_INVENTORY_DRAG, 200),
                        Label.of("Disabled the item dragging mechanic in a few affected Hypixel menus that don't properly support it, making them less glitchy."),
                        Label.of("This feature may be be removed once Hypixel fixes the bug on their end."),
                        Label.of("Affected menus: Brewing Stand, Anvil, Minions, Runic Pedestal and Reforging.")
                )
        ));
        features.add(new Feature(
                FEATURE_KEYBINDS.format(),
                Feature.enabledIf(skypixel.getKeybinds()::isAnyKeybindEnabled),
                new FeaturePane(
                        Label.of("Keybinds can be configured in Minecraft's controls menu.")
                )
        ));
        featureList = new FeatureList(this, features);

        String text = "Select a feature on the left to get started.";
        if (!fromCommand) {
            text += "\nYou can access this menu in-game using the \u00a7e/skypixel\u00a7r command.";
        }
        rightPane = new FeaturePane(Label.of(text));
    }

    public boolean isFromCommand() {
        return fromCommand;
    }

    public void setFromCommand(boolean fromCommand) {
        this.fromCommand = fromCommand;
    }

    @Override
    public void initGui() {
        featureList.setPosition(10, 10);
        featureList.setSize(150, height - 20);
        rightPane.setPosition(featureList.getWidth() + 20, 40);
        rightPane.setSize(width - featureList.getWidth() - 30, height - 50);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GL11.glPushMatrix();
        GL11.glTranslatef((featureList.getWidth() + 10 + width) / 2.0f, 10.0f, 0.0f);
        GL11.glScalef(2.0f, 2.0f, 1.0f);
        drawCenteredString(fontRendererObj, "Skypixel", 0, 0, 0xFFFFFF);
        GL11.glPopMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
        featureList.draw(mouseX, mouseY, partialTicks);
        rightPane.draw(mouseX, mouseY, partialTicks);
    }

    @Override
    public void updateScreen() {
        featureList.update();
        rightPane.update();
    }

    @Override
    public void handleMouseInput() {
        featureList.handleMouseInput();
        rightPane.handleMouseInput();
    }

    @Override
    protected void keyTyped(char eventChar, int eventKey) {
        if (eventKey == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(parent);
        }
    }

    public void setSelectedFeature(Feature feature) {
        rightPane = feature.getGui();
        initGui();
        skypixel.trySaveSettings("Failed to write " + skypixel.getSettingsFile());
    }

    @Override
    public void onGuiClosed() {
        skypixel.trySaveSettings("Failed to write " + skypixel.getSettingsFile());
    }
}
