package io.prplz.skypixel.asm.hook;

import io.prplz.skypixel.Skypixel;
import io.prplz.skypixel.gui.settings.SettingsGui;
import net.minecraft.client.Minecraft;

public class GuiScreenHook {

    private static final Minecraft mc = Minecraft.getMinecraft();

    /**
     * @return true to cancel message, false to proceed as normal
     */
    public static boolean sendChatMessage(String message) {
        if (message.equalsIgnoreCase("/skypixel")) {
            mc.ingameGUI.getChatGUI().addToSentMessages(message);
            SettingsGui settings = new SettingsGui(null);
            settings.setFromCommand(true);
            Skypixel.get().getTickExecutor().execute(() -> mc.displayGuiScreen(settings));
            return true;
        }
        return false;
    }
}
