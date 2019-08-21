package io.prplz.skypixel.command;

import io.prplz.skypixel.Skypixel;
import io.prplz.skypixel.gui.settings.SettingsGui;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class SkypixelCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "skypixel";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/skypixel";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        Skypixel.get().getTickExecutor().execute(() -> {
            SettingsGui gui = new SettingsGui(null);
            gui.setFromCommand(true);
            Minecraft.getMinecraft().displayGuiScreen(gui);
        });
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
